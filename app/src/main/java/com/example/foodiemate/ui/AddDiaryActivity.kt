package com.example.foodiemate.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodiemate.R
import com.example.foodiemate.data.entity.Diary
import com.example.foodiemate.viewmodel.DiaryViewModel
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddDiaryActivity : AppCompatActivity() {

    private val diaryViewModel: DiaryViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private var existingDiary: Diary? = null
    
    private lateinit var ivSelectedImage: ImageView
    private lateinit var tvAddImageHint: TextView
    private lateinit var etFoodName: EditText
    private lateinit var etContent: EditText

    companion object {
        const val EXTRA_DIARY = "extra_diary"
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            ivSelectedImage.setImageURI(it)
            tvAddImageHint.visibility = View.GONE
            
            try {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary)

        existingDiary = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_DIARY, Diary::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DIARY)
        }

        supportActionBar?.title = if (existingDiary != null) "编辑日记" else "写日记"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        
        existingDiary?.let { diary ->
            populateFields(diary)
        }
    }

    private fun initViews() {
        etFoodName = findViewById(R.id.etFoodName)
        etContent = findViewById(R.id.etContent)
        val flImageContainer = findViewById<FrameLayout>(R.id.flImageContainer)
        ivSelectedImage = findViewById(R.id.ivSelectedImage)
        tvAddImageHint = findViewById(R.id.tvAddImageHint)
        val btnSave = findViewById<MaterialButton>(R.id.btnSave)

        flImageContainer.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnSave.setOnClickListener {
            val name = etFoodName.text.toString().trim()
            val content = etContent.text.toString().trim()

            if (name.isEmpty()) {
                etFoodName.error = "请输入美食名称"
                return@setOnClickListener
            }

            if (content.isEmpty()) {
                etContent.error = "请输入日记内容"
                return@setOnClickListener
            }

            saveDiary(name, content)
        }
    }

    private fun populateFields(diary: Diary) {
        etFoodName.setText(diary.foodName)
        etContent.setText(diary.content)
        
        if (!diary.imageUri.isNullOrEmpty()) {
            selectedImageUri = Uri.parse(diary.imageUri)
            try {
                ivSelectedImage.setImageURI(selectedImageUri)
                tvAddImageHint.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun saveDiary(name: String, content: String) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        
        val diary = Diary(
            id = existingDiary?.id ?: 0, // Keep ID if editing, else 0 for auto-gen
            foodName = name,
            content = content,
            date = currentDate, // Update date to modified time? Or keep original? Let's update.
            imageUri = selectedImageUri?.toString()
        )

        diaryViewModel.insert(diary) // insert with conflict strategy REPLACE will update
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
