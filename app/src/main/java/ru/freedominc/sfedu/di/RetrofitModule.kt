package ru.freedominc.sfedu.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.freedominc.sfedu.data.remote.api.RecipesApi
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RecipesRetrofit

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Singleton
    @Provides
    @RecipesRetrofit
    fun provideRecipesRetrofit(): Retrofit {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://raw.githubusercontent.com/Lpirskaya/JsonLab/master/")
            .build()
    }

    @Provides
    fun provideRecipesApi(@RecipesRetrofit retrofit: Retrofit): RecipesApi {
        return retrofit.create(RecipesApi::class.java)
    }
}