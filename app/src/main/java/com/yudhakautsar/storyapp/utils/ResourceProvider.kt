package com.yudhakautsar.storyapp.utils

import android.content.Context
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes resId: Int): String
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
}

class ResourceProviderImpl(
    private val context: Context
) : ResourceProvider {
    
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
    
    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }
}

