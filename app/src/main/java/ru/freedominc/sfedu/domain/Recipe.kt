package ru.freedominc.sfedu.domain

data class Recipe(
    var calorie     : Int,
    var time        : Int,
    var name        : String,
    var ingredients : String,
    var difficulty  : Int
)