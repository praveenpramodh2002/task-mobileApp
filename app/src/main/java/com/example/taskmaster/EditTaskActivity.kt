package com.example.taskmaster

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class EditTaskActivity : AppCompatActivity() {
    private var selectedDate: String? = null
    private var selectedTime: String? = null
    private lateinit var taskDateTextView: TextView
    private lateinit var taskTimeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        val taskIndex = intent.getIntExtra("taskIndex", -1)
        val taskDetails = intent.getStringExtra("task") ?: ""

        // Initialize UI elements
        val editTaskEditText = findViewById<EditText>(R.id.editTaskEditText)
        taskDateTextView = findViewById(R.id.taskDateTextView)
        taskTimeTextView = findViewById(R.id.taskTimeTextView)
        val selectDateButton = findViewById<Button>(R.id.selectDateButton)
        val selectTimeButton = findViewById<Button>(R.id.selectTimeButton)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val deleteButton = findViewById<Button>(R.id.deleteButton)

        // Set the initial task details in the EditText
        editTaskEditText.setText(taskDetails)

        // Set initial date and time if available
        val parts = taskDetails.split(" - Due on ", " at ")
        if (parts.size == 3) {
            selectedDate = parts[1]
            selectedTime = parts[2]
            taskDateTextView.text = "Current Date: $selectedDate"
            taskTimeTextView.text = "Current Time: $selectedTime"
        }

        // Set up listeners for date and time selection
        selectDateButton.setOnClickListener { showDatePickerDialog() }
        selectTimeButton.setOnClickListener { showTimePickerDialog() }

        // Save button functionality
        saveButton.setOnClickListener {
            val updatedTaskDescription = editTaskEditText.text.toString()
            val updatedTask = "$updatedTaskDescription - Due on ${selectedDate ?: "Not Set"} at ${selectedTime ?: "Not Set"}"

            if (updatedTaskDescription.isNotBlank()) {
                val resultIntent = Intent().apply {
                    putExtra("updatedTask", updatedTask)
                    putExtra("taskIndex", taskIndex)
                }
                setResult(RESULT_OK, resultIntent)
            } else {
                setResult(RESULT_CANCELED)
            }
            finish() // Close activity
        }

        // Delete button functionality
        deleteButton.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("deleteTask", true)
                putExtra("taskIndex", taskIndex)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            selectedDate = "$day/${month + 1}/$year"
            taskDateTextView.text = "Selected Date: $selectedDate"
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            selectedTime = String.format("%02d:%02d", hour, minute)
            taskTimeTextView.text = "Selected Time: $selectedTime"
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }
}
