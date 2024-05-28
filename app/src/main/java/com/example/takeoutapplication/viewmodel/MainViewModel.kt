package com.example.takeoutapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.takeoutapplication.model.entity.RestaurantPreview
import com.example.takeoutapplication.model.entity.RestaurantSearchView
import com.example.takeoutapplication.model.repository.RestaurantsRepository
import com.example.takeoutapplication.network.RestaurantApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException

private const val restaurantsPerSize = 14

class MainViewModel(val userViewModel: UserViewModel): ViewModel(){
    var searchInput by mutableStateOf("")
    var searchResult by mutableStateOf(listOf<RestaurantSearchView>())
    private var searchJob: Job? = null

    fun search(){
        val size = 5
        val prefix = searchInput
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400) //延迟400ms发送
            try {
                val result = RestaurantApi.retrofitService.searchRestaurants(userViewModel.userInfoManager.bearerAuth.first(), size, prefix)
                if(result.code == 0){
                    searchResult = result.data!!
                }else{
                    throw Exception(result.message)
                }
            }catch (e: IOException){
                println("网络错误")
            }catch (e: Exception){
                println(e)
            }
        }
    }

    private val restaurantsRepository = RestaurantsRepository(userViewModel)
    val recommendRestaurants: Flow<PagingData<RestaurantPreview>> = Pager(
        config = PagingConfig(pageSize = restaurantsPerSize, enablePlaceholders = false),
        pagingSourceFactory = { restaurantsRepository.restaurantsPagingSource() }
    ).flow.cachedIn(viewModelScope)
}