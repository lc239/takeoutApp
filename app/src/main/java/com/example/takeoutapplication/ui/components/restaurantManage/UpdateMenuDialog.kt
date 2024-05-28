package com.example.takeoutapplication.ui.components.restaurantManage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.takeoutapplication.viewmodel.MainFrameViewModel
import com.example.takeoutapplication.viewmodel.RestaurantViewModel
import kotlinx.coroutines.launch

@Composable
fun UpdateMenuDialog(restaurantViewModel: RestaurantViewModel, mainFrameViewModel: MainFrameViewModel){
    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(400.dp)
        ) {
            TextField(
                modifier = Modifier.padding(4.dp),
                value = restaurantViewModel.updateMenuName,
                onValueChange = {
                    if(it.length <= 20) restaurantViewModel.updateMenuName = it
                },
                label = {
                    Text(text = "菜名")
                }
            )
            TextField(
                modifier = Modifier.padding(4.dp),
                value = restaurantViewModel.updateMenuDesc,
                onValueChange = {
                    if(it.length <= 100) restaurantViewModel.updateMenuDesc = it
                },
                label = {
                    Text(text = "菜品描述")
                }
            )
            TextField(
                modifier = Modifier.padding(4.dp),
                value = restaurantViewModel.updateMenuPrice,
                onValueChange = {
                    if(!(it.isNotEmpty() && it[0] == '0' && it.length > 1 && it[1] != '.')){
                        restaurantViewModel.updateMenuPrice = it
                    }
                },
                label = {
                    Text(text = "菜品价格(x.xx元)")
                }
            )
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(onClick = {
                    restaurantViewModel.deleteMenu()
                    restaurantViewModel.showUpdateMenuDialog = false
                }) {
                    Text(text = "删除")
                }
                Button(
                    onClick = {
                        if(restaurantViewModel.updateMenuName.isEmpty()){
                            scope.launch{
                                keyboard?.hide()
                                mainFrameViewModel.snackbarHostState.showSnackbar(
                                    message = "菜名不能为空",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                        else if(!restaurantViewModel.updateMenuPrice.matches(restaurantViewModel.pricePattern)){
                            scope.launch{
                                mainFrameViewModel.snackbarHostState.showSnackbar(
                                    message = "价格格式应为：xxxx.xx",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }else{
                            restaurantViewModel.updateMenu()
                            restaurantViewModel.showUpdateMenuDialog = false
                        }

                    }
                ) {
                    Text(text = "更新")
                }
                Button(onClick = {
                    restaurantViewModel.showUpdateMenuDialog = false
                }) {
                    Text(text = "取消")
                }
            }
        }
    }
}