package com.yudhakautsar.storyapp.data.remote.network

import com.yudhakautsar.storyapp.utils.NetworkConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface NetworkConfig {
    fun provideOkHttpClient(): OkHttpClient
    fun provideRetrofit(baseUrl: String): Retrofit
}

class NetworkConfigImpl(
    private val interceptors: List<Interceptor> = emptyList(),
    private val connectTimeout: Long = NetworkConstants.TIMEOUT_CONNECT,
    private val readTimeout: Long = NetworkConstants.TIMEOUT_READ,
    private val writeTimeout: Long = NetworkConstants.TIMEOUT_WRITE
) : NetworkConfig {

    override fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                interceptors.forEach { addInterceptor(it) }
                addInterceptor(createLoggingInterceptor())
            }
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .build()
    }

    override fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}

