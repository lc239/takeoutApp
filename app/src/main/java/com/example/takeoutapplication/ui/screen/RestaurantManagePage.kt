package com.example.takeoutapplication.ui.screen

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.takeoutapplication.R
import com.example.takeoutapplication.ui.components.restaurantManage.AddCategoryDialog
import com.example.takeoutapplication.ui.components.restaurantManage.AddMenuDialog
import com.example.takeoutapplication.ui.components.restaurantManage.MenuManageCard
import com.example.takeoutapplication.ui.components.restaurantManage.MenuManageView
import com.example.takeoutapplication.ui.components.restaurantManage.TakeOrderDialog
import com.example.takeoutapplication.ui.components.restaurantManage.TakeOrdersView
import com.example.takeoutapplication.ui.components.restaurantManage.UpdateMenuDialog
import com.example.takeoutapplication.utils.AliUtil
import com.example.takeoutapplication.utils.uriToFile
import com.example.takeoutapplication.viewmodel.MainFrameViewModel
import com.example.takeoutapplication.viewmodel.RestaurantViewModel
import java.io.File
import java.net.URI


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantManagePage(restaurantViewModel: RestaurantViewModel, mainFrameViewModel: MainFrameViewModel){
    val context = LocalContext.current
    val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE

    //权限申请
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult ={
            if(!it) restaurantViewModel.showUpdateMenuImageDialog = false
        }
    )
    if(restaurantViewModel.showAddCategoryDialog){
        AddCategoryDialog(restaurantViewModel = restaurantViewModel)
    }
    if(restaurantViewModel.showAddMenuDialog){
        AddMenuDialog(restaurantViewModel = restaurantViewModel, mainFrameViewModel = mainFrameViewModel)
    }
    if(restaurantViewModel.showUpdateMenuDialog){
        UpdateMenuDialog(restaurantViewModel = restaurantViewModel, mainFrameViewModel = mainFrameViewModel)
    }
    if(restaurantViewModel.showOrderDialog){
        TakeOrderDialog(restaurantViewModel)
    }
    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                val file = uriToFile(context, it)
                if(file != null) restaurantViewModel.updateMenuImage(file)
                else println(111)
            }
        })
    if(restaurantViewModel.showUpdateMenuImageDialog){
        if(context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED){
            println(1)
        }else{
            launcher.launch(permission)
            println(2)
        }

        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            title = {
                Text(
                    fontSize = 18.sp,
                    text = "确定更改此菜品的图片吗？"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    selectImageLauncher.launch("image/*") //选取图片
                    restaurantViewModel.showUpdateMenuImageDialog = false
                }) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    restaurantViewModel.showUpdateMenuImageDialog = false
                }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }
    if(restaurantViewModel.showConfirmDeleteCategoryDialog){
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            title = {
                Text(
                    fontSize = 18.sp,
                    text = "确定删除？"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    restaurantViewModel.deleteCategory()
                    restaurantViewModel.showConfirmDeleteCategoryDialog = false
                }) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    restaurantViewModel.showConfirmDeleteCategoryDialog = false
                }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("店铺管理")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            if(restaurantViewModel.restaurant.categories.isNotEmpty()){
                FloatingActionButton(onClick = {
                    restaurantViewModel.showAddMenuDialog = true
                }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "添加菜品")
                }
            }
        },
        snackbarHost = {
            SnackbarHost(restaurantViewModel.snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(
                Modifier.padding(10.dp)
            ){
                val restaurantImageModifier = Modifier
                    .padding(end = 8.dp)
                    .size(62.dp)
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.blue_500)
                    )
                AsyncImage(
                    model = AliUtil.nameToUrl(restaurantViewModel.restaurant.imageFilename),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    contentDescription = null,
                    modifier = restaurantImageModifier
                )
                Text(
                    text = restaurantViewModel.restaurant.name,
                    modifier = Modifier.align(Alignment.Bottom)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Button(
                            onClick = {
                                restaurantViewModel.showOrders = !restaurantViewModel.showOrders
                            },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(top = 4.dp)
                        ) {
                            Text(text = if(restaurantViewModel.showOrders) "管理菜单" else "待接订单")
                        }
                        if(restaurantViewModel.orders.isNotEmpty() && !restaurantViewModel.showOrders){
                            Text(
                                text = restaurantViewModel.orders.size.toString(),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(22.dp)
                                    .background(Color.Red)
                                    .align(Alignment.TopEnd),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Black
            )
            if(restaurantViewModel.showOrders){
                TakeOrdersView(
                    modifier = Modifier.fillMaxSize(),
                    restaurantViewModel = restaurantViewModel
                )
            }else{
                MenuManageView(
                    restaurantViewModel = restaurantViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}