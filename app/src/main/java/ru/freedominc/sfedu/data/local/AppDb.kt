package ru.freedominc.sfedu.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.freedominc.sfedu.data.local.dao.RecipeDao
import ru.freedominc.sfedu.data.local.dao.UserDao
import ru.freedominc.sfedu.data.local.model.DbFavoriteRecipe
import ru.freedominc.sfedu.data.local.model.DbRecipe
import ru.freedominc.sfedu.data.local.model.DbUser

@Database(entities = [DbUser::class, DbRecipe::class, DbFavoriteRecipe::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val recipeDao: RecipeDao
}