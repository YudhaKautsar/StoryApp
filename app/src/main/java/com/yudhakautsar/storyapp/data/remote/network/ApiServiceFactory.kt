package com.yudhakautsar.storyapp.data.remote.network

class ApiServiceFactory(
    private val networkConfig: NetworkConfig
) {
    
    fun <T> createService(
        serviceClass: Class<T>,
        baseUrl: String
    ): T {
        return networkConfig.provideRetrofit(baseUrl).create(serviceClass)
    }
}

