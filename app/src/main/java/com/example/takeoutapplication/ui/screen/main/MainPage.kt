package com.example.takeoutapplication.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.takeoutapplication.model.Screen
import com.example.takeoutapplication.ui.components.main.RestaurantCard
import com.example.takeoutapplication.viewmodel.MainViewModel
import com.example.takeoutapplication.viewmodel.ShoppingViewModel

@Composable
fun MainPage(
    mainViewModel: MainViewModel,
    shoppingViewModel: ShoppingViewModel,
    navController: NavController
){
    val recommendRestaurants = mainViewModel.recommendRestaurants.collectAsLazyPagingItems()
    val restaurantCardModifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(bottom = 4.dp)
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .clickable {
                    navController.navigate(Screen.SearchPage.route)
                }
                .padding(7.dp, 10.dp)
        ) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            Text(text = "点击此处搜索店铺")
        }
        LazyColumn{
            items(recommendRestaurants.itemCount){ index ->
                recommendRestaurants[index]?.let {
                    RestaurantCard(restaurant = it, modifier = restaurantCardModifier) {
                        shoppingViewModel.init(it.id)
                        navController.navigate(Screen.ShoppingPage.route)
                    }
                }
            }
            if(recommendRestaurants.loadState.append.endOfPaginationReached){
                item {
                    Text(text = "没有更多商家了")
                }
            }else if(recommendRestaurants.itemCount == 0){
                item {
                    Text(text = "加载中")
                }
            }
        }
    }
}