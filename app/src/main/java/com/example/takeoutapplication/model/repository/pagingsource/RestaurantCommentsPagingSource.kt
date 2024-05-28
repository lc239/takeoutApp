package com.example.takeoutapplication.model.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.takeoutapplication.model.entity.Order
import com.example.takeoutapplication.model.entity.OrderDeliveryView
import com.example.takeoutapplication.model.entity.RestaurantComment
import com.example.takeoutapplication.network.DeliveryService
import com.example.takeoutapplication.network.RestaurantService
import com.example.takeoutapplication.network.UserService
import com.example.takeoutapplication.viewmodel.RestaurantViewModel
import com.example.takeoutapplication.viewmodel.ShoppingViewModel
import com.example.takeoutapplication.viewmodel.UserViewModel
import kotlinx.coroutines.flow.first

class RestaurantCommentsPagingSource(
    val backend: RestaurantService,
    val userViewModel: UserViewModel,
    val shoppingViewModel: ShoppingViewModel
) : PagingSource<Int, RestaurantComment>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RestaurantComment> {
        val pageOffset = params.key ?: 0
        return try {
            println(shoppingViewModel.id)
            val response = backend.getComments(userViewModel.userInfoManager.bearerAuth.first(), shoppingViewModel.id, pageOffset, params.loadSize)
            LoadResult.Page(
                data = response.data!!,
                prevKey = null,
                nextKey = if(response.data.size < params.loadSize) null else pageOffset + 1
            )
        }catch (e: Exception){
            println("cuocuocuo" + e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RestaurantComment>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.nextKey
        }
    }
}