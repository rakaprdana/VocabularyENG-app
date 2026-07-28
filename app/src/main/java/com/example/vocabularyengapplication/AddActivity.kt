package com.example.vocabularyengapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vocabularyengapplication.databinding.ActivityAddBinding
import com.example.vocabularyengapplication.db.SqlDBHandler
import com.example.vocabularyengapplication.model.WordCategory

class AddActivity : AppCompatActivity() {
    private lateinit var bindingAddActivity: ActivityAddBinding
    private val sqlHandler: SqlDBHandler = SqlDBHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingAddActivity = ActivityAddBinding.inflate(layoutInflater)
        setContentView(bindingAddActivity.root)
        setSpinner()

        bindingAddActivity.btnSave.setOnClickListener {
            if (bindingAddActivity.etName.text.isNullOrEmpty() ||
                bindingAddActivity.etMeaning.text.isNullOrEmpty()||
                bindingAddActivity.spinnerCategory.selectedItem.toString().isEmpty()
                ) return@setOnClickListener

            sqlHandler.addVocab(
                bindingAddActivity.etName.text.toString(),
                bindingAddActivity.etMeaning.text.toString(),
                bindingAddActivity.spinnerCategory.selectedItem.toString()
            )
            setResult(200, Intent())
            finish()
        }

        bindingAddActivity.btnDiscard.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setSpinner(){
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, getCategoryList()
        )
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        bindingAddActivity.spinnerCategory.adapter = adapter
    }

    private fun getCategoryList(): List<String>{
        return WordCategory.values().map {
            if (it == WordCategory.ALL_CATEGORIES){
                ""
            } else{
                it.title
            }
        }
    }
}