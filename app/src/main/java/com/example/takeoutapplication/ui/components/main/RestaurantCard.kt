package com.example.takeoutapplication.ui.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.takeoutapplication.model.entity.RestaurantPreview
import com.example.takeoutapplication.utils.AliUtil
import com.example.takeoutapplication.utils.fenToYuan
import com.example.takeoutapplication.utils.getFixed1
import com.example.takeoutapplication.viewmodel.ShoppingViewModel
import java.text.DecimalFormat

@Composable
fun RestaurantCard(
    restaurant: RestaurantPreview,
    modifier: Modifier,
    onClick: () -> Unit
){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(end = 6.dp),
                model = AliUtil.nameToUrl(restaurant.imageFilename),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                Modifier.weight(1f)
            ) {
                Text(text = restaurant.name)
                Text(text = "评分：${getFixed1(restaurant.rate, restaurant.rateCount)}分")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "销售量：${restaurant.saleNum}")
                    Text(text = "配送费：${fenToYuan(restaurant.deliveryPrice)}元")
                }
            }
        }
    }
}