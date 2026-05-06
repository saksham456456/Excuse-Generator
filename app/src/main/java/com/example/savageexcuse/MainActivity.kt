package com.example.savageexcuse

import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.savageexcuse.data.Excuse
import com.example.savageexcuse.databinding.ActivityMainBinding
import com.example.savageexcuse.databinding.BottomSheetFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ExcusePagerAdapter
    private var isFirstLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupListeners()
        observeViewModel()
    }

    private fun setupViewPager() {
        adapter = ExcusePagerAdapter(
            onFavoriteClick = { excuse -> viewModel.toggleFavorite(excuse) },
            onShareClick = { excuse -> /* Handled globally for current item */ }
        )
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (!isFirstLoad) {
                    performHapticFeedback()
                }
                isFirstLoad = false

                // Hide helper text after first swipe
                if (position > 0) {
                    binding.tvSwipeHelper.visibility = View.GONE
                }
            }
        })
    }

    private fun performHapticFeedback() {
        val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(50)
        }
    }

    private fun setupListeners() {
        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnFilter.setOnClickListener {
            showFilterBottomSheet()
        }
        
        binding.btnShareImage.setOnClickListener {
            shareCurrentCard()
        }
        
        binding.btnMyStash.setOnClickListener {
            startActivity(Intent(this, MyStashActivity::class.java))
        }
    }

    private fun shareCurrentCard() {
        val currentItemPos = binding.viewPager.currentItem
        val excuse = adapter.currentList.getOrNull(currentItemPos) ?: return
        
        // Find the visible view inside the ViewPager
        val recyclerView = binding.viewPager.getChildAt(0) as? androidx.recyclerview.widget.RecyclerView
        val viewHolder = recyclerView?.findViewHolderForAdapterPosition(currentItemPos)
        val cardView = viewHolder?.itemView?.findViewById<View>(R.id.cardView)
        
        if (cardView != null) {
            ShareUtils.shareViewAsImage(this, cardView, excuse.text)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            val userPreferencesRepository = com.example.savageexcuse.data.UserPreferencesRepository(this@MainActivity)
            val nameFlow = userPreferencesRepository.userName

            nameFlow.collectLatest { userName ->
                viewModel.excuses.collectLatest { excuses ->
                    // Inject user's name dynamically
                    val injectedExcuses = if (!userName.isNullOrEmpty()) {
                        excuses.map {
                            it.copy(text = it.text.replace("[Name]", userName, ignoreCase = true))
                        }
                    } else {
                        excuses
                    }
                    adapter.submitList(injectedExcuses)
                }
            }
        }
    }

    private fun showFilterBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val filterBinding = BottomSheetFilterBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(filterBinding.root)

        val categories = listOf("School", "Work", "Relationships", "Savage", "Tech Issues", "Introvert Problems", "Strict Boss", "Clingy Ex", "Toxic Savage")

        categories.forEach { category ->
            val chip = Chip(this)
            chip.text = category
            chip.isCheckable = true
            chip.chipBackgroundColor = getColorStateList(R.color.btn_secondary_bg)
            chip.setTextColor(getColor(R.color.white))
            filterBinding.cgCategories.addView(chip)
        }

        filterBinding.btnApply.setOnClickListener {
            val selectedIds = filterBinding.cgCategories.checkedChipIds
            val selectedCategories = selectedIds.map { id ->
                filterBinding.cgCategories.findViewById<Chip>(id).text.toString()
            }
            viewModel.setFilterCategories(selectedCategories)
            bottomSheetDialog.dismiss()
        }

        filterBinding.tvClear.setOnClickListener {
            filterBinding.cgCategories.clearCheck()
        }

        bottomSheetDialog.show()
    }
}
