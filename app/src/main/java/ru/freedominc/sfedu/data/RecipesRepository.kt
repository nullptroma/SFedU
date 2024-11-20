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
import ru.freedominc.sfedu.domain.Recipe
import ru.freedominc.sfedu.domain.RecipesPackage
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
    val flowData: StateFlow<RecipesPackage>
        get() = _flowData
    private val _flowData: MutableStateFlow<RecipesPackage> =
        MutableStateFlow(RecipesPackage(isLoading = true))

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
            _flowData.value = _flowData.value.copy(
                recipes = dbRecipes.map { it.toRecipe() }
            )
        }
        tickerFlow(1.minutes, 5.seconds).onEach {
            refresh()
        }.launchIn(CoroutineScope(ioDispatcher))
    }

    private fun refresh() {
        runBlocking {
            _flowData.value = _flowData.value.copy(
                isLoading = true
            )
            var lastState = _flowData.value
            var error = false
            var message: String? = null
            try {
                val value = api.getList().map {
                    Recipe(it.calorie, it.time, it.name, it.ingredients, it.difficulty)
                }
                val dbRecipes = value.map {
                    with(it) {
                        DbRecipe(
                            0,
                            calorie,
                            time,
                            name,
                            ingredients,
                            difficulty
                        )
                    }
                }
                dao.nukeTable()
                dao.insertAll(*dbRecipes.toTypedArray())
                lastState = lastState.copy(
                    recipes = value
                )
            } catch (e: Exception) {
                error = true
                message = e.message
            }

            _flowData.value = lastState.copy(
                isError = error,
                errorMessage = message,
                isLoading = false
            )
        }
    }
}