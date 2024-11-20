package ru.freedominc.sfedu.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = DbUser::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("user_id"),
    onDelete = ForeignKey.CASCADE), ForeignKey(entity = DbRecipe::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("recipe_id"),
    onDelete = ForeignKey.CASCADE)],
    tableName = "favorites",
    primaryKeys = ["user_id", "recipe_id"]
)
data class DbFavoriteRecipe(
    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "recipe_id")
    val recipeId: Int,
) {
}