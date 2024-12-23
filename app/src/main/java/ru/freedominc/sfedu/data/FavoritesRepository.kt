package ru.freedominc.sfedu.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.freedominc.sfedu.data.local.dao.FavoriteRecipeDao
import ru.freedominc.sfedu.data.local.dao.RecipeDao
import ru.freedominc.sfedu.data.local.dao.UserDao
import ru.freedominc.sfedu.data.local.model.DbFavoriteRecipe
import ru.freedominc.sfedu.di.IoDispatcher
import ru.freedominc.sfedu.domain.DataPackage
import ru.freedominc.sfedu.domain.Recipe
import ru.freedominc.sfedu.domain.User
import ru.freedominc.sfedu.domain.UserManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    private val favoritesDao: FavoriteRecipeDao,
    private val usersDao: UserDao,
    private val recipesDao: RecipeDao,
    private val userManager: UserManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    val favoriteRecipes: StateFlow<DataPackage<Recipe>>
        get() = _favoriteRecipes
    private val _favoriteRecipes: MutableStateFlow<DataPackage<Recipe>> =
        MutableStateFlow(DataPackage(isLoading = true))
    private var _currentUser: User? = null

    init {
        CoroutineScope(ioDispatcher).launch {
            userManager.currentUser.collect {
                _currentUser = it
                update()
            }
        }
    }

    suspend fun toggleRecipe(recipe: Recipe) = withContext(ioDispatcher) {
        val user = _currentUser ?: return@withContext
        val curList = _favoriteRecipes.value.data.toMutableList()

        val isFavoriteNow = curList.contains(recipe)
        val dbUser = usersDao.getAll().single { it.email == user.email }
        val dbRecipe = recipesDao.getAll().single { it.toRecipe() == recipe }
        if(isFavoriteNow) {
            val userRecipes = favoritesDao.getAllUserToRecipe().filter { it.favorite.userId == dbUser.id }
            val toRemove = userRecipes.single { it.favorite.recipeId == dbRecipe.id }.favorite
            favoritesDao.delete(toRemove)
            curList.remove(recipe)
        }
        else {
            val dbFavoriteRecipe = DbFavoriteRecipe(dbUser.id, dbRecipe.id)
            favoritesDao.insertAll(dbFavoriteRecipe)
            curList.add(recipe)
        }
        _favoriteRecipes.value = DataPackage(
            data = curList,
            isError = false,
            errorMessage = null,
            isLoading = false
        )
    }

    private fun update() {
        val user = _currentUser ?: return
        val userRecipes = favoritesDao.getAllUserToRecipe().filter { it.user.email == user.email }.map { it.recipe.toRecipe() }
        _favoriteRecipes.value = DataPackage(
            data = userRecipes,
            isError = false,
            errorMessage = null,
            isLoading = false
        )
    }
}