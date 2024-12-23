package ru.freedominc.sfedu.ui.screens.recipe

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
import ru.freedominc.sfedu.di.CurrentRecipe
import ru.freedominc.sfedu.domain.Recipe
import ru.freedominc.sfedu.navigation.NavigationHelper
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    @Inject
    @CurrentRecipe
    @JvmField
    var currentRecipe: Recipe? = null

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

        val recipe = currentRecipe
        if(recipe == null) {
            binding.tvRecipeNotSelected.visibility = View.VISIBLE
            binding.recipeInfo.visibility = View.GONE
        }
        else {
            binding.tvRecipeNotSelected.visibility = View.GONE
            binding.recipeInfo.visibility = View.VISIBLE
            with(binding) {
                tvRecipeName.text = recipe.name
                tvRecipeCalorie.text = getString(R.string.calorie, recipe.calorie)
                tvRecipeTime.text = getString(R.string.coock_time, recipe.time)
                tvRecipeIngredients.text = getString(R.string.ingredients, recipe.ingredients)
                tvRecipeDifficulty.text = getString(R.string.difficulty, recipe.difficulty)
            }
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