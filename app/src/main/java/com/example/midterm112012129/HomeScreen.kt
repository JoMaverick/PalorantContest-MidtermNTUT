package com.example.midterm112012129

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val btn = findViewById<Button>(R.id.buttonHome)

        btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}