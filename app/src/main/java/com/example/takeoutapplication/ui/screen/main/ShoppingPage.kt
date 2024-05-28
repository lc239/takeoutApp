package com.example.takeoutapplication.ui.screen.main

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.takeoutapplication.R
import com.example.takeoutapplication.model.Screen
import com.example.takeoutapplication.model.entity.RestaurantComment
import com.example.takeoutapplication.ui.components.shopping.AddAddressDialog
import com.example.takeoutapplication.ui.components.shopping.AddressDialog
import com.example.takeoutapplication.ui.components.shopping.CommentCard
import com.example.takeoutapplication.ui.components.shopping.MenuCard
import com.example.takeoutapplication.utils.AliUtil
import com.example.takeoutapplication.viewmodel.ShoppingInfoUiState
import com.example.takeoutapplication.viewmodel.ShoppingViewModel

@Composable
fun ShoppingPage(
    shoppingViewModel: ShoppingViewModel,
    navController: NavController
){
    var commentRefreshed by remember {
        mutableStateOf(false)
    }
    var refreshed by remember {
        mutableStateOf(false)
    }
    if(!refreshed){
        //进来先显示菜单
        refreshed = true
        shoppingViewModel.showComments = false
    }
    val comments = shoppingViewModel.comments.collectAsLazyPagingItems()
    if(shoppingViewModel.showAddressDialog){
        AddressDialog(shoppingViewModel = shoppingViewModel)
    }
    if(shoppingViewModel.showAddAddressDialog){
        AddAddressDialog(shoppingViewModel = shoppingViewModel)
    }
    val avatarModifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .padding(end = 8.dp)
        .size(62.dp)
        .border(
            width = 1.dp,
            color = colorResource(id = R.color.blue_500)
        )
    if (shoppingViewModel.shoppingInfoUiState == ShoppingInfoUiState.Loading) Text(text = "加载中")
    else Column(
        modifier = Modifier.fillMaxSize()
    ) {
        //上方信息栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            AsyncImage(
                model = AliUtil.nameToUrl(shoppingViewModel.restaurant.imageFilename),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.Crop,
                modifier = avatarModifier,
                contentDescription = null
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = shoppingViewModel.restaurant.name)
                Text(
                    text = shoppingViewModel.restaurant.introduction,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(vertical = 6.dp)
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                shoppingViewModel.showComments = !shoppingViewModel.showComments
            }) {
                Text(text = if(shoppingViewModel.showComments) "查看菜单" else "查看评论")
            }
            Button(onClick = {
                navController.navigate(Screen.ShoppingCarDialog.route)
            }) {
                Text(text = "购物车")
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Black
        )
        if(shoppingViewModel.showComments){
            if(!commentRefreshed){
                //不知道为什么放在上层的comments.refresh()没有效果
                commentRefreshed = true
                comments.refresh()
            }
            CommentsPage(comments = comments, modifier = Modifier.fillMaxWidth())
        }else {
            CategoryPage(shoppingViewModel = shoppingViewModel, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun CategoryPage(shoppingViewModel: ShoppingViewModel, modifier: Modifier){
    val menuCardModifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp)
    Row(
        modifier
    ) {
        //左边的分类菜单
        LazyColumn(
            modifier = Modifier.weight(1f)
        ){
            itemsIndexed(shoppingViewModel.restaurant.categories){index, category ->
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        shoppingViewModel.currentCategoryIndex = index
                    },
                    colors = if(index == shoppingViewModel.currentCategoryIndex) ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = Color.Black
                    ) else ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    shape = RectangleShape
                ) {
                    Text(text = category.name)
                }
            }
        }
        VerticalDivider(
            thickness = 1.dp
        )
        //右边的具体菜单
        LazyColumn(
            modifier = Modifier
                .weight(5f)
                .fillMaxHeight()
        ){
            shoppingViewModel.restaurant.categories.let { categories ->
                if(categories.isNotEmpty()){
                    itemsIndexed(categories[shoppingViewModel.currentCategoryIndex].menus){index, menu ->
                        MenuCard(shoppingViewModel, menu = menu, menuIndex = index, modifier = menuCardModifier)
                    }
                }
            }
        }
    }
}

@Composable
fun CommentsPage(comments: LazyPagingItems<RestaurantComment>, modifier: Modifier){
    val commentModifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    LazyColumn(modifier){
        items(comments.itemCount){ index ->
            CommentCard(comment = comments[index]!!, modifier = commentModifier)
        }
        if(comments.loadState.append.endOfPaginationReached){
            item {
                Text(text = "没有更多评论了")
            }
        }
    }
}