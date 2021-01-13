package com.riocallos.githubusers.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(application: Application): SharedPreferences {
        val fileName = application.packageName + "_preferences"
        return application.getSharedPreferences(
                fileName,
                Context.MODE_PRIVATE
        )
    }

}