package com.example.takeoutapplication.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.takeoutapplication.viewmodel.MainFrameViewModel
import com.example.takeoutapplication.viewmodel.RestaurantInfoUiState
import com.example.takeoutapplication.viewmodel.RestaurantViewModel

@Composable
fun RestaurantPage(restaurantViewModel: RestaurantViewModel, mainFrameViewModel: MainFrameViewModel){
    when(restaurantViewModel.restaurantInfoUiState){
        is RestaurantInfoUiState.Success -> RestaurantManagePage(restaurantViewModel = restaurantViewModel, mainFrameViewModel = mainFrameViewModel)
        is RestaurantInfoUiState.Loading -> Text(text = "加载中")
        is RestaurantInfoUiState.Error -> RestaurantRegisterPage(restaurantViewModel = restaurantViewModel)
    }
}