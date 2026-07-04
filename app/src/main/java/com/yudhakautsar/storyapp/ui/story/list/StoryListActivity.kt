package com.yudhakautsar.storyapp.ui.story.list

import android.content.Intent
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.yudhakautsar.storyapp.R
import com.yudhakautsar.storyapp.StoryApplication
import com.yudhakautsar.storyapp.base.BaseActivity
import com.yudhakautsar.storyapp.base.ViewState
import com.yudhakautsar.storyapp.databinding.ActivityStoryListBinding
import com.yudhakautsar.storyapp.ui.auth.login.LoginActivity
import com.yudhakautsar.storyapp.ui.story.add.AddStoryActivity
import com.yudhakautsar.storyapp.ui.story.detail.StoryDetailActivity
import com.yudhakautsar.storyapp.utils.Constants
import com.yudhakautsar.storyapp.utils.gone
import com.yudhakautsar.storyapp.utils.showToast
import com.yudhakautsar.storyapp.utils.visible

class StoryListActivity : BaseActivity<ActivityStoryListBinding>() {

    private val viewModel: StoryListViewModel by viewModels {
        val appContainer = (application as StoryApplication).appContainer
        StoryListViewModelFactory(appContainer.getStoriesUseCase, appContainer.userPreference)
    }

    private val adapter: StoryAdapter by lazy {
        StoryAdapter { story ->
            val intent = Intent(this, StoryDetailActivity::class.java)
            intent.putExtra(Constants.EXTRA_STORY_DATA, story)
            startActivity(intent)
        }
    }

    override fun getViewBinding(): ActivityStoryListBinding {
        return ActivityStoryListBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        binding.rvStories.adapter = adapter
    }

    override fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logout()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }
            R.id.action_localization -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> super.onOptionsItemSelected(item)
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
