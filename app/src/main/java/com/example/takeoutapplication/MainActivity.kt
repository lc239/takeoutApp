package com.example.takeoutapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.takeoutapplication.ui.screen.LoginPage
import com.example.takeoutapplication.ui.screen.MainFrame
import com.example.takeoutapplication.ui.theme.TakeOutApplicationTheme
import com.example.takeoutapplication.viewmodel.MainFrameViewModel
import com.example.takeoutapplication.viewmodel.UserInfoUiState
import com.example.takeoutapplication.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TakeOutApplicationTheme {
                val userViewModel= UserViewModel(LocalContext.current)
                val mainFrameViewModel = MainFrameViewModel(userViewModel)
                val logined = userViewModel.logined.collectAsState(initial = true)
                if(logined.value){
                    LaunchedEffect(key1 = logined.value){
                        userViewModel.getInfo() //登录了就获取个人信息
                    }
                    MainFrame(userViewModel, mainFrameViewModel)
                }
                else LoginPage(userViewModel)
            }
        }
    }
}