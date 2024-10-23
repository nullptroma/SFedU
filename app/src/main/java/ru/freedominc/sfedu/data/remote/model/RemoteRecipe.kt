package ru.freedominc.sfedu.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteRecipe (
    @SerializedName("Calorie"     ) var calorie     : Int,
    @SerializedName("Time"        ) var time        : Int,
    @SerializedName("Name"        ) var name        : String,
    @SerializedName("Ingredients" ) var ingredients : String,
    @SerializedName("Difficulty"  ) var difficulty  : Int
)
