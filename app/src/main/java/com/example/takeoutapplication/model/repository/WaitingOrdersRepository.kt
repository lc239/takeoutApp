package com.example.takeoutapplication.model.repository

import com.example.takeoutapplication.model.repository.pagingsource.WaitingOrdersPagingSource
import com.example.takeoutapplication.network.DeliveryApi
import com.example.takeoutapplication.viewmodel.UserViewModel

class WaitingOrderRepository(val userViewModel: UserViewModel) {
    fun waitingOrdersPagingSource() = WaitingOrdersPagingSource(DeliveryApi.retrofitService, userViewModel)
}