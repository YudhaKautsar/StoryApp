package com.yudhakautsar.storyapp

import android.app.Application
import com.yudhakautsar.storyapp.di.AppContainer
import com.yudhakautsar.storyapp.di.AppContainerImpl

class StoryApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainerImpl(this)
    }
}
