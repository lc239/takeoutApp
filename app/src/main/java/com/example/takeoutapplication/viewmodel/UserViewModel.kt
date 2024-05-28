package com.example.takeoutapplication.viewmodel

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
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
import com.example.takeoutapplication.model.UserInfoManager
import com.example.takeoutapplication.model.entity.Address
import com.example.takeoutapplication.model.entity.MyWebSocketResponse
import com.example.takeoutapplication.model.entity.Order
import com.example.takeoutapplication.model.entity.RestaurantComment
import com.example.takeoutapplication.model.request.LoginRequestBody
import com.example.takeoutapplication.model.entity.Tokens
import com.example.takeoutapplication.model.entity.User
import com.example.takeoutapplication.model.entity.UsernameRequest
import com.example.takeoutapplication.model.repository.HistoryOrdersRepository
import com.example.takeoutapplication.model.request.RegisterUserBody
import com.example.takeoutapplication.model.request.RestaurantCommentForm
import com.example.takeoutapplication.network.RestaurantApi
import com.example.takeoutapplication.network.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

sealed interface LoginUiState{
    object Init: LoginUiState
    data class Success(val tokens: Tokens): LoginUiState
    data class Error(val msg: String): LoginUiState
    object Loading: LoginUiState
}

sealed interface UserInfoUiState{
    data class Success(val user: User): UserInfoUiState
    object Error: UserInfoUiState
    object Loading: UserInfoUiState
}

sealed interface CommentUiState{
    object Success: CommentUiState
    object Error: CommentUiState
    object Loading: CommentUiState
}

sealed interface DeliveringOrdersUiState{
    object Success: DeliveringOrdersUiState
    object Error: DeliveringOrdersUiState
    object Loading: DeliveringOrdersUiState
}

private const val historyOrdersPerPage = 10

class UserViewModel(context: Context): ViewModel(){

