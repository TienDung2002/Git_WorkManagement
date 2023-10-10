package com.example.workmanagement.Screens

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.workmanagement.MyContentProvider
import com.example.workmanagement.MyOpenHelper
import com.example.workmanagement.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SecondActivity : AppCompatActivity() {
    private lateinit var ETName: EditText
    private lateinit var datePicker: DatePicker
    private lateinit var saveBtn: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        ETName = findViewById(R.id.ET_input)
        datePicker = findViewById(R.id.datePicker)
        saveBtn = findViewById(R.id.btn_save)

        saveBtn.setOnClickListener {
            saveWork()
        }
    }

    private fun getDate(datePicker: DatePicker): String {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val calendar = Calendar.getInstance()

        calendar.set(year, month, day)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun saveWork() {
        val workName = ETName.text.toString()
        val selectedDate = getDate(datePicker)
        Log.d("test", "code đã chạy vào đây 1")

        if (workName.isEmpty()) {
            Toast.makeText(this,"Không bỏ trống nội dung công việc", Toast.LENGTH_SHORT).show()
            Log.d("test", "code đã chạy vào đây 2")
            return
        }

        Log.d("test", "code đã chạy vào đây 3")
        val values = ContentValues()
        values.put(MyOpenHelper.COLUMN_NAME, workName)
        values.put(MyOpenHelper.COLUMN_TIME, selectedDate)

        val uri = contentResolver.insert(MyContentProvider.CONTENT_URI, values)
        Log.d("test", "code đã chạy vào đây 4")

        if (uri == null) {
            Toast.makeText(this, "Lưu thất bại, có lỗi xảy ra!", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "Lưu thành công!", Toast.LENGTH_SHORT).show()
        // kill activity trở lại màn hình 1
        finish()
    }
}