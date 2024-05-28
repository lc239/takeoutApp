package com.example.takeoutapplication.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.takeoutapplication.model.entity.Category
import com.example.takeoutapplication.model.entity.Menu
import com.example.takeoutapplication.model.entity.Order
import com.example.takeoutapplication.model.entity.OrderDeliveryView
import com.example.takeoutapplication.model.entity.Restaurant
import com.example.takeoutapplication.model.entity.RestaurantPreview
import com.example.takeoutapplication.model.repository.RestaurantsRepository
import com.example.takeoutapplication.model.request.MenuRequestBody
import com.example.takeoutapplication.model.request.RestaurantRegister
import com.example.takeoutapplication.network.RestaurantApi
import com.example.takeoutapplication.utils.yuanToFen
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException

sealed interface RestaurantInfoUiState{
    object Loading: RestaurantInfoUiState
    data class Success(val restaurant: Restaurant): RestaurantInfoUiState
    data class Error(val msg: String): RestaurantInfoUiState
}

sealed interface RestaurantRegisterUiState{
    object Init: RestaurantRegisterUiState
    data class Success(val restaurant: Restaurant): RestaurantRegisterUiState
    data class Error(val msg: String): RestaurantRegisterUiState
    object Loading: RestaurantRegisterUiState
}

class RestaurantViewModel(val userViewModel: UserViewModel): ViewModel(){

    var restaurant by mutableStateOf(Restaurant(
        0,
        "restaurantName",
        "",
        0,
        0,
        0,
        "",
        listOf(),
        0
    ))

    var snackbarHostState = SnackbarHostState()
    var currentCategoryIndex by mutableIntStateOf(0)
    var showAddCategoryDialog by mutableStateOf(false)
    var newCategoryName by mutableStateOf("")
    var showAddMenuDialog by mutableStateOf(false)
    var newMenuName by mutableStateOf("")
    var newMenuDescription by mutableStateOf("")
    var newMenuPrice by mutableStateOf("0.00")
    val pricePattern = Regex("^\\d{1,4}\\.\\d{2}$")
    var currentMenuIndex by mutableIntStateOf(0)
    var showUpdateMenuDialog by mutableStateOf(false)
    var updateMenuName by mutableStateOf("")
    var updateMenuDesc by mutableStateOf("")
    var updateMenuPrice by mutableStateOf("0.00")
    var updateMenuImageFilename by mutableStateOf("")
    var showUpdateMenuImageDialog by mutableStateOf(false)
    var showConfirmDeleteCategoryDialog by mutableStateOf(false)

    var restaurantRegisterUiState: RestaurantRegisterUiState by mutableStateOf(RestaurantRegisterUiState.Init)
    fun register(restaurantRegister: RestaurantRegister){
        viewModelScope.launch {
            restaurantRegisterUiState = RestaurantRegisterUiState.Loading
            restaurantRegisterUiState = try {
                val result = RestaurantApi.retrofitService.register(userViewModel.userInfoManager.bearerAuth.first(), restaurantRegister)
                if(result.code == 0){
                    restaurant = result.data!!
                }else{
                    throw Exception(result.message)
                }
                restaurantInfoUiState = RestaurantInfoUiState.Success(restaurant)
                RestaurantRegisterUiState.Success(result.data)
            }catch (e: IOException){
                RestaurantRegisterUiState.Error("网络错误")
            }catch (e: Exception){
                RestaurantRegisterUiState.Error(e.message ?: "")
            }
        }
    }

    var restaurantInfoUiState: RestaurantInfoUiState by mutableStateOf(RestaurantInfoUiState.Loading)
        private set
    fun getInfo(){
        viewModelScope.launch {
            restaurantInfoUiState = RestaurantInfoUiState.Loading
            restaurantInfoUiState = try {
                val result = RestaurantApi.retrofitService.getInfo(userViewModel.userInfoManager.bearerAuth.first())
                if(result.code == 0){
                    restaurant = result.data!!
                }else{
                    throw Exception(result.message)
                }
                RestaurantInfoUiState.Success(restaurant)
            }catch (e: IOException){
                RestaurantInfoUiState.Error("网络错误")
            }catch (e: Exception){
                RestaurantInfoUiState.Error(e.message ?: "")
            }
        }
    }

