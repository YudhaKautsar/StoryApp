package com.yudhakautsar.storyapp.ui.story.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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
        binding.apply {
            btnGallery.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCamera() }
            buttonAdd.setOnClickListener { uploadImage() }
        }
    }

    override fun setupObservers() {
        viewModel.uploadState.observe(this) { state ->
            when (state) {
                is ViewState.Loading -> showLoading()
                is ViewState.Success -> {
                    hideLoading()
                    showToast(state.data)
                    setResult(RESULT_OK)
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
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        } else {
            Toast.makeText(this, "Izin kamera tidak diberikan.", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            myFile?.let { file ->
                getFile = file
                binding.apply {
                    ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
                }
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.apply {
                    ivPreview.setImageURI(uri)
                }
            }
        }
    }

    private fun uploadImage() {
        binding.apply {
            val description = edAddDescription.text.toString().trim()
            if (getFile != null && description.isNotEmpty()) {
                val file = reduceFileImage(getFile as File)
                viewModel.uploadStory(file, description)
            } else {
                showToast("Please select an image and enter a description")
            }
        }
    }

    override fun showLoading() {
        binding.apply {
            buttonAdd.setLoading(true)
        }
    }

    override fun hideLoading() {
        binding.apply {
            buttonAdd.setLoading(false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val CAMERA_X_RESULT = 200
    }
}
