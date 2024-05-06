package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var recordList= arrayListOf<Records>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadData()
        recyclerView.adapter=RecordAdapter(recordList,this)


        val refreshButton=findViewById<Button>(R.id.refreshButton)
        refreshButton.setOnClickListener{
            recyclerView.adapter=RecordAdapter(recordList,this)
        }
        val addRecordButton=findViewById<Button>(R.id.addRecordButton)
        addRecordButton.setOnClickListener{
            addRecord()
        }
        val cleanRecordButton=findViewById<Button>(R.id.cleanRecordButton)
        cleanRecordButton.setOnClickListener{
            val file = File(this.filesDir, "records.gson")
            if (file.exists()){
                AlertDialog.Builder(this)
                    .setTitle("确认")
                    .setMessage("确定reset人际关系吗？")
                    .setPositiveButton("确定") { _, _ ->
                        file.delete()
                    }
                    .setNegativeButton("取消") { _, _ ->}
                    .show()

            }
        }




    }

    fun goToRecord(record: Records){
        val intent=Intent(this,RecordsActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("data",record)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    private fun saveData(){
        val gson = Gson()
        val data = gson.toJson(recordList)
        val file = File(this.filesDir, "records.gson")
        file.writeText(data)
    }
    private fun loadData(){
        val file = File(this.filesDir, "records.gson")
        if (file.exists()){
            val data = file.readText()
            val mapType = object : TypeToken<ArrayList<Records>>() {}.type
            val dic: ArrayList<Records> = Gson().fromJson(data, mapType)
            recordList=dic
        }else{
            addRecord()
        }
    }
//    public fun console(notification:String){
//        Toast.makeText(this,notification, Toast.LENGTH_SHORT).show()
//    }
//    override fun onResume() {
//        super.onResume()
//        console("back!!!")
//
//    }


    private fun addRecord(){
        val newRecord=Records()

        val index=recyclerView.adapter!!.itemCount
        recordList.add(newRecord)
        recyclerView.adapter?.notifyItemInserted(index)
        saveData()
    }

    fun comfirm(position:Int,operation:(Int)->Int){
        AlertDialog.Builder(this)
            .setTitle("确认")
            .setMessage("确定reset人际关系吗？")
            .setPositiveButton("确定") { _, _ ->
                operation(position)
            }
            .setNegativeButton("取消") { _, _ ->}
            .show()
    }



}