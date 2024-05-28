package com.example.takeoutapplication.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.takeoutapplication.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector? = null){
    object MainPage: Screen("main_page", R.string.main_page, Icons.Filled.Home)
    object SearchPage: Screen("search_page", R.string.search_page)
    object ShoppingPage: Screen("shopping_page", R.string.shopping_page)
    object ShoppingCarDialog: Screen("shoppingcar_dialog", R.string.shoppingcar_dialog)
//    object RestaurantRegisterPage: Screen("restaurant_register_page", R.string.restaurant_register_page)
    object RestaurantManagePage: Screen("restaurant_manage_page", R.string.restaurant_manage_page, Icons.Filled.Restaurant)
    object MinePage: Screen("mine_page", R.string.mine_page, Icons.Filled.Person)
    object MineHistoryOrdersPage: Screen("mine_history_orders_page", R.string.mine_history_orders_page)
    object OrderCommentDialog: Screen("order_comment_dialog", R.string.order_comment_dialog)
    object MineDeliveringOrdersPage: Screen("mine_delivering_orders_page", R.string.mine_delivering_orders_page)
    object DeliveryPage: Screen("delivery_page", R.string.delivery_page, Icons.Filled.DirectionsCar)
}