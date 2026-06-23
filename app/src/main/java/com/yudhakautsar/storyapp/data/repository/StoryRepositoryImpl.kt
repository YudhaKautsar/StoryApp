package com.yudhakautsar.storyapp.data.repository

import com.yudhakautsar.storyapp.data.remote.api.StoryApiService
import com.yudhakautsar.storyapp.domain.model.Story
import com.yudhakautsar.storyapp.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StoryRepositoryImpl(
    private val apiService: StoryApiService
) : StoryRepository {

    override fun getStories(token: String): Flow<List<Story>> = flow {
        val response = apiService.getStories("Bearer $token")
        val stories = response.listStory.map {
            Story(
                id = it.id,
                name = it.name,
                description = it.description,
                photoUrl = it.photoUrl,
                createdAt = it.createdAt,
                lat = it.lat,
                lon = it.lon
            )
        }
        emit(stories)
    }
}
