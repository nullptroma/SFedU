package ru.freedominc.sfedu.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.freedominc.sfedu.domain.Recipe
import ru.freedominc.sfedu.domain.User

@Entity(tableName = "recipes")
data class DbRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var calorie     : Int,
    var time        : Int,
    var name        : String,
    var ingredients : String,
    var difficulty  : Int
) {
    fun toRecipe(): Recipe = Recipe(calorie, time, name, ingredients, difficulty)
}