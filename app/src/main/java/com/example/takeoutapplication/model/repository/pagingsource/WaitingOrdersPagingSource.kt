package com.example.takeoutapplication.model.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.takeoutapplication.model.entity.OrderDeliveryView
import com.example.takeoutapplication.network.DeliveryService
import com.example.takeoutapplication.viewmodel.UserViewModel
import kotlinx.coroutines.flow.first

class WaitingOrdersPagingSource(val backend: DeliveryService, val userViewModel: UserViewModel) : PagingSource<Int, OrderDeliveryView>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderDeliveryView> {
        val fromIndex = params.key ?: 0
        return try {
            println("form ${fromIndex} size ${params.loadSize}")
            val response = backend.getOrders(userViewModel.userInfoManager.bearerAuth.first(), fromIndex, params.loadSize)
            println("m+  " + response.message + response.data!!.size)
            LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = if(response.data.size < params.loadSize) null else fromIndex + params.loadSize
            )
        }catch (e: Exception){
            println("cuocuocuo" + e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, OrderDeliveryView>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it) //最新添加的页
            anchorPage?.nextKey
        }
    }
}