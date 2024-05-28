package com.example.takeoutapplication.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.takeoutapplication.model.Screen
import com.example.takeoutapplication.utils.getFixed1
import com.example.takeoutapplication.viewmodel.MainViewModel
import com.example.takeoutapplication.viewmodel.ShoppingViewModel

@Composable
fun SearchPage(
    mainViewModel: MainViewModel,
    shoppingViewModel: ShoppingViewModel,
    navController: NavController
){
    LazyColumn {
        item {
            TextField(
                value = mainViewModel.searchInput,
                onValueChange = {
                    mainViewModel.searchInput = it
                    if(it.trim().isNotEmpty()) mainViewModel.search()
                },
                singleLine = true,
                placeholder = {
                    Text(text = "店铺名的前缀")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(mainViewModel.searchResult){
            Card(onClick = {
                shoppingViewModel.init(it.id)
                navController.navigate(Screen.ShoppingPage.route)
            }) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(8.dp).fillMaxWidth()
                ) {
                    Text(text = it.name)
                    Text(text = getFixed1(it.rate, it.rateCount) + "分")
                }
            }
        }
        if(mainViewModel.searchResult.isEmpty()){
            item { 
                Text(text = "没有搜索结果")
            }
        }
    }
}