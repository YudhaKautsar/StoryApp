package com.yudhakautsar.storyapp.ui.story

import android.content.Intent
import androidx.activity.viewModels
import com.yudhakautsar.storyapp.StoryApplication
import com.yudhakautsar.storyapp.base.BaseActivity
import com.yudhakautsar.storyapp.base.ViewState
import com.yudhakautsar.storyapp.databinding.ActivityStoryListBinding
import com.yudhakautsar.storyapp.utils.gone
import com.yudhakautsar.storyapp.utils.showToast
import com.yudhakautsar.storyapp.utils.visible

class StoryListActivity : BaseActivity<ActivityStoryListBinding>() {

    private val viewModel: StoryListViewModel by viewModels {
        val appContainer = (application as StoryApplication).appContainer
        StoryListViewModelFactory(appContainer.getStoriesUseCase, appContainer.userPreference)
    }

    private val adapter: StoryAdapter by lazy { StoryAdapter() }

    override fun getViewBinding(): ActivityStoryListBinding {
        return ActivityStoryListBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        binding.rvStories.adapter = adapter
    }

    override fun setupObservers() {
        viewModel.storiesState.observe(this) { state ->
            when (state) {
                is ViewState.Loading -> showLoading()
                is ViewState.Success -> {
                    hideLoading()
                    adapter.submitList(state.data)
                }
                is ViewState.Error -> {
                    hideLoading()
                    showToast(state.message)
                }
                is ViewState.Empty -> {
                    hideLoading()
                    showToast("No stories found")
                }
                else -> hideLoading()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStories()
    }

    override fun showLoading() {
        binding.progressBar.visible()
    }

    override fun hideLoading() {
        binding.progressBar.gone()
    }
}
