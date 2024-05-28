package com.example.takeoutapplication.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.RecomposeScope
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.takeoutapplication.model.entity.OrderDeliveryView
import com.example.takeoutapplication.ui.components.delivery.OrderDetailDialog
import com.example.takeoutapplication.ui.components.delivery.OrderPreviewCard
import com.example.takeoutapplication.ui.components.delivery.TakeOrderDialog
import com.example.takeoutapplication.utils.DateUtils
import com.example.takeoutapplication.viewmodel.DeliveryInfoUiState
import com.example.takeoutapplication.viewmodel.DeliveryViewModel

@Composable
fun DeliveryPage(deliveryViewModel: DeliveryViewModel){
    val orders = deliveryViewModel.waitingOrders.collectAsLazyPagingItems()
    val orderCardModifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    if(deliveryViewModel.showTakeOrderDialog) TakeOrderDialog(deliveryViewModel, onConfirmed = {
        deliveryViewModel.takeOrder()
        orders.refresh()
        deliveryViewModel.showTakeOrderDialog = false
    }, onDismissed = {
        deliveryViewModel.showTakeOrderDialog = false
    })
    if(deliveryViewModel.showOrderDetailDialog) OrderDetailDialog(deliveryViewModel)
    if(deliveryViewModel.deliveryInfoUiState is DeliveryInfoUiState.Success) {
        LazyColumn(Modifier.padding(6.dp)) {
            item {
                Row(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "待送单数：${deliveryViewModel.deliveringOrders.size}")
                    Text(text = "完成单数：${deliveryViewModel.deliveryMan.completeCount}")
                }
                Row(
                    Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        modifier = Modifier.weight(1f),
                        shape = RectangleShape,
                        onClick = {
                            deliveryViewModel.showDeliveringList = true
                        },
                        colors = if (deliveryViewModel.showDeliveringList) ButtonDefaults.textButtonColors(
                            containerColor = Color.Green
                        ) else ButtonDefaults.textButtonColors()
                    ) {
                        Text(text = "待送单")
                    }
                    TextButton(
                        modifier = Modifier.weight(1f),
                        shape = RectangleShape,
                        onClick = {
                            orders.refresh()
                            deliveryViewModel.showDeliveringList = false
                        },
                        colors = if (!deliveryViewModel.showDeliveringList) ButtonDefaults.textButtonColors(
                            containerColor = Color.Green
                        ) else ButtonDefaults.textButtonColors()
                    ) {
                        Text(text = "可接单")
                    }
                }
            }
            if (deliveryViewModel.showDeliveringList) {
                items(deliveryViewModel.deliveringOrders) { order ->
                    OrderPreviewCard(
                        order = OrderDeliveryView(order.orderId!!, order.address, order.createTime!!),
                        modifier = orderCardModifier
                    ){
                        deliveryViewModel.openOrderDetailDialog(order)
                    }
                }
            } else {
                items(orders.itemCount) { index ->
                    orders[index]?.let { order ->
                        OrderPreviewCard(order = order, modifier = orderCardModifier){
                            deliveryViewModel.openTakeOrderDialog(order)
                        }
                    }
                }
                if (orders.loadState.append.endOfPaginationReached) item {
                    Text(text = "没有更多了，刷新试试吧")
                }
            }
        }
    }
}