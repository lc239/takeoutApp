package com.example.takeoutapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.takeoutapplication.model.Screen
import com.example.takeoutapplication.ui.screen.main.ShoppingCarDialog
import com.example.takeoutapplication.ui.screen.main.MainPage
import com.example.takeoutapplication.ui.screen.main.SearchPage
import com.example.takeoutapplication.ui.screen.main.ShoppingPage
import com.example.takeoutapplication.ui.screen.mine.CommentDialog
import com.example.takeoutapplication.ui.screen.mine.DeliveringOrdersPage
import com.example.takeoutapplication.ui.screen.mine.HistoryOrdersPage
import com.example.takeoutapplication.ui.screen.mine.MinePage
import com.example.takeoutapplication.viewmodel.DeliveryViewModel
import com.example.takeoutapplication.viewmodel.MainFrameViewModel
import com.example.takeoutapplication.viewmodel.MainViewModel
import com.example.takeoutapplication.viewmodel.RestaurantInfoUiState
import com.example.takeoutapplication.viewmodel.RestaurantRegisterUiState
import com.example.takeoutapplication.viewmodel.RestaurantViewModel
import com.example.takeoutapplication.viewmodel.ShoppingViewModel
import com.example.takeoutapplication.viewmodel.UserInfoUiState
import com.example.takeoutapplication.viewmodel.UserViewModel

@Composable
fun MainFrame(
    userViewModel: UserViewModel,
    mainFrameViewModel: MainFrameViewModel
){
    val navController = rememberNavController()
    val bottomNavPages: List<Screen>
    val startDes: String

    if(userViewModel.user.isSeller){
        bottomNavPages = listOf(Screen.RestaurantManagePage, Screen.MinePage)
        startDes = Screen.RestaurantManagePage.route
    }else if(userViewModel.user.isDeliveryMan){
        bottomNavPages = listOf(Screen.DeliveryPage, Screen.MinePage)
        startDes = Screen.DeliveryPage.route
    }else{
        bottomNavPages = listOf(Screen.MainPage, Screen.MinePage)
        startDes = "main"
    }
    //获取用户基本信息
    if(userViewModel.userInfoUiState is UserInfoUiState.Success)
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavPages.forEach{ screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any{it.route == screen.route} == true,
                        onClick = {
                            navController.navigate(screen.route)
                        },
                        icon = {
                            screen.icon?.let {
                                Icon(
                                    imageVector = it,
                                    contentDescription = null
                                )
                            }
                        },
                        label ={
                            Text(text = stringResource(id = screen.resourceId))
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(mainFrameViewModel.snackbarHostState)
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = startDes, modifier = Modifier.padding(innerPadding)){
            val restaurantViewModel = RestaurantViewModel(userViewModel)
            userViewModel.restaurantViewModel =  restaurantViewModel
            val mainViewModel = MainViewModel(userViewModel)
            val shoppingViewModel = ShoppingViewModel(userViewModel)
            val deliveryViewModel by lazy { DeliveryViewModel(userViewModel) }
            navigation(startDestination = Screen.MainPage.route, route = "main"){
                composable(Screen.MainPage.route){
                    MainPage(navController = navController, shoppingViewModel = shoppingViewModel, mainViewModel = mainViewModel)
                }
                composable(Screen.SearchPage.route){
                    SearchPage(
                        mainViewModel,
                        shoppingViewModel,
                        navController
                    )
                }
                composable(Screen.ShoppingPage.route){ ShoppingPage(shoppingViewModel, navController) }
                dialog(Screen.ShoppingCarDialog.route){
                    ShoppingCarDialog(shoppingViewModel = shoppingViewModel, navController = navController)
                }
            }

            composable(Screen.RestaurantManagePage.route){
                LaunchedEffect(key1 = userViewModel.userInfoUiState){
                    restaurantViewModel.getInfo()
                }
                if(userViewModel.userInfoUiState is UserInfoUiState.Success){
                    RestaurantPage(restaurantViewModel = restaurantViewModel, mainFrameViewModel = mainFrameViewModel)
                }else{
                    Text(text = "加载中")
                }
            }

            navigation(startDestination = Screen.MinePage.route, route = "mine"){
                composable(Screen.MinePage.route){
                    MinePage(
                        userViewModel,
                        Modifier
                            .background(Color(0x11333333))
                            .fillMaxSize(),
                        navController
                    )
                }
                composable(Screen.MineHistoryOrdersPage.route){
                    HistoryOrdersPage(
                        userViewModel = userViewModel,
                        modifier = Modifier.fillMaxSize(),
                        navController = navController
                    )
                }
                composable(Screen.MineDeliveringOrdersPage.route){
                    DeliveringOrdersPage(
                        userViewModel = userViewModel,
                        modifier = Modifier.fillMaxSize(),
                        navController = navController
                    )
                }
                dialog(Screen.OrderCommentDialog.route){
                    CommentDialog(userViewModel = userViewModel, navController = navController)
                }
            }

            composable(Screen.DeliveryPage.route){
                if(userViewModel.user.isDeliveryMan) DeliveryPage(deliveryViewModel)
                else DeliveryRegisterPage(deliveryViewModel)
            }
        }

    }
    else Text(text = "加载中")
}