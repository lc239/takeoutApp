package com.example.takeoutapplication.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun TopEndAndBottomStartMask(
    topEndColor: Color = Color(0xffbb8378),
    bottomStartColor: Color = Color(0xff1e6de6),
    topEndOffset: Offset,
    bottomStartOffset: Offset
){
    //右上向左下的渐变遮罩
    Box(modifier = Modifier.fillMaxSize().background(
        Brush.linearGradient(
        listOf(topEndColor, Color.Transparent),
        start = topEndOffset,
        end = bottomStartOffset
    )))
    //左下向右上的渐变遮罩
    Box(modifier = Modifier.background(
        Brush.linearGradient(
        listOf(bottomStartColor, Color.Transparent),
        start = bottomStartOffset,
        end = topEndOffset
    )).fillMaxSize())
}