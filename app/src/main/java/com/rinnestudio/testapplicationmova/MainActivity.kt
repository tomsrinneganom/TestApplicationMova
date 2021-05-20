package com.rinnestudio.testapplicationmova

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm

class MainActivity : AppCompatActivity() {

    private val viewModel: MyViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyRecyclerViewAdapter
    private var enterIsPressed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        Realm.init(this)

        initRecyclerView()

        initEditText()

        viewModel.readData().observe(this) {
            adapter.setItemList(it)
        }
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        adapter = MyRecyclerViewAdapter(arrayListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun initEditText() {
        val editText = findViewById<EditText>(R.id.searchEditText)
        editText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action != KeyEvent.ACTION_DOWN && !enterIsPressed) {
                enterIsPressed = true
                viewModel.getItem(editText.text.toString()).observe(this) {
                    enterIsPressed = false
                    if (it != null) {
                        adapter.addItem(it)

                    } else
                        Toast.makeText(
                            this,
                            "Не удалось найти фотографию",
                            Toast.LENGTH_LONG
                        ).show()
                }
            }

            true
        }

    }
}