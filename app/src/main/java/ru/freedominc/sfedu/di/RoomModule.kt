package ru.freedominc.sfedu.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.freedominc.sfedu.data.local.AppDb
import ru.freedominc.sfedu.data.local.dao.FavoriteRecipeDao
import ru.freedominc.sfedu.data.local.dao.RecipeDao
import ru.freedominc.sfedu.data.local.dao.UserDao
import ru.freedominc.sfedu.data.local.model.DbUser
import ru.freedominc.sfedu.domain.User
import java.util.concurrent.Executors
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    fun provideUserDao(database: AppDb): UserDao {
        return database.userDao
    }

    @Provides
    fun provideRecipeDao(database: AppDb): RecipeDao {
        return database.recipeDao
    }

    @Provides
    fun provideFavoriteDao(database: AppDb): FavoriteRecipeDao {
        return database.favoriteDao
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext appContext: Context
    ): AppDb {
        var room: AppDb? = null
        val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadExecutor().execute {
                    room?.let {
                        val dao = it.userDao

                        val list = listOf(
                            User("roman@mail.ru", "1"),
                            User("pytkov@sfedu.ru", "2"),
                            User("roman@yandex.ru", "3"),
                            User("roman@gmail.com", "4"),
                            User("roman@sfedu.ru", "5")
                        ).map { DbUser(0, it.email, it.password) }
                        dao.insertAll(*list.toTypedArray())
                    }
                }
            }
        }
        room = Room.databaseBuilder(
            appContext, AppDb::class.java, "main-db"
        ).fallbackToDestructiveMigration().addCallback(callback).build()
        return room
    }
}