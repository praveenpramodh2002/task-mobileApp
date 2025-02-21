package com.example.taskmaster

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private var tasks: ArrayList<String>? = null
    private var tasksAdapter: ArrayAdapter<String>? = null
    private var listView: ListView? = null
    private var sharedPreferences: SharedPreferences? = null
    private var selectedDate: String? = null
    private var selectedTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        listView = findViewById(R.id.listView)
        val addButton = findViewById<Button>(R.id.addButton)
        val selectDateButton = findViewById<Button>(R.id.selectDateButton)
        val selectTimeButton = findViewById<Button>(R.id.selectTimeButton)

        sharedPreferences = getSharedPreferences("TaskMasterPrefs", MODE_PRIVATE)
        tasks = ArrayList()
        loadTasks()

        tasksAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks!!)
        listView?.adapter = tasksAdapter

        addButton.setOnClickListener {
            val newTaskEditText = findViewById<EditText>(R.id.newTaskEditText)
            val task = newTaskEditText.text.toString()
            if (task.isNotEmpty() && selectedDate != null && selectedTime != null) {
                tasks!!.add("$task - Due on $selectedDate at $selectedTime")
                tasksAdapter!!.notifyDataSetChanged()
                saveTasks()
                newTaskEditText.setText("")

                selectedDate = null // Reset after adding task
                selectedTime = null // Reset after adding task
            }
        }

        selectDateButton.setOnClickListener { showDatePickerDialog() }
        selectTimeButton.setOnClickListener { showTimePickerDialog() }

        listView?.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this@MainActivity, EditTaskActivity::class.java)
            intent.putExtra("taskIndex", i)
            intent.putExtra("task", tasks!![i])
            startActivityForResult(intent, 1)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            selectedDate = "$day/${month + 1}/$year" // Format date as needed
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            selectedTime = String.format("%02d:%02d", hour, minute) // Format time as needed
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    private fun loadTasks() {
        val TASKS_KEY = "tasks_key"
        val taskSet = sharedPreferences!!.getStringSet(TASKS_KEY, HashSet())
        tasks!!.addAll(taskSet!!)
    }

    private fun saveTasks() {
        val TASKS_KEY = "tasks_key"
        sharedPreferences!!.edit().putStringSet(TASKS_KEY, HashSet(tasks)).apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val index = data!!.getIntExtra("taskIndex", -1)
            val updatedTask = data.getStringExtra("updatedTask")
            val isDelete = data.getBooleanExtra("deleteTask", false)

            if (isDelete && index >= 0) {
                tasks!!.removeAt(index)
            } else if (updatedTask != null && index >= 0) {
                tasks!![index] = updatedTask
            }

            tasksAdapter!!.notifyDataSetChanged()
            saveTasks()
        }
    }
}
