package ru.freedominc.sfedu.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.freedominc.sfedu.data.local.model.DbFavoriteRecipe
import ru.freedominc.sfedu.data.local.model.DbRecipe
import ru.freedominc.sfedu.data.local.model.DbUserAndRecipe

@Dao
interface FavoriteRecipeDao {
    @Insert
    fun insertAll(vararg favorite: DbFavoriteRecipe)

    @Query("SELECT * FROM favorites")
    fun getAll(): List<DbFavoriteRecipe>

    @Query("SELECT * FROM favorites")
    fun getAllUserToRecipe(): List<DbUserAndRecipe>

    @Delete
    fun delete(vararg favorite: DbFavoriteRecipe)

    @Query("DELETE FROM favorites")
    fun nukeTable()
}