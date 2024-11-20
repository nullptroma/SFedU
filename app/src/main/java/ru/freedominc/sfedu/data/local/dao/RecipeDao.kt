package ru.freedominc.sfedu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.freedominc.sfedu.data.local.model.DbRecipe
import ru.freedominc.sfedu.data.local.model.DbUser

@Dao
interface RecipeDao {
    @Insert
    fun insertAll(vararg recipe: DbRecipe)

    @Query("SELECT * FROM recipes")
    fun getAll(): List<DbRecipe>

    @Query("DELETE FROM recipes")
    fun nukeTable()
}