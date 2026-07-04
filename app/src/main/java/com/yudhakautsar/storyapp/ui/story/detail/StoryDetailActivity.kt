package com.yudhakautsar.storyapp.ui.story.detail

import android.os.Build
import androidx.activity.viewModels
import com.yudhakautsar.storyapp.StoryApplication
import com.yudhakautsar.storyapp.base.BaseActivity
import com.yudhakautsar.storyapp.base.ViewState
import com.yudhakautsar.storyapp.databinding.ActivityStoryDetailBinding
import com.yudhakautsar.storyapp.domain.model.Story
import com.yudhakautsar.storyapp.utils.Constants
import com.yudhakautsar.storyapp.utils.gone
import com.yudhakautsar.storyapp.utils.loadImage
import com.yudhakautsar.storyapp.utils.showToast
import com.yudhakautsar.storyapp.utils.visible

class StoryDetailActivity : BaseActivity<ActivityStoryDetailBinding>() {

    private val viewModel: StoryDetailViewModel by viewModels {
        val appContainer = (application as StoryApplication).appContainer
        StoryDetailViewModelFactory(appContainer.getStoryDetailUseCase, appContainer.userPreference)
    }

    override fun getViewBinding(): ActivityStoryDetailBinding {
        return ActivityStoryDetailBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        supportActionBar?.apply {
            title = "Detail Story"
            setDisplayHomeAsUpEnabled(true)
        }

        val storyId = intent.getStringExtra(Constants.EXTRA_STORY_ID)
        val storyParcelable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.EXTRA_STORY_DATA, Story::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.EXTRA_STORY_DATA)
        }

        storyParcelable?.let { bindStory(it) }

        storyId?.let {
            viewModel.getStoryDetail(it)
        }
    }

    override fun setupObservers() {
        viewModel.storyDetailState.observe(this) { state ->
            when (state) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    bindStory(state.data)
                }
                is ViewState.Error -> {
                    hideLoading()
                    showToast(state.message)
                }
                else -> hideLoading()
            }
        }
    }

    private fun bindStory(story: Story) {
        binding.apply {
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
            ivDetailPhoto.loadImage(story.photoUrl)
        }
    }

    override fun showLoading() {
        binding.apply {
            progressBar.visible()
        }
    }

    override fun hideLoading() {
        binding.apply {
            progressBar.gone()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
