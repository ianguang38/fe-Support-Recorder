package com.example.myapplication

import kotlinx.serialization.Serializable

@Serializable
data class Relations(
    val c1:Int,
    val c2:Int,
    var maxValue:Int=2,
    var value:Int=-1,
    var extra:Int=-1,   //如果没有即为-1，如果有即为位置
    var extraBoolean: Boolean=false



){


    private val transformationMap = mapOf(
        'a' to '!',
        'b' to '@',
        'c' to '#',
        // 其他映射关系
        'z' to '*'
    )

    val supportNumber:Int
            get() = if(extra>-1) maxValue+1 else maxValue

    val actuallValue:Int
            get() = if(extra>-1&&extraBoolean) value+1 else value

    val completion:Float
        get() = (actuallValue+1f)/(supportNumber+1f)

    val completed:Boolean
        get() = completion.toInt()==1

    val gameOrder: Int
        get(){
            val transMatrix: IntArray =intArrayOf(
                3,12,13,14,15,16,17,18,
                2,5,6,7,8,9,10,11,
                4,19,20,21,22,23,24,25,
                0,1,26,27,28,29,30,31,32,33,34,35,36,37)
            return transMatrix[c2]
        }


}


