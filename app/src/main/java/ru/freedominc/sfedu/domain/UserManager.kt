package ru.freedominc.sfedu.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.freedominc.sfedu.data.UsersRepository
import ru.freedominc.sfedu.di.IoDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(val repository: UsersRepository, @IoDispatcher ioDispatcher: CoroutineDispatcher) {
    private lateinit var _users: List<User>
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser
    var isReadyToWork = false
        private set

    init {
        CoroutineScope(ioDispatcher).launch {
            val users = mutableListOf<User>()
            while(users.isEmpty()){
                users.addAll(repository.getUsers())
                delay(250)
            }
            _users = users
            isReadyToWork = true
        }
    }

    fun tryLogin(email: String, password: String) : Boolean {
        val user = _users.firstOrNull {
            it.email == email && it.password == password
        }
        if(user == null)
            return false

        _currentUser.value = user
        return true
    }
}