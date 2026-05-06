package com.example.savageexcuse

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.savageexcuse.data.UserPreferencesRepository
import com.example.savageexcuse.databinding.ActivityOnboardingBinding
import kotlinx.coroutines.launch

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferencesRepository = UserPreferencesRepository(this)

        binding.btnContinue.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val persona = when (binding.rgPersona.checkedRadioButtonId) {
                R.id.rbStudent -> "Student"
                R.id.rbProfessional -> "Professional"
                R.id.rbIntrovert -> "Introvert"
                else -> "General"
            }

            lifecycleScope.launch {
                userPreferencesRepository.completeOnboarding(name, persona)
                startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}