    var userInfoManager: UserInfoManager = UserInfoManager(context)
    lateinit var mainFrameViewModel: MainFrameViewModel
    fun showGlobalMsg(msg: String){
        viewModelScope.launch {
            mainFrameViewModel.snackbarHostState.showSnackbar(msg)
        }
    }
    val snackbarHostState = SnackbarHostState()
    val historyOrdersSnackbarHostState = SnackbarHostState()
    fun showMsg(msg: String, duration: SnackbarDuration = SnackbarDuration.Short){
        viewModelScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss() //关闭当前的提示
            snackbarHostState.showSnackbar(message = msg, duration = duration)
        }
    }
    fun showHistoryOrdersMsg(msg: String){
        viewModelScope.launch {
            historyOrdersSnackbarHostState.currentSnackbarData?.dismiss()
            historyOrdersSnackbarHostState.showSnackbar(message = msg)
        }
    }

    val logined = userInfoManager.logined

    var loginUiState: LoginUiState by mutableStateOf(LoginUiState.Init)
        private set
    fun login(loginRequestBody: LoginRequestBody) {
        viewModelScope.launch {
            loginUiState = LoginUiState.Loading
            loginUiState = try {
                val res = UserApi.retrofitService.login(loginRequestBody)
                if(res.code == 0){
                    userInfoManager.saveTokens(res.data!!) //保存token到数据库
                }else{
                    throw Exception(res.message)
                }
                LoginUiState.Success(res.data)
            }catch (e: IOException){
                println(e)
                LoginUiState.Error("网络错误")
            }catch (e: Exception){
                println(e)
                LoginUiState.Error(e.message ?: "")
            }
        }
    }

    fun register(registerUserBody: RegisterUserBody){
        viewModelScope.launch {
            loginUiState = LoginUiState.Loading
            loginUiState = try {
                val res = UserApi.retrofitService.register(registerUserBody)
                if(res.code == 0){
                    userInfoManager.saveTokens(res.data!!)
                }else{
                    throw Exception(res.message)
                }
                LoginUiState.Success(res.data)
            }catch (e: IOException){
                LoginUiState.Error("网络错误")
            }catch (e: Exception){
                LoginUiState.Error(e.message ?: "")
            }
        }
    }

    var userInfoUiState: UserInfoUiState by mutableStateOf(UserInfoUiState.Loading)
        private set
    fun getInfo(){
        viewModelScope.launch {
            userInfoUiState = UserInfoUiState.Loading
            userInfoUiState = try {
                val res = UserApi.retrofitService.getInfo(userInfoManager.bearerAuth.first())
                if(res.code == 0){
                    user = res.data!!
                    openWs()
                }else{
                    throw Exception(res.message)
                }
                UserInfoUiState.Success(user)
            }catch (e: IOException){
                showGlobalMsg("网络错误")
                println(1)
                println(e)
                UserInfoUiState.Error
            }catch (e: Exception){
                println(2)
                println(e)
                logout() //应该在拦截器用
                UserInfoUiState.Error
            }
        }
    }

    var user: User by mutableStateOf(User(0, "username", "", false, false, "", listOf<Address>()))

    fun setSeller(boolean: Boolean){
        user = user.copy(isSeller = boolean)
    }

    fun logout(){
        viewModelScope.launch {
            userInfoManager.clear()
        }
    }

    lateinit var restaurantViewModel: RestaurantViewModel

    fun getNotTakenOrder(orderId: String){
        viewModelScope.launch {
            try {
                val res = RestaurantApi.retrofitService.getNotTakenOrder(
                    userInfoManager.bearerAuth.first(),
                    orderId
                )
                if(res.code == 0){
                    restaurantViewModel.orders[res.data!!] = false
                    mainFrameViewModel.snackbarHostState.showSnackbar("有新订单")
                }
            }catch (e: IOException){
                println(e)
            }catch (e: Exception){
                println(e)
            }
        }
    }

    lateinit var webSocket: WebSocket

    fun openWs(){
        val wsHttpClient = okhttp3.OkHttpClient.Builder().pingInterval(50, TimeUnit.SECONDS).build()
        val request = Request.Builder().url("ws://8.130.174.243/wsp?id=${user.id}").build()
        webSocket = wsHttpClient.newWebSocket(request, object : WebSocketListener(){
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                println("ws open")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val msg = Json.decodeFromString<MyWebSocketResponse>(text)
                when(msg.type){
                    0 -> getNotTakenOrder(msg.data!!)
                }
                println("msg $text")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                println("close $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                println("closed $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                println("failed $response")
            }
        })
    }

    private val historyOrdersRepository = HistoryOrdersRepository(this)
    val historyOrders: Flow<PagingData<Order>> = Pager(
        config = PagingConfig(pageSize = historyOrdersPerPage, enablePlaceholders = false),
        pagingSourceFactory = { historyOrdersRepository.historyOrdersPagingSource() }
    ).flow.cachedIn(viewModelScope)
    var commentingOrder: Order? by mutableStateOf(null)
    var comment by mutableStateOf(RestaurantCommentForm(5, ""))

    var commentUiState: CommentUiState by mutableStateOf(CommentUiState.Success)
    fun commentOrder(onSuccess: () -> Unit = {}){
        val o = commentingOrder
        val c = comment
        println(c)
        viewModelScope.launch {
            commentUiState = CommentUiState.Loading
            commentUiState = try {
                val res = UserApi.retrofitService.commentOrder(
                    userInfoManager.bearerAuth.first(),
                    o!!.orderId!!,
                    c
                )
                if(res.code == 0){
                    o.commentId = res.data!!.id
                    onSuccess()
                }
                CommentUiState.Success
            }catch (e: IOException){
                println(e)
                CommentUiState.Error
            }catch (e: Exception){
                println(e)
                CommentUiState.Error
            }
        }
    }

    var deliveringOrders = mutableStateListOf<Order>()
    var deliveringOrdersUiState: DeliveringOrdersUiState by mutableStateOf(DeliveringOrdersUiState.Loading)
    fun getDeliveringOrders(){
        viewModelScope.launch {
            deliveringOrdersUiState = DeliveringOrdersUiState.Loading
            deliveringOrdersUiState = try {
                val res = UserApi.retrofitService.getDeliveringOrders(
                    userInfoManager.bearerAuth.first(),
                )
                if(res.code == 0){
                    deliveringOrders.clear()
                    deliveringOrders.addAll(res.data!!)
                }
                DeliveringOrdersUiState.Success
            }catch (e: IOException){
                println(e)
                DeliveringOrdersUiState.Error
            }catch (e: Exception){
                println(e)
                DeliveringOrdersUiState.Error
            }
        }
    }

    var showChangeUserAvatarDialog by mutableStateOf(false)
    fun updateAvatar(file: File){
        viewModelScope.launch {
            try {
                snackbarHostState.showSnackbar("正在上传...(根据图片大小时间可能较长)", duration = SnackbarDuration.Short)
                val result = UserApi.retrofitService.uploadAvatar(
                    userInfoManager.bearerAuth.first(),
                    MultipartBody.Part.createFormData("avatar", file.name, file.asRequestBody())
                )
                if(result.code == 0){
                    user = user.copy(avatarFilename = result.data!!)
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

    var usernameUpdating by mutableStateOf(false)
    fun updateUsername(username: String){
        usernameUpdating = true
        viewModelScope.launch {
            try {
                showMsg("修改中，请稍等")
                val result = UserApi.retrofitService.updateUsername(
                    userInfoManager.bearerAuth.first(),
                    UsernameRequest(username)
                )
                if(result.code == 0){
                    showMsg("修改名字成功")
                    user = user.copy(username = username)
                }else{
                    throw Exception(result.message)
                }
            }catch (e: IOException){
                showMsg("网络错误")
            }catch (e: Exception){
                showMsg("修改名字失败")
            }finally {
                usernameUpdating = false
            }
        }
    }
}