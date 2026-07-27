package com.example.vocabularyengapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyengapplication.adapter.CategoryAdapter
import com.example.vocabularyengapplication.databinding.ActivityDashboardBinding
import com.example.vocabularyengapplication.db.SqlDBHandler
import com.example.vocabularyengapplication.model.WordCategory

class DashboardActivity : AppCompatActivity() {
    private lateinit var bindingDashboard: ActivityDashboardBinding
    private lateinit var adapterCategory: CategoryAdapter
    private var selectedCategory: WordCategory = WordCategory.ALL_CATEGORIES
    private val dbHandler: SqlDBHandler = SqlDBHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingDashboard = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(bindingDashboard.root)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userName = sharedPreferences.getString("USER_NAME", null)
        if (userName != null) {
            bindingDashboard.tvGreeting.text = getString(R.string.txt_greeting, userName)
        }

        setCategoryList()
    }

    fun setCategoryList() {
        val categoryList = WordCategory.values().toList()
        adapterCategory = CategoryAdapter(categoryList, selectedCategory) { wordCategory ->
            selectedCategory = wordCategory
            refreshListCategoryAndVocab(wordCategory)
        }

        bindingDashboard.rvCategory.apply {
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = adapterCategory
        }
    }

    private fun refreshListCategoryAndVocab(wordCategory: WordCategory) {
        val llistWord = if (wordCategory == WordCategory.ALL_CATEGORIES) {
            dbHandler.getVocab()
        } else {
            dbHandler.getVocab().filter { it.category == wordCategory }
        }

        adapterCategory.updateSelectedCategory(selectedCategory)
    }
}