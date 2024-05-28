package com.example.takeoutapplication.model.repository

import com.example.takeoutapplication.model.repository.pagingsource.HistoryOrdersPagingSource
import com.example.takeoutapplication.model.repository.pagingsource.RestaurantsPagingSource
import com.example.takeoutapplication.network.RestaurantApi
import com.example.takeoutapplication.network.UserApi
import com.example.takeoutapplication.viewmodel.UserViewModel

class HistoryOrdersRepository(val userViewModel: UserViewModel) {
    fun historyOrdersPagingSource() = HistoryOrdersPagingSource(UserApi.retrofitService, userViewModel)
}