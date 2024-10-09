package ru.freedominc.sfedu

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import ru.freedominc.sfedu.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val emails = listOf(
        "roman@mail.ru",
        "pytkov@sfedu.ru",
        "roman@yandex.ru",
        "roman@gmail.com",
        "roman@sfedu.ru"
    )
    private val passwords = listOf("1", "2", "3", "4", "5")

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
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        for (i in 0 until minOf(emails.count(), passwords.count())) {
            if (emails[i] == email && passwords[i] == password) {
                return true
            }
        }

        binding.buttonLogin.setBackgroundColor(Color.RED)
        return false
    }

}