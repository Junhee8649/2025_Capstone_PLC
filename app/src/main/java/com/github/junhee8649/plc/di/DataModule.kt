package com.github.junhee8649.plc.di

import com.github.junhee8649.plc.data.repository.VarProcessRepository
import com.github.junhee8649.plc.data.repository.VarProcessRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindVarProcessRepository(
        impl: VarProcessRepositoryImpl
    ): VarProcessRepository
}