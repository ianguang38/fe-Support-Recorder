package com.example.myapplication

import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class Records(
    var name:String="",
    val fn:String = getDefaultFilename()
):Serializable{
    companion object {
        private fun getDefaultFilename():String{
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss") // 定义时间格式
            return "relation"+currentDateTime.format(formatter)+".gson"
        }

    }
}