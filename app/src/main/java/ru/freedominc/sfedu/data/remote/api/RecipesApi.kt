package ru.freedominc.sfedu.data.remote.api

import ru.freedominc.sfedu.data.remote.model.RemoteRecipe
import retrofit2.http.GET

interface RecipesApi {
    @GET("recipes2022.json")
    suspend fun getList(): List<RemoteRecipe>
}