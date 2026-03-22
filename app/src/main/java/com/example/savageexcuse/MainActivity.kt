package com.example.savageexcuse

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.savageexcuse.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentCategory = 1 // 0: School, 1: Work, 2: Relationships, 3: Savage

    private val schoolExcuses = arrayOf(
        "“Dog ne charger kha liya yaar... ab kya hi kar sakte hain 😂”",
        "“Assignment complete tha but laptop ne aaj strike kar diya 😔✊”",
        "“Network issue tha, submit button hi gayab ho gaya”",
        "“Sir, group project mein main akela insaan tha, baaki sab aatma”",
        "“Pura din light nahi thi, phone dead tha aur dimaag bhi”"
    )

    private val workExcuses = arrayOf(
        "“Meeting ka link hi nahi mila, spam mein gaya hoga pakka”",
        "“Laptop update ho raha tha poori raat... 99% pe atak gaya 💀”",
        "“Wifi router ne dum tod diya, funeral mein busy tha”",
        "“Health issues... mental health count hoti hai na sir?”",
        "“Client call pe tha, isliye late ho gaya (Client = Mummy)”"
    )

    private val relationshipExcuses = arrayOf(
        "“Phone silent pe tha is a lifestyle bro, not a mistake”",
        "“So gaya tha, sapne mein tum hi thi... isliye uthne ka mann nahi kiya”",
        "“Battery 1% thi, phone switch off hone wala tha risk nahi liya”",
        "“Insta scroll karte karte waqt ka pata hi nahi chala, sorry 😌”",
        "“Message seen kiya par reply karna bhool gaya, typical me”"
    )

    private val savageExcuses = arrayOf(
        "“Main late nahi hoon, duniya jaldi aa gayi hai”",
        "“Mujhe yaad tha, bas importance nahi di 😌”",
        "“Busy nahi tha... mentally unavailable tha 😌”",
        "“Message seen kiya par reply nahi, suspense zaruri hai life mein”",
        "“Main apni favourite hoon, isliye khud ko time de raha tha”"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set initial category (Work as per UI image)
        updateCategorySelection(1)

        binding.btnSchool.setOnClickListener { updateCategorySelection(0) }
        binding.btnWork.setOnClickListener { updateCategorySelection(1) }
        binding.btnRelationships.setOnClickListener { updateCategorySelection(2) }
        binding.btnSavageMode.setOnClickListener { updateCategorySelection(3) }

        binding.btnGenerate.setOnClickListener {
            animateGenerateButton()
            generateExcuse()
        }

        binding.btnCopy.setOnClickListener {
            copyToClipboard()
        }

        binding.btnShare.setOnClickListener {
            shareExcuse()
        }
    }

    private fun updateCategorySelection(category: Int) {
        currentCategory = category
        
        val buttons = listOf(binding.btnSchool, binding.btnWork, binding.btnRelationships, binding.btnSavageMode)
        
        buttons.forEachIndexed { index, button ->
            if (index == category) {
                button.alpha = 1.0f
                button.scaleX = 1.05f
                button.scaleY = 1.05f
            } else {
                button.alpha = 0.5f
                button.scaleX = 1.0f
                button.scaleY = 1.0f
            }
        }
    }

    private fun generateExcuse() {
        val excuses = when (currentCategory) {
            0 -> schoolExcuses
            1 -> workExcuses
            2 -> relationshipExcuses
            3 -> savageExcuses
            else -> schoolExcuses
        }
        
        val randomExcuse = excuses[Random.nextInt(excuses.size)]
        
        // Simple text transition animation
        binding.tvExcuse.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction {
                binding.tvExcuse.text = randomExcuse
                binding.tvExcuse.animate()
                    .alpha(1f)
                    .setDuration(150)
                    .start()
            }
            .start()
    }

    private fun animateGenerateButton() {
        binding.btnGenerate.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                binding.btnGenerate.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun copyToClipboard() {
        val text = binding.tvExcuse.text.toString()
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("savage_excuse", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied! 😎", Toast.LENGTH_SHORT).show()
    }

    private fun shareExcuse() {
        val text = binding.tvExcuse.text.toString()
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}
