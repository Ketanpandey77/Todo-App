package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_todo.view.*
import java.text.SimpleDateFormat
import java.util.*

class ToDoAdapter(val list: List<ToDoModel>):RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_todo, parent, false)
        )
    }

    override fun getItemCount() =list.size

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemId(position: Int): Long {
        return list[position].id
    }

    class ToDoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(toDoModel: ToDoModel){
            with(itemView){
                txtShowTitle.text=toDoModel.title
                txtShowTask.text=toDoModel.description
                txtShowCategory.text=toDoModel.category
                updateTime(toDoModel.time)
                updateDate(toDoModel.date)
            }
        }
        private fun updateTime(time:Long){
            val myformat="h:mm a"
            val sdf=SimpleDateFormat(myformat)
            itemView.txtShowTime.text=sdf.format(Date(time))
        }
        private fun updateDate(date:Long){
            val myformat="EEE,d MMM yyyy"
            val sdf= SimpleDateFormat(myformat)
            itemView.txtShowDate.text=sdf.format(Date(date))
        }
    }

}
