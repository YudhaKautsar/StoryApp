package com.yudhakautsar.storyapp.data.remote.api

import com.yudhakautsar.storyapp.data.remote.dto.StoryResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StoryApiService {
    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): StoryResponse
}
