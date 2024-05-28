package com.example.takeoutapplication.ui.components.mine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.takeoutapplication.model.entity.Order
import com.example.takeoutapplication.model.entity.Restaurant
import com.example.takeoutapplication.network.RestaurantApi
import com.example.takeoutapplication.utils.DateUtils
import com.example.takeoutapplication.utils.fenToYuan
import com.example.takeoutapplication.viewmodel.UserViewModel
import kotlinx.coroutines.flow.first
import java.io.IOException

@Composable
fun DeliveringOrderCard(order: Order, modifier: Modifier, userViewModel: UserViewModel){
    var restaurantInfo: Restaurant? by remember {
        mutableStateOf(null)
    }
    if(restaurantInfo == null){
        LaunchedEffect(true){
            try {
                println(1111)
                val result = RestaurantApi.retrofitService.getByRestaurantId(
                    userViewModel.userInfoManager.bearerAuth.first(),
                    order.restaurantId!!
                )
                if(result.code == 0){
                    restaurantInfo = result.data!!
                }else{
                    throw Exception(result.message)
                }
            }catch (e: IOException){

            }catch (e: Exception){

            }
        }
    }

    Card(
        modifier = modifier
    ) {
        Text(text = "订单号：${order.orderId}")
        Text(text = if(restaurantInfo == null) "加载中" else restaurantInfo!!.name)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "总价：${fenToYuan(order.price)}元")
            Text(text = "下单时间：${DateUtils.instantToString1(order.createTime!!)}")
        }
    }
}