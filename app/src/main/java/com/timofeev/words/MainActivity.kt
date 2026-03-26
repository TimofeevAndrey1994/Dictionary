package com.timofeev.words

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.timofeev.words.di.Creator
import com.timofeev.words.utils.Resource
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            Creator.provideGetWordMeaningUseCase(applicationContext).getWordMeaning("hello").collect { resource ->
                when(resource){
                    is Resource.Error -> {
                        println("ошибка!")
                    }
                    is Resource.Success -> {
                        println(resource.data)
                    }
                }
            }
        }

        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
        //    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //    insets
        //}
    }
}