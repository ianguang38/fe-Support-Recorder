package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityRecordsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.Exception

class RecordsActivity : AppCompatActivity() {



    private val charaNumber=38       //角色总数
    private var selected:Int=-1     //当前选择的
    private var selected1:Int=-1    //之前选的主角色
    private var orderType:Int=0

    private var fn:String=""



    private var dic=HashMap<Int,HashMap<Int, Relations>>()


    private lateinit var mainChara: ImageView
    private lateinit var recyclerView: RecyclerView
    private val imageResources = intArrayOf(
        R.drawable.a1,
        R.drawable.a2,
        R.drawable.a3,
        R.drawable.a4,
        R.drawable.a5,
        R.drawable.a6,
        R.drawable.a7,
        R.drawable.a8,
        R.drawable.b1,
        R.drawable.b2,
        R.drawable.b3,
        R.drawable.b4,
        R.drawable.b5,
        R.drawable.b6,
        R.drawable.b7,
        R.drawable.b8,
        R.drawable.c1,
        R.drawable.c2,
        R.drawable.c3,
        R.drawable.c4,
        R.drawable.c5,
        R.drawable.c6,
        R.drawable.c7,
        R.drawable.c8,
        R.drawable.d1,
        R.drawable.d2,
        R.drawable.d3,
        R.drawable.d4,
        R.drawable.d5,
        R.drawable.d6,
        R.drawable.d7,
        R.drawable.d8,
        R.drawable.d9,
        R.drawable.d10,
        R.drawable.d11,
        R.drawable.d12,
        R.drawable.d13,
        R.drawable.d14,

        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)


        val intent = intent
        val extras = intent.extras
        try{
            fn= (extras!!.get("data") as? Records)!!.fn
            dic= loadData()!!
        }catch (e:Exception){
            for(i in 0 until charaNumber){
                dic[i]= HashMap()
            }
        }


        val cleanButton: Button = findViewById(R.id.cleanButton)
        cleanButton.setOnClickListener {
            cleanData()
        }
        //上方角色列表
        val gridLayout: GridLayout = findViewById(R.id.gridLayout)
        for (i in 0 until charaNumber) {
            val imageView = ImageView(this)
            imageView.setImageResource(imageResources[i])

            // 设置图片大小
            val params = GridLayout.LayoutParams()
            params.width = 130 // 设置图片宽度为 200 像素
            params.height = 130 // 设置图片高度为 200 像素
            imageView.layoutParams = params
            gridLayout.addView(imageView)
            //图片按钮id为0开始的自然数
            imageView.id = i
            imageView.setOnClickListener { view ->
                clickAt(view)
            }
        }

        //顺序按钮
        val cOrder: Button =findViewById(R.id.classOrder)
        val gOrder: Button =findViewById(R.id.gameOrder)
        val fOrder: Button =findViewById(R.id.finishOrder)
        val orderButtonList= arrayListOf(cOrder,gOrder,fOrder)
        for(i in 0..2){
            orderButtonList[i].setOnClickListener{
                orderType=i
                for(b in orderButtonList)b.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
                updateS2List()
            }
        }



