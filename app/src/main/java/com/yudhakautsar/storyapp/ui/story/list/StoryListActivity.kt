package com.yudhakautsar.storyapp.ui.story.list

import android.content.Intent
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
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
            val intent = Intent(this, StoryDetailActivity::class.java).apply {
                putExtra(Constants.EXTRA_STORY_ID, story.id)
            }
            startActivity(intent)
        }
    }

    private val launcherAddStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.getStories()
        }
    }

    override fun getViewBinding(): ActivityStoryListBinding {
        return ActivityStoryListBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        binding.apply {
            rvStories.adapter = adapter
            supportActionBar?.title = getString(R.string.title_story_list)
        }
    }

    override fun setupListeners() {
        binding.apply {
            fabAdd.setOnClickListener {
                val intent = Intent(this@StoryListActivity, AddStoryActivity::class.java)
                launcherAddStory.launch(intent)
            }
        }
    }

    override fun setupObservers() {
        viewModel.storiesState.observe(this) { state ->
            when (state) {
                is ViewState.Loading -> showLoading()
                is ViewState.Success -> {
                    hideLoading()
                    adapter.submitList(state.data) {
                        binding.apply {
                            rvStories.scrollToPosition(0)
                        }
                    }
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
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
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
        binding.apply {
            progressBar.visible()
        }
    }

    override fun hideLoading() {
        binding.apply {
            progressBar.gone()
        }
    }
}
