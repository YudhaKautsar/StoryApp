package com.yudhakautsar.storyapp.ui.story

import android.os.Build
import com.bumptech.glide.Glide
import com.yudhakautsar.storyapp.base.BaseActivity
import com.yudhakautsar.storyapp.databinding.ActivityStoryDetailBinding
import com.yudhakautsar.storyapp.domain.model.Story
import com.yudhakautsar.storyapp.utils.Constants

class StoryDetailActivity : BaseActivity<ActivityStoryDetailBinding>() {

    override fun getViewBinding(): ActivityStoryDetailBinding {
        return ActivityStoryDetailBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        supportActionBar?.apply {
            title = "Detail Story"
            setDisplayHomeAsUpEnabled(true)
        }

        val story = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.EXTRA_STORY_DATA, Story::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.EXTRA_STORY_DATA)
        }

        story?.let { bindStory(it) }
    }

    private fun bindStory(story: Story) {
        binding.apply {
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
            Glide.with(this@StoryDetailActivity)
                .load(story.photoUrl)
                .into(ivDetailPhoto)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