        //右侧羁绊列表
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mainChara= findViewById(R.id.mainChara)



    }

    //    @SuppressLint("ResourceType")
    @SuppressLint("ResourceAsColor")
    private fun clickAt(view: View){
        selected=view.id
        if(selected1==-1 or selected1){//没有选择主角色
            selected1=view.id
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            updateS1(view)
        }else{//已经选择主角色
            if(selected1==view.id){//取消主角色
                updateS1(null)
                selected1=-1
            }else{
                if(dic[selected1]?.contains(selected) == true){//取消选择的副角色
                    view.setBackgroundColor(Color.rgb(255, 255, 255))
                    dic[selected1]?.remove(selected)
                    dic[selected]?.remove(selected1)
                    updateS2List()
                }else{  //选择副角色
                    view.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
                    selected=view.id

                    dic[selected1]?.set(selected, Relations(selected1,selected))
                    dic[selected]?.set(selected1, Relations(selected,selected1))



                    updateS2List()
                }
            }
        }

    }

    private fun updateS1(view: View?){
        //清空高亮
        for (i in 0 until charaNumber){
            findViewById<View>(i).setBackgroundColor(Color.rgb(255,255,255))
        }

        if(view == null){   //取消主角色
            mainChara.setImageDrawable(null)

            recyclerView.adapter=null


        }else{              //设置主角色
            mainChara.setImageResource(imageResources[view.id])

            //设置高光
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            for(i in dic[selected1]!!.keys){
                findViewById<View>(i).setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            }


            updateS2List()

        }
    }
    private fun updateS2List() {
        if(selected1>-1){
            val sortType=when(orderType) {
                1 -> compareBy<Relations> { it.gameOrder}
                2 -> compareBy<Relations> { it.completion}
                else -> {compareBy<Relations> { it.c2}}
            }
            val sortedList=ArrayList<Relations>(dic[selected1]!!.values).sortedWith(sortType)
            recyclerView.adapter = RelationAdapter(sortedList, this)
        }

        freshCompletion()
    }

    public fun getImageResource(i:Int): Int {
        return imageResources[i]
    }

    public fun console(notification:String){
        Toast.makeText(this,notification, Toast.LENGTH_SHORT).show()
    }

    fun changeValue(c1:Int,c2:Int,value:Int){
        dic[c1]!![c2]!!.value=value
        dic[c2]!![c1]!!.value=value
        recyclerView.adapter!!.notifyDataSetChanged()
        freshCompletion()
    }

    fun changeMaxValue(c1:Int,c2:Int,value:Int){
        dic[c1]!![c2]!!.maxValue=value
        dic[c2]!![c1]!!.maxValue=value
        freshCompletion()
    }

    fun changeExtra(c1:Int,c2:Int,value:Int){
        val relation: Relations? =dic[c1]!![c2]
        val extra= relation?.extra
        val extraBoolean=relation?.extraBoolean

        if((extra!=-1) and (extra==value)){
            if(extraBoolean==false){        //点亮plus
                dic[c1]!![c2]?.extraBoolean=true
                dic[c2]!![c1]?.extraBoolean=true
                dic[c1]!![c2]?.value= Integer.max(value, relation.value)
                dic[c2]!![c1]?.value= Integer.max(value, relation.value)
            }else{                          //去掉extra
                dic[c1]!![c2]?.extra=-1
                dic[c1]!![c2]?.extraBoolean=false
                dic[c2]!![c1]?.extraBoolean=false
            }
            recyclerView.adapter!!.notifyDataSetChanged()
        }else{                               //增加extra or 更换
            dic[c1]!![c2]?.extra=value
            dic[c2]!![c1]?.extra=value
            recyclerView.adapter!!.notifyDataSetChanged()

        }
        freshCompletion()

    }

    private fun freshCompletion(){
        val supportNumber=findViewById<TextView>(R.id.supportNumber)
        val completion=findViewById<TextView>(R.id.completion)

        if(selected1==-1){
            supportNumber.text="羁绊数量:"
            completion.text="完成度："
        }else{
            var valueSum=0
            var supportSum=0
            for(relation in dic[selected1]!!.values){
                valueSum+=relation.actuallValue+1
                supportSum+=relation.supportNumber+1
            }
            supportNumber.text= "羁绊数量:$valueSum/$supportSum"
            if(supportSum==0)completion.text="完成度：0%"
            else completion.text="完成度："+"%.0f".format((valueSum.toFloat()/supportSum.toFloat()*100))+"%"

        }
        saveData()
    }

    private fun saveData(){
        val gson = Gson()
        val data = gson.toJson(dic)
        val file = File(this.filesDir, fn)
        file.writeText(data)
    }

    private fun loadData():HashMap<Int, HashMap<Int, Relations>>?{
        val file = File(this.filesDir,fn)
        return if (file.exists()){
            val data = file.readText()
            val mapType = object : TypeToken<HashMap<Int, HashMap<Int, Relations>>>() {}.type
            val dic: HashMap<Int, HashMap<Int, Relations>> = Gson().fromJson(data, mapType)
            dic
//            null
        }else{
            null
        }
    }

    private fun cleanData(){
        val file = File(this.filesDir, "relation.gson")
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

    override fun onBackPressed() {
        // 执行返回上一个界面的操作，例如关闭当前 Activity
        super.onBackPressed() // 调用父类的方法以实现默认的返回操作


    }



}


