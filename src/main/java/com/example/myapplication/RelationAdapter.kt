package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.EditRelationBinding

class RelationAdapter(private val relationMap: List<Relations>,private val activity:RecordsActivity) : RecyclerView.Adapter<RelationAdapter.ViewHolder>() {

//    private var sortedItems: List<Relations> = relationMap
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.edit_relation, parent, false)
//        return ViewHolder(view)


//        val handler = Handler(Looper.getMainLooper())
//        val delayMillis = 5000 // 1 秒
//        val runnable = object : Runnable {
//            override fun run() {
//                // 执行定时任务的代码
//                // 比如更新UI或执行后台操作等
//                // 一段时间后再次执行定时任务
//                activity.console(relationMap[0].maxValue.toString())
//
//            }
//        }
//
//// 第一次执行定时任务
//        handler.postDelayed(runnable, delayMillis.toLong())






        val binding:EditRelationBinding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.edit_relation,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    private fun buttonAppearance(relation: Relations, holder: ViewHolder){
        val rulerView: View = holder.itemView.findViewById(R.id.textView)
        val layoutParams = rulerView.layoutParams
        val rulerLength = 160 * relation.maxValue + 145
        layoutParams.width = rulerLength
        rulerView.layoutParams = layoutParams
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val relation = sortedItems[position]
        val relation = relationMap[position]

        if(relation.completed){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(activity, R.color.green))
        }else{
            holder.itemView.setBackgroundColor(ContextCompat.getColor(activity, R.color.white))
        }

        //数据绑定
        relation.let { holder.c2view.setImageResource(activity.getImageResource(relation.c2)) }
//        holder.sliderView.value= relation!!.maxValue.toFloat()
        for(i in 0..3){
            if(relation.value>=i)holder.buttonList[i].setTextAppearance(R.style.MyTextBlack)
            else holder.buttonList[i].setTextAppearance(R.style.notSelected)
            if(i==3)break
            if(relation.extraBoolean and (relation.extra==i))holder.plusList[i].setTextAppearance(R.style.MyTextBlack)
            else if(!relation.extraBoolean and (relation.extra==i))holder.plusList[i].setTextAppearance(R.style.notSelected)
            else holder.plusList[i].setTextAppearance(R.style.MyTextWhite)
        }
        holder.sliderView.progress=relation.maxValue
        buttonAppearance(relation, holder)


        //滑动条改变监听
        holder.sliderView.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //改变数值
                relation.maxValue = seekBar!!.progress
                activity.changeMaxValue(relation.c1, relation.c2, seekBar.progress)
                //改变显示
                buttonAppearance(relation, holder)

            }

        })

        //字母添加监听
        for(i in 0..3) {
            holder.buttonList[i].setOnClickListener {
                activity.changeValue(relation.c1, relation.c2, i)
                if (relation.extra != -1 && relation.extra < i && !relation.extraBoolean) {
                    relation.extraBoolean = true
                }
            }
            if(i==3)break
            holder.plusList[i].setOnClickListener {
                activity.changeExtra(relation.c1, relation.c2, i)
            }

        }

    }

//    fun sortList(i:Int){
//        sortedItems = when (i) {
//            0 -> relationMap.sortedBy { it.c2}          //班级顺序
//            1 -> relationMap.sortedBy { it.c2}          //游戏内顺序
//            2 -> relationMap.sortedBy { it.completion}  //完成度顺序
//            // 可以添加其他排序方式的处理逻辑
//            else -> {relationMap}
//        }
//        notifyDataSetChanged()
//    }
    override fun getItemCount(): Int {

        return relationMap.size
    }

    class ViewHolder(private val binding:EditRelationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:Relations) {
            binding.item = item
            binding.executePendingBindings()
        }

        val c2view:ImageView=itemView.findViewById(R.id.c2)
        val sliderView:SeekBar=itemView.findViewById(R.id.slider)

        private val sButton:TextView=itemView.findViewById(R.id.textView7)
        private val aButton:TextView=itemView.findViewById(R.id.textView3)
        private val bButton:TextView=itemView.findViewById(R.id.textView2)
        private val cButton:TextView=itemView.findViewById(R.id.textView1)

        private val cPlus:TextView=itemView.findViewById(R.id.textView4)
        private val bPlus:TextView=itemView.findViewById(R.id.textView5)
        private val aPlus:TextView=itemView.findViewById(R.id.textView6)

        val buttonList= arrayListOf(cButton,bButton,aButton,sButton)
        val plusList= arrayListOf(cPlus,bPlus,aPlus)



    }
//    class ViewHolder(itemView:EditRelationBinding) : RecyclerView.ViewHolder(itemView) {

//
//
//    }
//    fun sortItems() {
//        relationMap.sortBy { it.name } // 根据名称排序，你可以根据需要修改排序逻辑
//        notifyDataSetChanged() // 更新 RecyclerView
//    }
}

