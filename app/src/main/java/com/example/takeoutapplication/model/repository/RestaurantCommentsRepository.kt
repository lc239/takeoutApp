package com.example.takeoutapplication.model.repository

import com.example.takeoutapplication.model.repository.pagingsource.HistoryOrdersPagingSource
import com.example.takeoutapplication.model.repository.pagingsource.RestaurantCommentsPagingSource
import com.example.takeoutapplication.model.repository.pagingsource.RestaurantsPagingSource
import com.example.takeoutapplication.network.RestaurantApi
import com.example.takeoutapplication.network.UserApi
import com.example.takeoutapplication.viewmodel.ShoppingViewModel
import com.example.takeoutapplication.viewmodel.UserViewModel

class RestaurantCommentsRepository(val userViewModel: UserViewModel, val shoppingViewModel: ShoppingViewModel) {
    fun restaurantCommentsPagingSource() = RestaurantCommentsPagingSource(RestaurantApi.retrofitService, userViewModel, shoppingViewModel)
}