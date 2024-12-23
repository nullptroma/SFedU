package ru.freedominc.sfedu

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.freedominc.sfedu.data.UsersRepository
import ru.freedominc.sfedu.databinding.ActivityLoginBinding
import ru.freedominc.sfedu.domain.User
import ru.freedominc.sfedu.domain.UserManager
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var userManager: UserManager

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 30) {
            binding.root.setWindowInsetsAnimationCallback(object :
                WindowInsetsAnimation.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE) {
                override fun onProgress(
                    p0: WindowInsets,
                    p1: MutableList<WindowInsetsAnimation>
                ): WindowInsets {
                    val systemBars = p0.getInsets(WindowInsets.Type.systemBars())
                    val ime = p0.getInsets(WindowInsets.Type.ime())
                    binding.root.setPadding(
                        systemBars.left + ime.left,
                        systemBars.top + ime.top,
                        systemBars.right + ime.right,
                        systemBars.bottom + ime.bottom
                    )
                    return p0
                }

                override fun onEnd(animation: WindowInsetsAnimation) {
                    super.onEnd(animation)
                }
            })
        }
        else {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                val keyboard = insets.getInsets(WindowInsetsCompat.Type.ime())
                v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom + keyboard.bottom
                )
                insets
            }
        }

        binding.buttonLogin.setOnClickListener {
            if (checkLogin()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun checkLogin(): Boolean {
        if(!userManager.isReadyToWork) {
            Toast.makeText(this, getString(R.string.wait_toast), Toast.LENGTH_SHORT).show()
            return false
        }

        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        val res = userManager.tryLogin(email, password)

        if(!res)
            binding.buttonLogin.setBackgroundColor(Color.RED)
        return res
    }

}