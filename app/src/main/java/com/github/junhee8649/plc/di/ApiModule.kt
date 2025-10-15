package com.github.junhee8649.plc.di

import com.github.junhee8649.plc.data.network.PLCApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun providePLCApiService(retrofit: Retrofit): PLCApiService {
        return retrofit.create(PLCApiService::class.java)
    }
}