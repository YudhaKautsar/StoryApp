package com.yudhakautsar.storyapp.domain.usecase.story

import com.yudhakautsar.storyapp.domain.model.Story
import com.yudhakautsar.storyapp.domain.repository.StoryRepository

class GetStoryDetailUseCase(private val repository: StoryRepository) {
    suspend operator fun invoke(token: String, id: String): Result<Story> {
        return repository.getStoryDetail(token, id)
    }
}
