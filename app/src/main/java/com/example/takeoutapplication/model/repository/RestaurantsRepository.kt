package com.example.takeoutapplication.model.repository

import com.example.takeoutapplication.model.repository.pagingsource.RestaurantsPagingSource
import com.example.takeoutapplication.network.RestaurantApi
import com.example.takeoutapplication.viewmodel.UserViewModel

class RestaurantsRepository(val userViewModel: UserViewModel) {
    fun restaurantsPagingSource() = RestaurantsPagingSource(RestaurantApi.retrofitService, userViewModel)
}