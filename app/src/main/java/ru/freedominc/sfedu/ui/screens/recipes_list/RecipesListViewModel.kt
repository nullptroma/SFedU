package ru.freedominc.sfedu.ui.screens.recipes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.freedominc.sfedu.data.FavoritesRepository
import ru.freedominc.sfedu.data.RecipesRepository
import ru.freedominc.sfedu.domain.DataPackage
import ru.freedominc.sfedu.domain.Recipe
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    fun getRecipesPackage(): StateFlow<DataPackage<Recipe>> = recipesRepository.recipes

    fun isFavorite(recipe: Recipe): Boolean {
        return favoritesRepository.favoriteRecipes.value.data.contains(recipe)
    }

    fun toggleFavoriteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            favoritesRepository.toggleRecipe(recipe)
        }
    }
}