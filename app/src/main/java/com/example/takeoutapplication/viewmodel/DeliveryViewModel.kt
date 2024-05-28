package com.example.takeoutapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.takeoutapplication.model.repository.WaitingOrderRepository
import com.example.takeoutapplication.model.entity.DeliveryMan
import com.example.takeoutapplication.model.entity.Order
import com.example.takeoutapplication.model.entity.OrderDeliveryView
import com.example.takeoutapplication.network.DeliveryApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException

private const val waitingOrdersSizePerPage = 10

sealed interface DeliveryInfoUiState{
    data class Success(val deliveryMan: DeliveryMan): DeliveryInfoUiState
    object Error: DeliveryInfoUiState
    object Loading: DeliveryInfoUiState
}

class DeliveryViewModel(val userViewModel: UserViewModel): ViewModel(){
    private val waitingOrderRepository = WaitingOrderRepository(userViewModel)
    lateinit var deliveryMan: DeliveryMan
    var deliveringOrders = mutableStateListOf<Order>()
    var waitingOrders: Flow<PagingData<OrderDeliveryView>> = Pager(
        config = PagingConfig(pageSize = waitingOrdersSizePerPage, enablePlaceholders = false),
        pagingSourceFactory = { waitingOrderRepository.waitingOrdersPagingSource() }
    ).flow.cachedIn(viewModelScope)
    var showDeliveringList by mutableStateOf(false)

    fun register(){
        viewModelScope.launch {
            deliveryInfoUiState = DeliveryInfoUiState.Loading
            deliveryInfoUiState = try {
                println(userViewModel.userInfoManager.bearerAuth.first())
                val res = DeliveryApi.retrofitService.register(userViewModel.userInfoManager.bearerAuth.first())
                if(res.code == 0){
                    deliveryMan = res.data!!
                    userViewModel.user = userViewModel.user.copy(isDeliveryMan = true)
                }else{
                    throw Exception(res.message)
                }
                DeliveryInfoUiState.Success(deliveryMan)
            }catch (e: IOException){
                println(e)
                DeliveryInfoUiState.Error
            }catch (e: Exception){
                println(e)
                DeliveryInfoUiState.Error
            }
        }
    }

    fun getInfo(){
        viewModelScope.launch {
            deliveryInfoUiState = DeliveryInfoUiState.Loading
            deliveryInfoUiState = try {
                val res = DeliveryApi.retrofitService.getInfo(userViewModel.userInfoManager.bearerAuth.first())
                if(res.code == 0){
                    deliveryMan = res.data!!
                    getDeliveringOrders()
                }else{
                    throw Exception(res.message)
                }
                DeliveryInfoUiState.Success(deliveryMan)
            }catch (e: IOException){
                println(e)
                DeliveryInfoUiState.Error
            }catch (e: Exception){
                println(e)
                DeliveryInfoUiState.Error
            }
        }
    }

    fun getDeliveringOrders(){
        viewModelScope.launch {
            deliveryInfoUiState = DeliveryInfoUiState.Loading
            deliveryInfoUiState = try {
                val res = DeliveryApi.retrofitService.getDeliveringOrders(userViewModel.userInfoManager.bearerAuth.first())
                if(res.code == 0){
                    deliveringOrders.addAll(res.data!!)
                }else{
                    throw Exception(res.message)
                }
                DeliveryInfoUiState.Success(deliveryMan)
            }catch (e: IOException){
                println(e)
                DeliveryInfoUiState.Error
            }catch (e: Exception){
                println(e)
                DeliveryInfoUiState.Error
            }
        }
    }

    lateinit var takingOrder: OrderDeliveryView
    var showTakeOrderDialog by mutableStateOf(false)
    fun openTakeOrderDialog(order: OrderDeliveryView){
        takingOrder = order
        showTakeOrderDialog = true
    }

    fun takeOrder(){
        viewModelScope.launch {
            try {
                val res = DeliveryApi.retrofitService.takeOrder(
                    userViewModel.userInfoManager.bearerAuth.first(),
                    takingOrder.orderId
                )
                if(res.code == 0){
                    userViewModel.showMsg("接单成功")
                    deliveringOrders.add(res.data!!)
                }else{
                    userViewModel.showMsg("订单已不在")
                    throw Exception(res.message)
                }
            }catch (e: IOException){
                println(e)
            }catch (e: Exception){
                println(e)
            }
        }
    }

    var showOrderDetailDialog by mutableStateOf(false)
    lateinit var detailedOrder: Order
    fun openOrderDetailDialog(order: Order){
        detailedOrder = order
        showOrderDetailDialog = true
    }

    fun completeOrder(){
        viewModelScope.launch {
            try {
                val res = DeliveryApi.retrofitService.completeOrder(
                    userViewModel.userInfoManager.bearerAuth.first(),
                    detailedOrder.orderId!!
                )
                if(res.code == 0){
                    deliveringOrders.remove(detailedOrder)
                    deliveryMan = res.data!!
                    userViewModel.showMsg("确认完成")
                }else{
                    userViewModel.showMsg("出错了")
                    throw Exception(res.message)
                }
            }catch (e: IOException){
                println(e)
            }catch (e: Exception){
                println(e)
            }
        }
    }

    var deliveryInfoUiState: DeliveryInfoUiState by mutableStateOf(DeliveryInfoUiState.Loading)
    init {
        if(userViewModel.user.isDeliveryMan){
            getInfo()
        }
    }
}