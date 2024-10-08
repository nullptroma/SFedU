package ru.freedominc.sfedu.ui.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.freedominc.sfedu.R
import ru.freedominc.sfedu.databinding.FragmentRecipeBinding
import ru.freedominc.sfedu.di.RecipeArgs
import ru.freedominc.sfedu.navigation.NavigationHelper
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    @Inject
    @RecipeArgs
    @JvmField
    var args: Bundle? = null

    private var _binding: FragmentRecipeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recipeViewModel =
            ViewModelProvider(this).get(RecipeViewModel::class.java)

        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recipe = args?.getString("recipe")
        if(recipe == null) {
            binding.textRecipe.text = resources.getString(R.string.no_recipe)
        }
        else {
            binding.textRecipe.text = resources.getString(R.string.recipe, recipe)
        }
        return root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}