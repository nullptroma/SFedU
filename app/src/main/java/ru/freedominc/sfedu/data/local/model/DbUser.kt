package ru.freedominc.sfedu.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.freedominc.sfedu.domain.User

@Entity(tableName = "users")
data class DbUser(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val email: String,
    val password: String
) {
    fun toUser(): User = User(email, password)
}