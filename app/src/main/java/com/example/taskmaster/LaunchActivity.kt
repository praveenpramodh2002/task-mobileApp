package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // The layout of the launching screen

        val getStartButton = findViewById<Button>(R.id.getstart)

        getStartButton.setOnClickListener {
            // Navigate to MainActivity when the button is clicked
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }
}
