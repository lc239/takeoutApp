package com.example.takeoutapplication.utils

import java.text.DecimalFormat

fun yuanToFen(yuan: String): Long{
    var s1 = yuan.filter { c -> c != '.' }
    while (s1[0] == '0' && s1.length > 1 && s1[1] == '0'){
        s1 = s1.substring(1)
    }
    return s1.toLong()
}

fun fenToYuan(fen: Long): String{
    val s = fen.toString()
    val builder = StringBuilder(s).insert(0, "00").insert(s.length, '.')
    while (builder[0] == '0' && builder[1] != '.'){
        builder.deleteCharAt(0)
    }
    return builder.toString()
}

private val df1: DecimalFormat = DecimalFormat("#.0")
fun getFixed1(a: Long, b: Long): String{
    return if(b == 0L) "0.0" else df1.format(a / b)
}