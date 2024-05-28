package com.example.takeoutapplication.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.takeoutapplication.model.entity.Address
import com.example.takeoutapplication.model.entity.Menu
import com.example.takeoutapplication.model.entity.Order
import com.example.takeoutapplication.model.entity.OrderedMenu
import com.example.takeoutapplication.model.entity.Restaurant
import com.example.takeoutapplication.model.entity.RestaurantComment
import com.example.takeoutapplication.model.repository.HistoryOrdersRepository
import com.example.takeoutapplication.model.repository.RestaurantCommentsRepository
import com.example.takeoutapplication.network.RestaurantApi
import com.example.takeoutapplication.network.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface ShoppingInfoUiState{
    data object Loading: ShoppingInfoUiState
    data class Success(val restaurant: Restaurant): ShoppingInfoUiState
    data class Error(val msg: String): ShoppingInfoUiState
}

sealed interface OrderUiState{
    data object Init: OrderUiState
    data object Loading: OrderUiState
    data object Success: OrderUiState
    data class Error(val msg: String): OrderUiState
}

sealed interface AddressUiState{
    data object Init: AddressUiState
    data object Loading: AddressUiState
    data object Success: AddressUiState
    data class Error(val msg: String): AddressUiState
}

private const val commentsPerPage = 20

class ShoppingViewModel(val userViewModel: UserViewModel): ViewModel(){
    var currentCategoryIndex by mutableIntStateOf(0)
    var shoppingInfoUiState: ShoppingInfoUiState by mutableStateOf(ShoppingInfoUiState.Loading)
    lateinit var restaurant: Restaurant
    var id: Long = 0

    fun init(id: Long){
        this.id = id
        showMenus = true
        shoppingCar.clear()
        if(addresses.isEmpty()) addresses.addAll(userViewModel.user.addresses)
        getRestaurantInfo(id)
    }

    private fun getRestaurantInfo(id: Long){
        viewModelScope.launch {
            shoppingInfoUiState = ShoppingInfoUiState.Loading
            shoppingInfoUiState = try {
                val result = RestaurantApi.retrofitService.getByRestaurantId(
                    userViewModel.userInfoManager.bearerAuth.first(),
                    id
                )
                if(result.code == 0){
                    restaurant = result.data!!
                }else{
                    throw Exception(result.message)
                }
                ShoppingInfoUiState.Success(result.data)
            }catch (e: IOException){
                ShoppingInfoUiState.Error("网络错误")
            }catch (e: Exception){
                ShoppingInfoUiState.Error(e.message ?: "")
            }
        }
    }

    var showMenus by mutableStateOf(true)
    var shoppingCar = mutableStateMapOf<Menu, Int>()

    var newAddress: Address by mutableStateOf(Address("", "", ""))
    var showAddressDialog by mutableStateOf(false)
    var showAddAddressDialog by mutableStateOf(false)
    var addresses = mutableStateListOf<Address>()
    var selectedAddress: Address? by mutableStateOf(null)
    var addressUiState: AddressUiState by mutableStateOf(AddressUiState.Init)
    fun addAddress(){
        viewModelScope.launch {
            addressUiState = AddressUiState.Loading
            addressUiState = try {
                val result = UserApi.retrofitService.addAddress(
                    userViewModel.userInfoManager.bearerAuth.first(),
                    newAddress
                )
                if(result.code == 0){
                    addresses.add(newAddress)
                }else{
                    throw Exception(result.message)
                }
                AddressUiState.Success
            }catch (e: IOException){
                AddressUiState.Error("网络错误")
            }catch (e: Exception){
                AddressUiState.Error(e.message ?: "")
            }
        }
    }

    var orderUiState: OrderUiState by mutableStateOf(OrderUiState.Init)
    fun putOrder(){
        if(selectedAddress == null) return
        val order = Order(
            menus = shoppingCar.toList().map {
                OrderedMenu(
                    findMenuCategoryIndex(it.first),
                    it.first.name,
                    it.second,
                    it.first.price
                )
            },
            address = selectedAddress!!,
            price = totalPrice()
        )
        viewModelScope.launch {
            orderUiState = OrderUiState.Loading
            orderUiState = try {
                val result = UserApi.retrofitService.putOrder(
                    userViewModel.userInfoManager.bearerAuth.first(),
                    id,
                    order
                )
                if(result.code == 0){
                    shoppingCar.clear()
                    userViewModel.showGlobalMsg("下单成功")
                }else{
                    throw Exception(result.message)
                }
                OrderUiState.Success
            }catch (e: IOException){
                OrderUiState.Error("网络错误")
            }catch (e: Exception){
                OrderUiState.Error(e.message ?: "")
            }
        }
    }

    private fun findMenuCategoryIndex(menu: Menu): Int{
        for ((index, e) in restaurant.categories.withIndex()){
            if(e.menus.contains(menu)) return index
        }
        return -1
    }
    fun totalPrice(): Long{
        var res = 0L
        shoppingCar.forEach{
            res += it.key.price * it.value
        }
        return res
    }

    var showComments by mutableStateOf(false)
    private val restaurantCommentsRepository = RestaurantCommentsRepository(this.userViewModel, this)
    val comments: Flow<PagingData<RestaurantComment>> = Pager(
        config = PagingConfig(pageSize = commentsPerPage, enablePlaceholders = false),
        pagingSourceFactory = { restaurantCommentsRepository.restaurantCommentsPagingSource() }
    ).flow.cachedIn(viewModelScope)
}