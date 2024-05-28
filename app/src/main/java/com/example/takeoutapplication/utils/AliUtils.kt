package com.example.takeoutapplication.utils

object AliUtil{

    const val ALIOSS_PREFIX: String = "https://takeoutapp.oss-cn-beijing.aliyuncs.com/"

    fun nameToUrl(filename: String): String{
        return ALIOSS_PREFIX + filename
    }
}