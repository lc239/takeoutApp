package com.example.takeoutapplication.ui.components.delivery

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.takeoutapplication.viewmodel.DeliveryViewModel

@Composable
fun TakeOrderDialog(deliveryViewModel: DeliveryViewModel, onConfirmed: () -> Unit, onDismissed: () -> Unit){
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = {
            Button(onClick = onConfirmed) {
                Text(text = "确定")
            }
        },
        dismissButton = {
            Button(onClick = onDismissed) {
                Text(text = "取消")
            }
        },
        title = {
            Text(text = "接取此订单？")
        }
    )
}