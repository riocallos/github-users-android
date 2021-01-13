package com.riocallos.githubusers.di.builders

import com.riocallos.githubusers.features.user.views.UserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract fun contributeUserFragment(): UserFragment

}