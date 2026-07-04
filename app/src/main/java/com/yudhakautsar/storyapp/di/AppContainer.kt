package com.yudhakautsar.storyapp.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.yudhakautsar.storyapp.data.local.UserPreference
import com.yudhakautsar.storyapp.data.remote.api.AuthApiService
import com.yudhakautsar.storyapp.data.remote.api.StoryApiService
import com.yudhakautsar.storyapp.data.remote.network.ApiServiceFactory
import com.yudhakautsar.storyapp.data.remote.network.NetworkConfig
import com.yudhakautsar.storyapp.data.remote.network.NetworkConfigImpl
import com.yudhakautsar.storyapp.data.repository.AuthRepositoryImpl
import com.yudhakautsar.storyapp.data.repository.StoryRepositoryImpl
import com.yudhakautsar.storyapp.domain.repository.AuthRepository
import com.yudhakautsar.storyapp.domain.repository.StoryRepository
import com.yudhakautsar.storyapp.domain.usecase.auth.LoginUseCase
import com.yudhakautsar.storyapp.domain.usecase.auth.RegisterUseCase
import com.yudhakautsar.storyapp.domain.usecase.story.AddStoryUseCase
import com.yudhakautsar.storyapp.domain.usecase.story.GetStoriesUseCase
import com.yudhakautsar.storyapp.domain.usecase.story.GetStoryDetailUseCase
import com.yudhakautsar.storyapp.utils.Constants

private val Context.dataStore by preferencesDataStore(name = Constants.PREF_NAME)

interface AppContainer {
    val loginUseCase: LoginUseCase
    val registerUseCase: RegisterUseCase
    val getStoriesUseCase: GetStoriesUseCase
    val getStoryDetailUseCase: GetStoryDetailUseCase
    val addStoryUseCase: AddStoryUseCase
    val userPreference: UserPreference
}

class AppContainerImpl(private val context: Context) : AppContainer {

    private val networkConfig: NetworkConfig by lazy {
        NetworkConfigImpl()
    }

    private val apiServiceFactory: ApiServiceFactory by lazy {
        ApiServiceFactory(networkConfig)
    }

    private val authApiService: AuthApiService by lazy {
        apiServiceFactory.createService(
            AuthApiService::class.java,
            Constants.BASE_URL
        )
    }

    private val storyApiService: StoryApiService by lazy {
        apiServiceFactory.createService(
            StoryApiService::class.java,
            Constants.BASE_URL
        )
    }

    private val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authApiService)
    }

    private val storyRepository: StoryRepository by lazy {
        StoryRepositoryImpl(storyApiService)
    }

    override val userPreference: UserPreference by lazy {
        UserPreference(context.dataStore)
    }

    override val loginUseCase: LoginUseCase by lazy {
        LoginUseCase(authRepository)
    }

    override val registerUseCase: RegisterUseCase by lazy {
        RegisterUseCase(authRepository)
    }

    override val getStoriesUseCase: GetStoriesUseCase by lazy {
        GetStoriesUseCase(storyRepository)
    }

    override val getStoryDetailUseCase: GetStoryDetailUseCase by lazy {
        GetStoryDetailUseCase(storyRepository)
    }

    override val addStoryUseCase: AddStoryUseCase by lazy {
        AddStoryUseCase(storyRepository)
    }
}
