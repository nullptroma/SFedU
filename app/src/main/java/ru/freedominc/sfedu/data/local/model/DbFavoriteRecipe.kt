package ru.freedominc.sfedu.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

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
)

data class DbUserAndRecipe(
    @Embedded val favorite: DbFavoriteRecipe,
    @Relation(parentColumn = "user_id", entityColumn = "id")
    val user: DbUser,
    @Relation(parentColumn = "recipe_id", entityColumn = "id")
    val recipe: DbRecipe
)