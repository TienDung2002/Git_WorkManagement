package com.example.workmanagement

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import android.util.Log

class MyContentProvider : ContentProvider() {
    private lateinit var dbHelper: MyOpenHelper

    companion object {
        private const val AUTHORITY = "com.example.workmanagement.MyContentProvider"
        private const val WORKS_PATH = "works"
        private const val WORK = 1
        private const val WORK_ID = 2

        // URI (Uniform Resource Identifier)
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$WORKS_PATH")

        // add thêm quy tắc vào uri
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            // trả về full danh sách
            uriMatcher.addURI(AUTHORITY, WORKS_PATH, WORK)
            // trả về công việc dựa vào id
            uriMatcher.addURI(AUTHORITY, "$WORKS_PATH/#", WORK_ID)
        }
    }

    // Khởi tạo db
    override fun onCreate(): Boolean {
        dbHelper = MyOpenHelper(context!!)
        return true
    }

    // Câu lệnh truy vấn
    override fun query(
        uri: Uri,
        // mảng tên các cột
        projection: Array<out String>?,
        // điều kiện Where
        selection: String?,
        selectionArgs: Array<out String>?,
        // sắp xếp KQ
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase
        val match = uriMatcher.match(uri)
        return when (match) {
            // uri math full danh sách
            WORK -> {
                val cursor = db.query(
                    MyOpenHelper.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
                // đặt thông báo
                cursor.setNotificationUri(context?.contentResolver, uri)
                // trả về kq truy vấn
                cursor
            }
            // uri math theo WORK_ID cụ thể
            WORK_ID -> {
                // Lấy phần cuối của Uri: ví dụ "content://com.example.workmanagement.MyContentProvider/works/2" thì id là 2
                val id = uri.lastPathSegment
                // Tạo dk truy vấn sử dụng "?" như placeholder để thay thế ID sau này trong truy vấn
                val selectionById = "${MyOpenHelper.COLUMN_ID} = ?"
                // Tạo 1 mảng chứa ID lấy từ lastPathSegment, mảng này để thay thế "?" ở selectionById
                val selectionArgsById = arrayOf(id)
                val cursor = db.query(
                    MyOpenHelper.TABLE_NAME,
                    projection,
                    selectionById,
                    selectionArgsById,
                    null,
                    null,
                    sortOrder
                )
                cursor.setNotificationUri(context?.contentResolver, uri)
                cursor
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.readableDatabase
        val match = uriMatcher.match(uri)
        return when (match) {
            WORK -> {
                val newRow = db.insert(MyOpenHelper.TABLE_NAME, null, values)
                if (newRow > 0) {
                    // tạo Uri mới với ID chèn vào để sau này truy vấn sau này (thêm, sửa, xóa)
                    val newUri = ContentUris.withAppendedId(CONTENT_URI, newRow)
                    // Thông báo thay đổi dữ liệu
                    context?.contentResolver?.notifyChange(newUri, null)
                    newUri
                } else throw SQLException("Failed to insert row into $uri")
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}