package ru.freedominc.sfedu.di

import android.os.Bundle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import ru.freedominc.sfedu.navigation.NavigationHelper
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RecipeArgs

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SlideshowArgs

@Module
@InstallIn(SingletonComponent::class)
class NavigationModule {

    @Provides
    @Singleton
    fun provideNavigationHelper() : NavigationHelper{
        return NavigationHelper()
    }

    @Provides
    @RecipeArgs
    fun provideRecipeArgs(navigationHelper: NavigationHelper) : Bundle? {
        return navigationHelper.currentRecipeArgs
    }
}

