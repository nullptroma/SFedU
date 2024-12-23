package ru.freedominc.sfedu.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.freedominc.sfedu.data.local.dao.UserDao
import ru.freedominc.sfedu.di.IoDispatcher
import ru.freedominc.sfedu.domain.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val dao: UserDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getUsers(): List<User> = withContext(ioDispatcher) {
        return@withContext dao.getAll().map { it.toUser() }
    }
}