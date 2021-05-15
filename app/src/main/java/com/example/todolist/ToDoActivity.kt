package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_to_do.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

val DB_NAME="todo.db"
class ToDoActivity : AppCompatActivity(), View.OnClickListener {

    var finalDate = 0L
    var finalTime = 0L

    lateinit var myCalender:Calendar
    lateinit var dateSetListener:DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener:TimePickerDialog.OnTimeSetListener

    val labels= arrayOf("Personal","College","Competitive Exam","Competitive Coding","Android Development","College Assignment","College Project")

    //declaring database here
    val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)

        etDate.setOnClickListener(this)
        etSetTime.setOnClickListener(this)
        btnSave.setOnClickListener(this)

        setUpSpinner()
    }

    private fun setUpSpinner() {
        val arrayAdapter=ArrayAdapter<String>(
               this,android.R.layout.simple_spinner_dropdown_item,labels
        )
        spCategory.adapter=arrayAdapter
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.etDate->{
                setListener()
            }
            R.id.etSetTime->{
                setTimeListener()
            }
            R.id.btnSave->{
                saveData()
            }
        }
    }

    private fun saveData() {
        val category = spCategory.selectedItem.toString()
        val title = etTitle.text.toString()
        val description = etTask.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext db.todoDao().insertTask(
                    ToDoModel(
                        title,
                        description,
                        category,
                        finalDate,
                        finalTime
                    )
                )
            }
            finish()
        }
    }

    private fun setTimeListener() {
        myCalender= Calendar.getInstance()
        timeSetListener=TimePickerDialog.OnTimeSetListener{_:TimePicker,hours:Int,minutes:Int->
            myCalender.set(Calendar.HOUR_OF_DAY,hours)
            myCalender.set(Calendar.MINUTE,minutes)
            updateTime()
        }
        val timePickerDialog=TimePickerDialog(
            this,timeSetListener,myCalender.get(Calendar.HOUR_OF_DAY),
                myCalender.get(Calendar.MINUTE),true
        )
        timePickerDialog.show()
    }

    private fun updateTime() {
        val myFormat="h:mm a"
        val sdf=SimpleDateFormat(myFormat)
        finalTime = myCalender.time.time
        etSetTime.setText(sdf.format(myCalender.time))
    }

    private fun setListener() {
        myCalender= Calendar.getInstance()
        dateSetListener=DatePickerDialog.OnDateSetListener{ _: DatePicker, year:Int, month:Int, dayOfMonth:Int->
            myCalender.set(Calendar.YEAR,year)
            myCalender.set(Calendar.MONTH,month)
            myCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateDate()
        }
        val datePickerDialog=DatePickerDialog(
                this,dateSetListener,myCalender.get(Calendar.YEAR),
                 myCalender.get(Calendar.MONTH),myCalender.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate=System.currentTimeMillis()
        datePickerDialog.show();
    }

    private fun updateDate() {
        //Mon , 30 Jan 2001
        val myFormat="EEE,dd MMM YYYY"
        val sdf=SimpleDateFormat(myFormat)
        finalDate = myCalender.time.time
        etDate.setText(sdf.format(myCalender.time))
        etTime.visibility=View.VISIBLE
    }
}