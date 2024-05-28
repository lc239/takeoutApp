package com.example.takeoutapplication.ui.components.delivery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.takeoutapplication.model.entity.OrderDeliveryView
import com.example.takeoutapplication.utils.DateUtils

@Composable
fun OrderPreviewCard(order: OrderDeliveryView, modifier: Modifier, onClick: () -> Unit){
    val paddingModifier = Modifier.padding(horizontal = 4.dp)
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = Color.White
        ),
        onClick = onClick
    ) {
        Row(
            modifier = paddingModifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "姓名：" + order.address.name)
            Text(text = "手机：" + order.address.phone)
        }
        Text(text = "地址：" + order.address.address, modifier = paddingModifier)
        Text(text = "下单时间：" + DateUtils.instantToString1(order.createTime), modifier = paddingModifier)
    }
}