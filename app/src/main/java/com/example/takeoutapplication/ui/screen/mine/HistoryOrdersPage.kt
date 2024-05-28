package com.example.takeoutapplication.ui.screen.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.takeoutapplication.R
import com.example.takeoutapplication.model.Screen
import com.example.takeoutapplication.model.entity.Order
import com.example.takeoutapplication.ui.components.mine.HistoryOrderCard
import com.example.takeoutapplication.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryOrdersPage(userViewModel: UserViewModel, modifier: Modifier, navController: NavController){
    val historyOrders = userViewModel.historyOrders.collectAsLazyPagingItems()
    LaunchedEffect(true){
        historyOrders.refresh()
    }
    val historyCardModifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 6.dp)
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
                        Text(text = "历史订单", fontSize = 18.sp, color = Color.White)
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
        },
        snackbarHost = {
            SnackbarHost(userViewModel.historyOrdersSnackbarHostState)
        }
    ) {
        LazyColumn(
            Modifier
                .background(colorResource(id = R.color.grey_background))
                .padding(it)
                .padding(horizontal = 6.dp, vertical = 4.dp)
                .fillMaxSize()
                ) {
            items(historyOrders.itemCount) {
                HistoryOrderCard(
                    order = historyOrders[it]!!,
                    modifier = historyCardModifier,
                    userViewModel = userViewModel,
                    footer = {
                        key(historyOrders[it]!!.commentId) {
                            if (historyOrders[it]!!.commentId == null) {
                                Button(onClick = {
                                    userViewModel.commentingOrder = historyOrders[it]
                                    navController.navigate(Screen.OrderCommentDialog.route)
                                }) {
                                    Text(text = "评论")
                                }
                            }
                        }
                    }
                ) {
                    //展示详细信息
                }
            }
            if (historyOrders.loadState.append.endOfPaginationReached) {
                item {
                    Text(text = "没有更多订单了")
                }
            }
        }
    }
}