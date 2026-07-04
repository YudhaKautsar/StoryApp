package com.yudhakautsar.storyapp.data.repository

import com.yudhakautsar.storyapp.data.remote.api.StoryApiService
import com.yudhakautsar.storyapp.domain.model.Story
import com.yudhakautsar.storyapp.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

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

    override suspend fun getStoryDetail(token: String, id: String): Result<Story> {
        return try {
            val response = apiService.getStoryDetail("Bearer $token", id)
            if (!response.error) {
                val storyDto = response.story
                Result.success(
                    Story(
                        id = storyDto.id,
                        name = storyDto.name,
                        description = storyDto.description,
                        photoUrl = storyDto.photoUrl,
                        createdAt = storyDto.createdAt,
                        lat = storyDto.lat,
                        lon = storyDto.lon
                    )
                )
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadStory(token: String, file: File, description: String): Result<String> {
        return try {
            val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val response = apiService.uploadStory("Bearer $token", imageMultipart, descriptionRequestBody)
            if (!response.error) {
                Result.success(response.message)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
