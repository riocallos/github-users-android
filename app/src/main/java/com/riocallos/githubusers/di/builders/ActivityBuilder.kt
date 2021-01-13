package com.riocallos.githubusers.di.builders

import com.riocallos.githubusers.di.scopes.ActivityScope
import com.riocallos.githubusers.features.main.MainActivity
import com.riocallos.githubusers.features.user.views.UserActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeUserActivity(): UserActivity

}