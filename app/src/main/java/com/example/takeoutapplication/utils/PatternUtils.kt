package com.example.takeoutapplication.utils

object PatternUtils{
    private val phonePattern = Regex("^(13[0-9]|14[014-9]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")
    fun checkPhone(phone: String): Boolean{
        return phone.matches(phonePattern)
    }
}