package ru.freedominc.sfedu.domain

data class RecipesPackage(
    val recipes: List<Recipe> = listOf(),
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)
