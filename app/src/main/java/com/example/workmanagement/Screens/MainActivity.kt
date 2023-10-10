package com.example.workmanagement.Screens

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workmanagement.MyAdapter
import com.example.workmanagement.MyContentProvider
import com.example.workmanagement.MyOpenHelper
import com.example.workmanagement.R
import com.example.workmanagement.WorkModel.Work
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var addBtn : FloatingActionButton
    private lateinit var recyclerView : RecyclerView
    private lateinit var myAdapter : MyAdapter
    private val List = mutableListOf<Work>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ánh xạ
        addBtn = findViewById(R.id.btn_add)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // thiết lập recycler
        recyclerView.layoutManager = LinearLayoutManager(this)
        myAdapter = MyAdapter(List)
        recyclerView.adapter = myAdapter

        // Hiển thị full danh sách
        DataLoaded()

        addBtn.setOnClickListener{
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    // Xử lí khi add xong work và quay về màn hình 1
    override fun onResume() {
        super.onResume()
        // gọi lại hàm để cập nhật danh sách
        DataLoaded()
    }

    private fun DataLoaded() {
        val uri = MyContentProvider.CONTENT_URI

        // Các cột muốn truy vấn
        val projection = arrayOf(
            MyOpenHelper.COLUMN_ID,
            MyOpenHelper.COLUMN_NAME,
            MyOpenHelper.COLUMN_TIME
        )

        // Dùng resolver để truy vấn vào MyContentProvider
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)

        cursor?.apply {
            // clear data cũ để làm mới
            List.clear()
            while (moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(MyOpenHelper.COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(MyOpenHelper.COLUMN_NAME))
                val time = cursor.getString(cursor.getColumnIndexOrThrow(MyOpenHelper.COLUMN_TIME))

                val work = Work(id, name, time)
                List.add(work)
            }
            myAdapter.notifyDataSetChanged()
            // xóa cursor để tối ưu bộ nhớ
            close()
        }
    }
}