    fun addCategory(){
        viewModelScope.launch {
            try {
                val result = RestaurantApi.retrofitService.addCategory(userViewModel.userInfoManager.bearerAuth.first(), newCategoryName)
                if(result.code == 0){
                    val newCategories = restaurant.categories.toMutableList()
                    newCategories.add(Category(newCategoryName, listOf()))
                    restaurant = restaurant.copy(categories = newCategories.toList())
                    currentCategoryIndex = restaurant.categories.size - 1 //添加后前往新增分类
                }else{
                    throw Exception(result.message)
                }
            }catch (e: IOException){
                println(e)
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun deleteCategory(){
        viewModelScope.launch {
            try {
                val index = currentCategoryIndex
                val result = RestaurantApi.retrofitService.deleteCategory(userViewModel.userInfoManager.bearerAuth.first(), index)
                if(result.code == 0){
                    val newCategories = restaurant.categories.toMutableList()
                    currentCategoryIndex = 0 //删除后回到第一个分类
                    newCategories.removeAt(index)
                    restaurant = restaurant.copy(categories = newCategories.toList())
                }else{
                    throw Exception(result.message)
                }
            }catch (e: IOException){
                println(e)
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun addMenu(){
        viewModelScope.launch {
            try {
                println(newMenuName)
                println(yuanToFen(newMenuPrice))
                val result = RestaurantApi.retrofitService.addMenu(
                    userViewModel.userInfoManager.bearerAuth.first(),
                    currentCategoryIndex,
                    MenuRequestBody(newMenuName, newMenuDescription, yuanToFen(newMenuPrice))
                )
                if(result.code == 0){
                    val newCategories = restaurant.categories.toMutableList()
                    val newMenus = newCategories[currentCategoryIndex].menus.toMutableList()
                    newMenus.add(result.data!!)
                    newCategories[currentCategoryIndex] = newCategories[currentCategoryIndex].copy(menus = newMenus)
                    restaurant = restaurant.copy(categories = newCategories.toList())
                }else{
                    throw Exception(result.message)
                }
            }catch (e: IOException){
                println(e)
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun updateMenu(){
        viewModelScope.launch {
            try {
                val categoryIndex = currentCategoryIndex
                val menuIndex = currentMenuIndex
                val updatedMenu = Menu(updateMenuName, updateMenuDesc, yuanToFen(updateMenuPrice), updateMenuImageFilename)
                val result = RestaurantApi.retrofitService.updateMenu(
                    userViewModel.userInfoManager.bearerAuth.first(),
                    categoryIndex,
                    menuIndex,
                    updatedMenu
                )
                if(result.code == 0){
                    val newCategories = restaurant.categories.toMutableList()
                    val newMenus = newCategories[categoryIndex].menus.toMutableList()
                    newMenus[menuIndex] = updatedMenu
                    newCategories[categoryIndex] = newCategories[categoryIndex].copy(menus = newMenus)
                    restaurant = restaurant.copy(categories = newCategories.toList())
                }else{
                    throw Exception(result.message)
                }
            }catch (e: IOException){
                println(e)
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun deleteMenu(){
        viewModelScope.launch {
            try {
                val categoryIndex = currentCategoryIndex
                val menuIndex = currentMenuIndex
                val result = RestaurantApi.retrofitService.deleteMenu(
                    userViewModel.userInfoManager.bearerAuth.first(),
                    categoryIndex,
                    menuIndex
                )
                if(result.code == 0){
                    val newCategories = restaurant.categories.toMutableList()
                    val newMenus = newCategories[categoryIndex].menus.toMutableList()
                    newMenus.removeAt(menuIndex)
                    newCategories[categoryIndex] = newCategories[categoryIndex].copy(menus = newMenus)
                    restaurant = restaurant.copy(categories = newCategories.toList())
                }else{
                    throw Exception(result.message)
                }
            }catch (e: IOException){
                println(e)
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun updateMenuImage(file: File){
        viewModelScope.launch {
            try {
                val categoryIndex = currentCategoryIndex
                val menuIndex = currentMenuIndex
                snackbarHostState.showSnackbar("正在上传...(根据图片大小时间可能较长)", duration = SnackbarDuration.Short)
                val result = RestaurantApi.retrofitService.updateMenuImage(
                    userViewModel.userInfoManager.bearerAuth.first(),
                    categoryIndex,
                    menuIndex,
                    MultipartBody.Part.createFormData("image", file.name, file.asRequestBody())
                )
                if(result.code == 0){
                    val newCategories = restaurant.categories.toMutableList()
                    val newMenus = newCategories[currentCategoryIndex].menus.toMutableList()
                    newMenus[menuIndex] = newMenus[menuIndex].copy(imageFilename = result.data!!)
                    newCategories[currentCategoryIndex] = newCategories[currentCategoryIndex].copy(menus = newMenus)
                    restaurant = restaurant.copy(categories = newCategories.toList())
                }else{
                    throw Exception(result.message)
                }
            }catch (e: IOException){
                snackbarHostState.showSnackbar("网络错误", duration = SnackbarDuration.Short)
            }catch (e: Exception){
                snackbarHostState.showSnackbar("出错了，请检查文件格式", duration = SnackbarDuration.Short)
            }
        }
    }

    val orders = mutableStateMapOf<Order, Boolean>()
    var showOrders by mutableStateOf(false)
    lateinit var orderProcessing: Order
    fun openOrderDialog(order: Order){
        orderProcessing = order
        showOrderDialog = true
    }
    var showOrderDialog by mutableStateOf(false)

    fun takeOrder(order: Order){
        viewModelScope.launch {
            try {
                val result = RestaurantApi.retrofitService.takeOrder(
                    userViewModel.userInfoManager.bearerAuth.first(),
                    order.orderId!!
                )
                if(result.code == 0){
                    orders[order] = true
                    userViewModel.showMsg("接单成功")
                }else{
                    userViewModel.showMsg("接单失败")
                    orders.remove(order)
                    throw Exception(result.message)
                }
            }catch (e: IOException){
                println(e)
            }catch (e: Exception){
                println(e)
            }
        }
    }

    fun rejectOrder(order: Order){
        viewModelScope.launch {
            try {
                val result = RestaurantApi.retrofitService.rejectOrder(
                    userViewModel.userInfoManager.bearerAuth.first(),
                    order.orderId!!
                )
                if(result.code == 0){
                    orders.remove(order)
                }else{
                    throw Exception(result.message)
                }
            }catch (e: IOException){
                println(e)
            }catch (e: Exception){
                println(e)
            }
        }
    }
}