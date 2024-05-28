package com.example.takeoutapplication.ui.components.restaurantManage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.takeoutapplication.viewmodel.RestaurantViewModel

@Composable
fun AddCategoryDialog(restaurantViewModel: RestaurantViewModel){
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        ) {
            TextField(
                modifier = Modifier.padding(10.dp),
                value = restaurantViewModel.newCategoryName,
                onValueChange = {
                    if(it.length <= 6) restaurantViewModel.newCategoryName = it
                },
                label = {
                    Text(text = "新分类名")
                }
            )
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    modifier = Modifier.padding(end = 10.dp),
                    onClick = {
                        if(restaurantViewModel.newCategoryName.isNotEmpty()){
                            restaurantViewModel.addCategory()
                        }
                        restaurantViewModel.showAddCategoryDialog = false
                    }
                ) {
                    Text(text = "确定")
                }
                Button(onClick = {
                    restaurantViewModel.showAddCategoryDialog = false
                }) {
                    Text(text = "取消")
                }
            }
        }
    }
}