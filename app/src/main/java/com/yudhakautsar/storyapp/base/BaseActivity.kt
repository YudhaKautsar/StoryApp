package com.yudhakautsar.storyapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)

        setupObservers()
        setupViews()
        setupListeners()
    }

    protected open fun setupObservers() {}

    protected open fun setupViews() {}

    protected open fun setupListeners() {}

    protected open fun showLoading() {}

    protected open fun hideLoading() {}

    protected open fun showError(message: String) {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

