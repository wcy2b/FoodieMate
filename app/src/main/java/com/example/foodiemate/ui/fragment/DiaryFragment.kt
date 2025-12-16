package com.example.foodiemate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiemate.R
import com.example.foodiemate.data.entity.Diary
import com.example.foodiemate.ui.adapter.DiaryAdapter
import com.example.foodiemate.viewmodel.DiaryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiaryFragment : Fragment() {

    private val diaryViewModel: DiaryViewModel by viewModels()
    private lateinit var adapter: DiaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_diary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
        setupFab(view)
        
        // Update Title if possible (or keep it static in activity)
        // (activity as? AppCompatActivity)?.supportActionBar?.title = "美食日记"
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvDiary)
        adapter = DiaryAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        diaryViewModel.allDiaries.observe(viewLifecycleOwner) { diaries ->
            adapter.submitList(diaries)
        }
    }

    private fun setupFab(view: View) {
        view.findViewById<FloatingActionButton>(R.id.fabAddDiary).setOnClickListener {
            insertTestDiary()
        }
    }

    private fun insertTestDiary() {
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val diary = Diary(
            foodName = "测试美食 ${System.currentTimeMillis() % 1000}",
            content = "这是一条测试日记，记录了今天吃的好吃的。时间：$currentDate",
            date = currentDate
        )
        diaryViewModel.insert(diary)
        Toast.makeText(requireContext(), "已添加测试数据", Toast.LENGTH_SHORT).show()
    }
}

