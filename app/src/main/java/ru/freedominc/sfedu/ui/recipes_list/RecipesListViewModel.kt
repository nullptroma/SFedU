package ru.freedominc.sfedu.ui.recipes_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.freedominc.sfedu.data.remote.repositories.RecipesRepository
import ru.freedominc.sfedu.domain.RecipesPackage
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(private val recipeRepo: RecipesRepository) : ViewModel() {
    fun getRecipesPackage(): Flow<RecipesPackage> = recipeRepo.flowData
}