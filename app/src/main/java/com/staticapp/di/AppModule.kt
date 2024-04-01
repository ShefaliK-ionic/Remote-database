package com.staticapp.di

import android.content.Context
import androidx.room.Room
import com.staticapp.roomDb.MessageDao
import com.staticapp.roomDb.MessageData
import com.staticapp.roomDb.MessageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context):MessageDatabase =
        Room.databaseBuilder(context,MessageDatabase::class.java,"msgDatabase")//.allowMainThreadQueries()
            .build()

    @Provides
    @Singleton
    fun provideDao(msgDatabase: MessageDatabase):MessageDao=msgDatabase.getDao()




}