package com.example.vocabularyengapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.vocabularyengapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding
    val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        //Perlu dicek karna hasil uji coba
        val savedUserNamed = sharedPreferences.getString("USER_NAME", null)
        if (savedUserNamed != null){
            //direct to main page
        } else{
            showOnBoard()
        }
    }

    private fun showOnBoard(){
        bindingMain.etNameOnboarding.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                bindingMain.tvTitle.text = getString(R.string.txt_title_default)
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                bindingMain.tvTitle.text = getString(R.string.txt_title_replaced)
            }

            override fun afterTextChanged(s: Editable?) {
                bindingMain.tvTitle.text = getString(R.string.txt_title_replaced)
                bindingMain.btnStart.isVisible = !s.isNullOrEmpty()
            }
        })
        bindingMain.btnStart.setOnClickListener {
            val userName = bindingMain.etNameOnboarding.text.toString()
            saveName(userName)
        }
    }

    private fun saveName(userName: String){
        val editor = sharedPreferences.edit()
        editor.putString("USER_NAME", userName)
        editor.apply()
    }
}