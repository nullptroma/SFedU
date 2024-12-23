package ru.freedominc.sfedu.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.freedominc.sfedu.data.local.dao.RecipeDao
import ru.freedominc.sfedu.data.local.model.DbRecipe
import ru.freedominc.sfedu.data.remote.api.RecipesApi
import ru.freedominc.sfedu.di.IoDispatcher
import ru.freedominc.sfedu.domain.DataPackage
import ru.freedominc.sfedu.domain.Recipe
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Singleton
class RecipesRepository @Inject constructor(
    private val api: RecipesApi,
    private val dao: RecipeDao,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) {
    val recipes: StateFlow<DataPackage<Recipe>>
        get() = _recipes
    private val _recipes: MutableStateFlow<DataPackage<Recipe>> =
        MutableStateFlow(DataPackage(isLoading = true))

    private fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    init {
        CoroutineScope(ioDispatcher).launch {
            val dbRecipes = dao.getAll()
            _recipes.value = _recipes.value.copy(
                data = dbRecipes.map { it.toRecipe() }
            )
        }
        tickerFlow(1.minutes, 5.seconds).onEach {
            refresh()
        }.launchIn(CoroutineScope(ioDispatcher))
    }

    private fun refresh() {
        runBlocking {
            _recipes.value = _recipes.value.copy(
                isLoading = true
            )
            var lastState = _recipes.value
            var error = false
            var message: String? = null
            try {
                val remoteRecipes = api.getList().map {
                    Recipe(it.calorie, it.time, it.name, it.ingredients, it.difficulty)
                }
                val oldRecipes = dao.getAll().associateBy({ it.name }, { it.id })
                val dbRecipes = remoteRecipes.map {
                    with(it) {
                        val id = oldRecipes.getOrDefault(name, 0)
                        DbRecipe(
                            id,
                            calorie,
                            time,
                            name,
                            ingredients,
                            difficulty
                        )
                    }
                }
                dao.insertAll(*dbRecipes.toTypedArray())
                lastState = lastState.copy(
                    data = remoteRecipes
                )
            } catch (e: Exception) {
                error = true
                message = e.message
            }

            _recipes.value = lastState.copy(
                isError = error,
                errorMessage = message,
                isLoading = false
            )
        }
    }
}