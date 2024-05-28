package com.example.takeoutapplication.ui.components.restaurantManage

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.takeoutapplication.viewmodel.RestaurantViewModel

@Composable
fun MenuManageView(restaurantViewModel: RestaurantViewModel, modifier: Modifier){
    val menuCardModifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp)
    Row(modifier) {
        //左边的分类菜单
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(restaurantViewModel.restaurant.categories) { index, category ->
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        restaurantViewModel.currentCategoryIndex = index
                    },
                    colors = if (index == restaurantViewModel.currentCategoryIndex) ButtonDefaults.buttonColors(
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
            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        restaurantViewModel.showAddCategoryDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "添加分类",
                        tint = Color.Black
                    )
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
        ) {
            restaurantViewModel.restaurant.categories.let { categories ->
                if (categories.isNotEmpty()) {
                    itemsIndexed(categories[restaurantViewModel.currentCategoryIndex].menus) { index, menu ->
                        MenuManageCard(
                            restaurantViewModel,
                            menu = menu,
                            menuIndex = index,
                            modifier = menuCardModifier
                        )
                    }
                    item {
                        TextButton(
                            onClick = {
                                restaurantViewModel.showConfirmDeleteCategoryDialog = true
                            }
                        ) {
                            Text(text = "删除此分类")
                        }
                    }
                }
            }
        }
    }
}