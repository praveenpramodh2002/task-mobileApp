package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class TaskListActivity : AppCompatActivity() {
    private val taskList = mutableListOf<String>()
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        listView = findViewById(R.id.listView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList)
        listView.adapter = adapter

        findViewById<Button>(R.id.addButton).setOnClickListener {
            val intent = Intent(this, EditTaskActivity::class.java)
            startActivityForResult(intent, 1001)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val updatedTask = data?.getStringExtra("updatedTask")
            val taskIndex = data?.getIntExtra("taskIndex", -1)

            if (!updatedTask.isNullOrEmpty() && updatedTask.trim().isNotEmpty() && !taskList.contains(updatedTask)) {
                taskList.add(updatedTask)
                adapter.notifyDataSetChanged()
            }
        }
    }
}