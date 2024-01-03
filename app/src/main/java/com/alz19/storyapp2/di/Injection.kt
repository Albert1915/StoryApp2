package com.alz19.storyapp2.di

import android.content.Context
import com.alz19.storyapp2.data.story.StoryDatabase
import com.alz19.storyapp2.helper.preference.TokenPreference
import com.alz19.storyapp2.helper.preference.dataStore
import com.alz19.storyapp2.repository.story.StoryRepositoryImpl
import com.alz19.storyapp2.repository.token.TokenRepositoryImpl

object Injection {
    fun provideTokenRepository(context: Context) : TokenRepositoryImpl {
        val pref = TokenPreference.getInstance(context.dataStore)
        return TokenRepositoryImpl.getInstance(pref)
    }

    fun  provideStoryRepository (context: Context) : StoryRepositoryImpl {
        val database = StoryDatabase.getDatabase(context)
        return StoryRepositoryImpl(database)
    }
}