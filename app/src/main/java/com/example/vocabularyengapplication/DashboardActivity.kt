package com.example.vocabularyengapplication

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyengapplication.adapter.CategoryAdapter
import com.example.vocabularyengapplication.adapter.VocabAdapter
import com.example.vocabularyengapplication.databinding.ActivityDashboardBinding
import com.example.vocabularyengapplication.db.SqlDBHandler
import com.example.vocabularyengapplication.model.ListWordState
import com.example.vocabularyengapplication.model.WordCategory

class DashboardActivity : AppCompatActivity() {
    private lateinit var bindingDashboard: ActivityDashboardBinding
    private lateinit var adapterCategory: CategoryAdapter
    private lateinit var adapterVocab: VocabAdapter
    private var selectedListState: ListWordState = ListWordState.NORMAL
    private var selectedCategory: WordCategory = WordCategory.ALL_CATEGORIES
    private val dbHandler: SqlDBHandler = SqlDBHandler(this)
    private var progress = 0
    private var maxVocab = 100

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

        btnDeleteAdd()
        setProgressAndRefresh()
        setCategoryList()
        setVocabList()

        // button for navigate
        bindingDashboard.ivAdd.setOnClickListener {
            navigateToNewVocab()
        }

        // button delete
        bindingDashboard.ivDelete.setOnClickListener {
            selectedListState = ListWordState.REMOVE
            adapterVocab.setListState(selectedListState)
            buttonCancel()
        }

        // button cancel
        bindingDashboard.btnCancel.setOnClickListener {
            /*Jika terjadi perubahan state pada sebuah event handler,
            perbarui nilai state terlebih dahulu sebelum memanggil
            fungsi pembaharu UI.
             * */
            selectedListState = ListWordState.NORMAL
            adapterVocab.setListState(selectedListState)
            btnDeleteAdd()
        }
    }

    fun setCategoryList() {
        val categoryList = WordCategory.values().toList()
        adapterCategory =
            CategoryAdapter(categoryList, selectedCategory) { wordCategory ->
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
        val listWord =
            if (wordCategory == WordCategory.ALL_CATEGORIES) {
                dbHandler.getVocab()
            } else {
                dbHandler.getVocab().filter { it.category == wordCategory }
            }
        // update list
        adapterVocab.refreshList(listWord)
        adapterCategory.updateSelectedCategory(selectedCategory)
    }

    // navigate to add new vocab
    private fun navigateToNewVocab() {
        val intent = Intent(this, AddActivity::class.java)
        startActivityForResult(intent, 200)
    }

    // set value adapter vocab
    private fun setVocabList() {
        adapterVocab =
            VocabAdapter(dbHandler.getVocab(), selectedListState) { positionToBeRemove ->
                // remove and refresh function
                refreshAndRemove(positionToBeRemove)
            }

        bindingDashboard.rvVocab.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = adapterVocab
        }
    }

    private fun refreshAndRemove(position: Int) {
        dbHandler.deleteVocab(position)
        adapterVocab.refreshList(dbHandler.getVocab())

        // logic for visible button
        if (dbHandler.getVocab().isNotEmpty()) {
            buttonCancel()
        } else {
            btnDeleteAdd()
        }

        setProgressAndRefresh()
    }

    private fun buttonCancel() {
        bindingDashboard.btnCancel.isVisible = true
        bindingDashboard.ivDelete.isVisible = false
        bindingDashboard.ivAdd.isVisible = false
        setProgressAndRefresh()
    }

    private fun btnDeleteAdd() {
        bindingDashboard.btnCancel.isVisible = false
        bindingDashboard.ivAdd.isVisible = true
        bindingDashboard.ivDelete.isVisible = dbHandler.getVocab().isNotEmpty()
        setProgressAndRefresh()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            adapterVocab.refreshList(dbHandler.getVocab())
            btnDeleteAdd()
        }
    }

    private fun setProgressAndRefresh() {
        progress = (dbHandler.getVocab().size * 100 / maxVocab)
        bindingDashboard.tvTitleVocabAvailableValue.text = getString(R.string.txt_available_value, progress)
        bindingDashboard.tvAchieved.text = "$progress %"
        bindingDashboard.pbAchieved.progress = progress
        bindingDashboard.ivAdd.isVisible = progress < 100 && selectedListState == ListWordState.NORMAL
    }
}
