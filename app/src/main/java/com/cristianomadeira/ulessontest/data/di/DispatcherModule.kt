package com.cristianomadeira.ulessontest.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: CoroutinesDispatchers)

enum class CoroutinesDispatchers {
    Default, IO
}

@Module
@InstallIn(SingletonComponent::class)
object DispatchedModule {

    @Provides
    @Dispatcher(CoroutinesDispatchers.IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(CoroutinesDispatchers.Default)
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}