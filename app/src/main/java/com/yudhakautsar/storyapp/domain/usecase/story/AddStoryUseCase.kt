package com.yudhakautsar.storyapp.domain.usecase.story

import com.yudhakautsar.storyapp.domain.repository.StoryRepository
import java.io.File

class AddStoryUseCase(private val repository: StoryRepository) {
    suspend operator fun invoke(token: String, file: File, description: String): Result<String> {
        return repository.uploadStory(token, file, description)
    }
}
