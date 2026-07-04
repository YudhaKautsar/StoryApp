package com.yudhakautsar.storyapp.domain.repository

import com.yudhakautsar.storyapp.domain.model.Story
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoryRepository {
    fun getStories(token: String): Flow<List<Story>>
    suspend fun getStoryDetail(token: String, id: String): Result<Story>
    suspend fun uploadStory(token: String, file: File, description: String): Result<String>
}
