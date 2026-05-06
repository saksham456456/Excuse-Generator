package com.example.savageexcuse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.savageexcuse.data.UserPreferencesRepository
import com.example.savageexcuse.databinding.ActivityProfileBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val uri = result.data?.data
            if (uri != null) {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                selectedImageUri = uri
                binding.ivAvatar.setImageURI(selectedImageUri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferencesRepository = UserPreferencesRepository(this)

        binding.btnBack.setOnClickListener { finish() }

        binding.ivAvatar.setOnClickListener { openGallery() }
        binding.tvChangeAvatar.setOnClickListener { openGallery() }

        binding.btnSaveProfile.setOnClickListener { saveProfile() }

        loadProfileData()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun loadProfileData() {
        lifecycleScope.launch {
            val name = userPreferencesRepository.userName.first() ?: ""
            val username = userPreferencesRepository.userUsername.first() ?: ""
            val avatarUriString = userPreferencesRepository.userAvatarUri.first()

            binding.etName.setText(name)
            binding.etUsername.setText(username)

            if (!avatarUriString.isNullOrEmpty()) {
                try {
                    val uri = Uri.parse(avatarUriString)
                    selectedImageUri = uri
                    binding.ivAvatar.setImageURI(uri)
                } catch (e: Exception) {
                    // Ignore, fallback to default
                }
            }
        }
    }

    private fun saveProfile() {
        val name = binding.etName.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            userPreferencesRepository.updateProfile(name, username, selectedImageUri?.toString())
            Toast.makeText(this@ProfileActivity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
