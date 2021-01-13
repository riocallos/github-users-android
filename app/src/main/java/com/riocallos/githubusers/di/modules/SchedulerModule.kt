package com.riocallos.githubusers.di.modules

import com.riocallos.githubusers.base.BaseSchedulerProvider
import com.riocallos.githubusers.di.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SchedulerModule {
    @Provides
    @Singleton
    fun providesSchedulerSource(): BaseSchedulerProvider = SchedulerProvider.getInstance()
}