package com.example.vocabularyengapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vocabularyengapplication.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var bindingAddActivity: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingAddActivity = ActivityAddBinding.inflate(layoutInflater)
        setContentView(bindingAddActivity.root)
    }
}