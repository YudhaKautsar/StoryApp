package com.yudhakautsar.storyapp.ui.story

import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.yudhakautsar.storyapp.StoryApplication
import com.yudhakautsar.storyapp.base.BaseActivity
import com.yudhakautsar.storyapp.base.ViewState
import com.yudhakautsar.storyapp.databinding.ActivityAddStoryBinding
import com.yudhakautsar.storyapp.utils.reduceFileImage
import com.yudhakautsar.storyapp.utils.showToast
import com.yudhakautsar.storyapp.utils.uriToFile
import java.io.File

class AddStoryActivity : BaseActivity<ActivityAddStoryBinding>() {

    private val viewModel: AddStoryViewModel by viewModels {
        val appContainer = (application as StoryApplication).appContainer
        AddStoryViewModelFactory(appContainer.addStoryUseCase, appContainer.userPreference)
    }

    private var getFile: File? = null

    override fun getViewBinding(): ActivityAddStoryBinding {
        return ActivityAddStoryBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        supportActionBar?.apply {
            title = "Add Story"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun setupListeners() {
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
    }

    override fun setupObservers() {
        viewModel.uploadState.observe(this) { state ->
            when (state) {
                is ViewState.Loading -> showLoading()
                is ViewState.Success -> {
                    hideLoading()
                    showToast(state.data)
                    finish()
                }
                is ViewState.Error -> {
                    hideLoading()
                    showToast(state.message)
                }
                else -> hideLoading()
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera() {
        // Implement camera if needed, for now focusing on gallery requirement
        showToast("Camera feature coming soon")
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.ivPreview.setImageURI(uri)
            }
        }
    }

    private fun uploadImage() {
        val description = binding.edAddDescription.text.toString().trim()
        if (getFile != null && description.isNotEmpty()) {
            val file = reduceFileImage(getFile as File)
            viewModel.uploadStory(file, description)
        } else {
            showToast("Please select an image and enter a description")
        }
    }

    override fun showLoading() {
        binding.buttonAdd.setLoading(true)
    }

    override fun hideLoading() {
        binding.buttonAdd.setLoading(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
