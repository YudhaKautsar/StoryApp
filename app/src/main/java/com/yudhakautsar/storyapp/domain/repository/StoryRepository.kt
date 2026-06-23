package com.yudhakautsar.storyapp.domain.repository

import com.yudhakautsar.storyapp.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    fun getStories(token: String): Flow<List<Story>>
}
