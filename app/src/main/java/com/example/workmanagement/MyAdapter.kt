package com.example.workmanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workmanagement.WorkModel.Work

class MyAdapter(private var workList: List<Work>) : RecyclerView.Adapter<MyAdapter.WorkViewHolder>() {

    inner class WorkViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val TV_workName : TextView
        val TV_workTime : TextView
        init {
            TV_workName = item.findViewById(R.id.TV_work_name)
            TV_workTime = item.findViewById(R.id.TV_work_time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.work_item, parent, false)
        return WorkViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val work = workList[position]
        holder.TV_workName.text = work.workName
        holder.TV_workTime.text = work.time
    }

    override fun getItemCount(): Int {
        return workList.size
    }
}