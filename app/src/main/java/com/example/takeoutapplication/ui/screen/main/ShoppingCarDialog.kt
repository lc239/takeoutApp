package com.example.takeoutapplication.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.takeoutapplication.ui.components.shopping.OrderedMenuCard
import com.example.takeoutapplication.utils.fenToYuan
import com.example.takeoutapplication.viewmodel.ShoppingViewModel

@Composable
fun ShoppingCarDialog(shoppingViewModel: ShoppingViewModel, navController: NavController){
    val modifier = Modifier.fillMaxWidth()
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            modifier
                .height(550.dp)
                .fillMaxWidth()
        ) {
            LazyColumn(
                Modifier
                    .height(400.dp)
                    .fillMaxWidth()
            ){
                items(shoppingViewModel.shoppingCar.toList()){
                    OrderedMenuCard(shoppingViewModel = shoppingViewModel, menu = it.first, modifier = modifier.padding(4.dp))
                }
            }
            HorizontalDivider()
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = if(shoppingViewModel.selectedAddress == null) "未选择地址" else "已选择地址")
                Button(onClick = {
                    shoppingViewModel.showAddressDialog = true
                }) {
                    Text(text = "选择地址")
                }
            }
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "配送费：${fenToYuan(shoppingViewModel.restaurant.deliveryPrice)}元")
                Text(text = "总价格：${fenToYuan(shoppingViewModel.totalPrice())}元")
            }
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = {
                    shoppingViewModel.putOrder()
                    navController.popBackStack()
                }) {
                    Text(text = "下单")
                }
                Button(onClick = {
                    navController.popBackStack()
                }) {
                    Text(text = "关闭")
                }
            }
        }
    }
}