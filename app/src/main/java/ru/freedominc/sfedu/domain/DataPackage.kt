package ru.freedominc.sfedu.domain

data class DataPackage<T>(
    val data: List<T> = listOf(),
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)
