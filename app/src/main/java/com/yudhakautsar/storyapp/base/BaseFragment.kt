package com.yudhakautsar.storyapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

