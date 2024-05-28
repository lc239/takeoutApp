package com.example.takeoutapplication.ui.screen.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.takeoutapplication.R
import com.example.takeoutapplication.ui.components.mine.DeliveringOrderCard
import com.example.takeoutapplication.viewmodel.DeliveringOrdersUiState
import com.example.takeoutapplication.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveringOrdersPage(userViewModel: UserViewModel, modifier: Modifier, navController: NavController){
    val orderCardModifier = Modifier.fillMaxWidth().padding(4.dp)
    var composed by remember {
        mutableStateOf(false)
    }
    if(!composed){
        userViewModel.getDeliveringOrders()
        composed = true
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                },
                title = {
                    Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "配送中订单", fontSize = 18.sp, color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            listOf(
                                colorResource(id = R.color.blue_200),
                                colorResource(id = R.color.blue_500)
                            )
                        )
                    )
                    .height(46.dp)
            )
        }
    ){
        if(userViewModel.deliveringOrdersUiState is DeliveringOrdersUiState.Loading){
            Text(text = "加载中", modifier = Modifier.padding(it))
        }else if(userViewModel.deliveringOrdersUiState is DeliveringOrdersUiState.Error){
            Text(text = "网络错误", modifier = Modifier.padding(it))
        }
        else{
            if(userViewModel.deliveringOrders.isEmpty()){
                Text(text = "没有订单正在配送", modifier = Modifier.padding(it))
            }
            LazyColumn(Modifier.padding(it).fillMaxWidth()){
                items(userViewModel.deliveringOrders){ order ->
                    DeliveringOrderCard(order = order, modifier = orderCardModifier, userViewModel = userViewModel)
                }
            }
        }
    }
}