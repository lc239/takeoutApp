package com.example.takeoutapplication.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.takeoutapplication.model.Screen

class MainFrameViewModel(userViewModel: UserViewModel): ViewModel(){
//    val bottomNavPages: MutableList<Screen> = mutableStateListOf(
//        Screen.MainPage,
//        Screen.RestaurantManagePage,
//        Screen.DeliveryPage,
//        Screen.MinePage
//    )
    val snackbarHostState = SnackbarHostState()

    init {
        userViewModel.mainFrameViewModel = this
    }
}