package ru.freedominc.sfedu.ui.screens.favorite_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.freedominc.sfedu.data.FavoritesRepository
import ru.freedominc.sfedu.domain.DataPackage
import ru.freedominc.sfedu.domain.Recipe
import javax.inject.Inject

@HiltViewModel
class FavoritesListViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    fun getFavoriteRecipesPackage(): StateFlow<DataPackage<Recipe>> = favoritesRepository.favoriteRecipes

    fun isFavorite(recipe: Recipe): Boolean {
        return favoritesRepository.favoriteRecipes.value.data.contains(recipe)
    }

    fun toggleFavoriteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            favoritesRepository.toggleRecipe(recipe)
        }
    }
}