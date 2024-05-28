package com.example.takeoutapplication.ui.components.restaurantManage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.takeoutapplication.R
import com.example.takeoutapplication.utils.DateUtils
import com.example.takeoutapplication.utils.fenToYuan
import com.example.takeoutapplication.viewmodel.RestaurantViewModel

@Composable
fun TakeOrdersView(
    modifier: Modifier,
    restaurantViewModel: RestaurantViewModel
){
    val orderCardModifier = Modifier
        .padding(horizontal = 4.dp, vertical = 6.dp)
        .fillMaxWidth()
        .padding(end = 6.dp)
    val orderCardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = Color.White
    )
    val orderTakenCardColors = CardDefaults.cardColors(
        containerColor = colorResource(id = R.color.taken_green),
        contentColor = Color.White
    )
    LazyColumn(modifier){
        items(restaurantViewModel.orders.toList()){
            Card(
                modifier = orderCardModifier,
                colors = if(it.second) orderTakenCardColors else orderCardColors,
                shape = RoundedCornerShape(2.dp),
                onClick = {
                    restaurantViewModel.openOrderDialog(it.first)
                }
            ) {
                Text(text = "订单号：${it.first.orderId}")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "总价：${fenToYuan(it.first.price)}元")
                    Text(text = "下单时间：${DateUtils.instantToString1(it.first.createTime!!)}")
                }
            }
        }
    }
}