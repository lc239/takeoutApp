package com.example.takeoutapplication.ui.screen.mine

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.takeoutapplication.viewmodel.CommentUiState
import com.example.takeoutapplication.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun CommentDialog(userViewModel: UserViewModel, navController: NavController){
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "评分：")
                RateStars{ value ->
                    userViewModel.comment = userViewModel.comment.copy(rate = value)
                }
            }
            TextField(
                value = userViewModel.comment.content,
                onValueChange = {
                    if(it.length <= 100) userViewModel.comment = userViewModel.comment.copy(content = it)
                },
                label = {
                    Text(text = "内容")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = {
                        userViewModel.commentOrder{
                            navController.popBackStack()
                            userViewModel.showHistoryOrdersMsg("评价完成")
                        }
                    },
                    enabled = userViewModel.commentUiState !is CommentUiState.Loading
                ) {
                    Text(text = "确认")
                }
                Button(onClick = {
                    navController.popBackStack()
                }) {
                    Text(text = "取消")
                }
            }
        }
    }
}

@Composable
fun RateStars(
    modifier: Modifier = Modifier,
    defaultValue: Int = 5,
    readOnly: Boolean = false,
    onChange: (Int) -> Unit = {}
){
    LaunchedEffect(true){
        println(2)
        onChange(defaultValue) //第一次进入调用一次
    }
    var value by remember {
        mutableIntStateOf(defaultValue)
    }
    Row(modifier){
        repeat(5){ index ->
            if(readOnly){
                Icon(
                    imageVector = if(value < index + 1) Icons.Default.StarBorder else Icons.Default.Star,
                    tint = Color.Yellow,
                    contentDescription = null
                )
            }else{
                IconButton(onClick = {
                    value = index + 1
                    onChange(value)
                }) {
                    Icon(
                        imageVector = if(value < index + 1) Icons.Default.StarBorder else Icons.Default.Star,
                        tint = Color.Yellow,
                        contentDescription = null
                    )
                }
            }
        }
    }
}