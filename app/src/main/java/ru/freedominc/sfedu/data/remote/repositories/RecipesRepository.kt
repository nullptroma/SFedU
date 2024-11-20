package ru.freedominc.sfedu.data.remote.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import ru.freedominc.sfedu.data.remote.api.RecipesApi
import ru.freedominc.sfedu.di.IoDispatcher
import ru.freedominc.sfedu.domain.Recipe
import ru.freedominc.sfedu.domain.RecipesPackage
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Singleton
class RecipesRepository @Inject constructor(
    private val api: RecipesApi,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) {
    val flowData: StateFlow<RecipesPackage>
        get() = _flowData
    private val _flowData: MutableStateFlow<RecipesPackage> = MutableStateFlow(RecipesPackage(isLoading = true))

    private fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    init {
        tickerFlow(10.seconds, 1.seconds).onEach {
            refresh()
        }.launchIn(CoroutineScope(ioDispatcher))
    }

    private fun refresh() {
        runBlocking {
            _flowData.value = _flowData.value.copy(
                isLoading = true
            )
            var value: List<Recipe>
            var error = false
            var message: String? = null
            try {
                value = api.getList().map {
                    Recipe(it.calorie, it.time, it.name, it.ingredients, it.difficulty)
                }

            } catch (e: Exception) {
                value = listOf()
                error = true
                message = e.message
            }

            _flowData.value =  RecipesPackage(value, error, message, false)
        }
    }
}