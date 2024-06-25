package com.example.tales.ui.story

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.tales.Injector
import com.example.tales.MainActivity
import com.example.tales.databinding.ActivityCreateStoryBinding
import com.example.tales.viewmodel.StoryViewModel
import com.example.tales.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class CreateStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var photoFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyViewModel = ViewModelProvider(this, ViewModelFactory(Injector.provideStoryRepository(this))).get(StoryViewModel::class.java)

        binding.buttonPickPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(intent)
        }

        binding.buttonCapturePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            } else {
                launchCamera()
            }
        }

        binding.buttonAdd.setOnClickListener {
            val description = binding.edAddDescription.text.toString()
            if (::photoFile.isInitialized) {
                val photoRequestBody = photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val photoMultipart = MultipartBody.Part.createFormData("photo", photoFile.name, photoRequestBody)
                val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
                storyViewModel.addStory(descriptionBody, photoMultipart, null, null)
            } else {
                Toast.makeText(this, "Please select a photo first", Toast.LENGTH_SHORT).show()
            }
        }

        storyViewModel.addStoryResult.observe(this) { result ->
            if (result.isSuccess) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, result.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                binding.ivPreview.setImageURI(selectedImageUri)
                photoFile = createTempFileFromUri(selectedImageUri)
            }
        }
    }

    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as Bitmap
            binding.ivPreview.setImageBitmap(bitmap)
            photoFile = createTempFileFromBitmap(bitmap)
        }
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResultLauncher.launch(intent)
    }

    private fun createTempFileFromUri(uri: Uri): File {
        val tempFile = File.createTempFile("temp_image", ".jpg", cacheDir)
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return tempFile
    }

    private fun createTempFileFromBitmap(bitmap: Bitmap): File {
        val tempFile = File.createTempFile("temp_image", ".jpg", cacheDir)
        val outputStream = FileOutputStream(tempFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
        return tempFile
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1001
    }
}
