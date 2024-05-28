package com.example.takeoutapplication.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.takeoutapplication.viewmodel.DeliveryViewModel

//已废弃，角色管理已经修改
@Composable
fun DeliveryRegisterPage(deliveryViewModel: DeliveryViewModel){
    Box(modifier = Modifier.fillMaxSize()){
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = { deliveryViewModel.register() }
        ) {
            Text(text = "成为骑手")
        }
    }
}