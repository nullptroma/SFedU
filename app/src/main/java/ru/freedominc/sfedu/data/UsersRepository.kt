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
import kotlinx.coroutines.withContext
import ru.freedominc.sfedu.data.local.dao.RecipeDao
import ru.freedominc.sfedu.data.local.dao.UserDao
import ru.freedominc.sfedu.data.local.model.DbRecipe
import ru.freedominc.sfedu.data.remote.api.RecipesApi
import ru.freedominc.sfedu.di.IoDispatcher
import ru.freedominc.sfedu.domain.Recipe
import ru.freedominc.sfedu.domain.RecipesPackage
import ru.freedominc.sfedu.domain.User
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Singleton
class UsersRepository @Inject constructor(
    private val dao: UserDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getUsers(): List<User> = withContext(ioDispatcher) {
        return@withContext dao.getAll().map { it.toUser() }
    }
}