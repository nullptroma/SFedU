package ru.freedominc.sfedu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.freedominc.sfedu.data.local.model.DbRecipe
import ru.freedominc.sfedu.data.local.model.DbUser

@Dao
interface UserDao {
    @Insert
    fun insertAll(vararg user: DbUser)

    @Query("SELECT * FROM users")
    fun getAll(): List<DbUser>

    @Query("DELETE FROM users")
    fun nukeTable()
}