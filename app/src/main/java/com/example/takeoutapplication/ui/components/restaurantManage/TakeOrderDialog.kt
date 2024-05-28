package com.example.takeoutapplication.ui.components.restaurantManage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.takeoutapplication.utils.fenToYuan
import com.example.takeoutapplication.viewmodel.RestaurantViewModel

@Composable
fun TakeOrderDialog(restaurantViewModel: RestaurantViewModel){
    val menuCardModifier =  Modifier.padding(bottom = 8.dp)
    val menuCardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = Color.White
    )
    val maxWidthModifier = Modifier.fillMaxWidth()
    val modifier1 = maxWidthModifier.padding(2.dp)
    Dialog(onDismissRequest = { /*TODO*/ }) {
        restaurantViewModel.orderProcessing.let { order ->
            Card(
                modifier = maxWidthModifier
            ) {
                LazyColumn(
                    maxWidthModifier
                        .padding(6.dp)
                        .heightIn(max = 400.dp)
                ){
                    items(order.menus){
                        Card(
                            modifier = menuCardModifier,
                            colors = menuCardColors
                        ) {
                            Row(
                                modifier = modifier1,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "菜名：" + it.name)
                                Text(text = "数量：" + it.num.toString())
                            }
                            Row(
                                modifier = modifier1,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(text = fenToYuan(it.price * it.num)+ "元")
                            }
                        }
                    }
                }
                Text(
                    modifier = maxWidthModifier,
                    textAlign = TextAlign.End,
                    text = "总价格：${fenToYuan(order.price)}元"
                )
                Row(
                    modifier = maxWidthModifier,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = order.address.name)
                    Text(text = order.address.phone)
                }
                Text(text = order.address.address)
                Row(
                    modifier = maxWidthModifier,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    if(!restaurantViewModel.orders[order]!!){
                        Button(onClick = {
                            restaurantViewModel.showOrderDialog = false
                            restaurantViewModel.takeOrder(order)
                        }) {
                            Text(text = "接单")
                        }
                        Button(onClick = {
                            restaurantViewModel.showOrderDialog = false
                            restaurantViewModel.rejectOrder(order)
                        }) {
                            Text(text = "拒接")
                        }
                    }
                    Button(onClick = {
                        restaurantViewModel.showOrderDialog = false
                    }) {
                        Text(text = "关闭")
                    }
                }
            }
        }
    }
}