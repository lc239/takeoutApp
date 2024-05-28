package com.example.takeoutapplication.ui.components.delivery

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.takeoutapplication.viewmodel.DeliveryViewModel

@Composable
fun OrderDetailDialog(deliveryViewModel: DeliveryViewModel){
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        title = {
            Text(text = "确认订单")
        },
        confirmButton = {
            Button(onClick = {
                deliveryViewModel.completeOrder()
                deliveryViewModel.showOrderDetailDialog = false
            }) {
                Text(text = "确认送达")
            }
        },
        dismissButton = {
            Button(onClick = {
                deliveryViewModel.showOrderDetailDialog = false
            }) {
                Text(text = "关闭")
            }
        }
    )
}