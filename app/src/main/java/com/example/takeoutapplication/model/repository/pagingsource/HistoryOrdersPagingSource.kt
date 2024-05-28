package com.example.takeoutapplication.model.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.takeoutapplication.model.entity.Order
import com.example.takeoutapplication.model.entity.OrderDeliveryView
import com.example.takeoutapplication.network.DeliveryService
import com.example.takeoutapplication.network.UserService
import com.example.takeoutapplication.viewmodel.UserViewModel
import kotlinx.coroutines.flow.first

class HistoryOrdersPagingSource(val backend: UserService, val userViewModel: UserViewModel) : PagingSource<Int, Order>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        val pageOffset = params.key ?: 0
        return try {
            val response = backend.getHistoryOrders(userViewModel.userInfoManager.bearerAuth.first(), pageOffset, params.loadSize)
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

    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.nextKey
        }
    }
}