package com.riocallos.githubusers.di

import android.app.Application
import com.riocallos.githubusers.base.BaseApplication
import com.riocallos.githubusers.di.builders.ActivityBuilder
import com.riocallos.githubusers.di.builders.FragmentBuilder
import com.riocallos.githubusers.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AppModule::class,
            AndroidSupportInjectionModule::class,
            ActivityBuilder::class,
            FragmentBuilder::class,
            ViewModelModule::class,
            SchedulerModule::class,
            CacheModule::class,
            NetworkModule::class,
            ApiServiceModule::class,
            RepositoryModule::class,
            DatabaseModule::class
        ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(app: BaseApplication)
}