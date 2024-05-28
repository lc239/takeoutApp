package com.example.takeoutapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.takeoutapplication.model.request.RestaurantRegister
import com.example.takeoutapplication.viewmodel.RestaurantRegisterUiState
import com.example.takeoutapplication.viewmodel.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantRegisterPage(restaurantViewModel: RestaurantViewModel){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "注册店铺")
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.tertiary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var restaurantName by remember { mutableStateOf("") }
            var introduction by remember { mutableStateOf("") }
            val textFieldModifier = Modifier.width(300.dp)
            TextField(
                value = restaurantName,
                onValueChange = {
                    if(it.length <= 15) restaurantName = it
                },
                singleLine = true,
                label = {
                    Text(
                        text = "店铺名",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                },
                modifier = textFieldModifier.padding(bottom = 10.dp)
            )
            TextField(
                value = introduction,
                onValueChange = {
                    if(it.length <= 200) introduction = it
                },
                singleLine = false,
                label = {
                    Text(
                        text = "店铺简介",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                },
                modifier = textFieldModifier
            )
            TextButton(
                enabled = restaurantViewModel.restaurantRegisterUiState !is RestaurantRegisterUiState.Loading, //加载时禁用
                onClick = {
                    if(restaurantName.isNotEmpty()){
                        restaurantViewModel.register(RestaurantRegister(restaurantName, introduction))
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 6.dp)
            ) {
                Text(text = "注册", color = Color.White, fontSize = 16.sp)
            }
        }

    }
}