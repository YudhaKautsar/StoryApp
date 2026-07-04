package com.yudhakautsar.storyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class StoryDetailResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("story")
    val story: StoryDto
)
