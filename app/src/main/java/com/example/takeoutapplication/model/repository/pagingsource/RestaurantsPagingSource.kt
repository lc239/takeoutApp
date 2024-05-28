package com.example.takeoutapplication.model.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.takeoutapplication.model.entity.OrderDeliveryView
import com.example.takeoutapplication.model.entity.RestaurantPreview
import com.example.takeoutapplication.network.DeliveryService
import com.example.takeoutapplication.network.RestaurantService
import com.example.takeoutapplication.viewmodel.UserViewModel
import kotlinx.coroutines.flow.first

class RestaurantsPagingSource(val backend: RestaurantService, val userViewModel: UserViewModel) : PagingSource<Int, RestaurantPreview>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RestaurantPreview> {
        val pageOffset = params.key ?: 0
        return try {
            val response = backend.getRestaurantsPage(userViewModel.userInfoManager.bearerAuth.first(), pageOffset, params.loadSize)
            LoadResult.Page(
                data = response.data!!,
                prevKey = null,
                nextKey = if (response.data.size < params.loadSize) null else pageOffset + 1
            )
        }catch (e: Exception){
            println("cuocuocuo" + e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RestaurantPreview>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.nextKey
        }
    }
}