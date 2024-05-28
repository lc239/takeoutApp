package com.example.takeoutapplication.ui.components.restaurantManage

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.takeoutapplication.model.entity.Menu
import com.example.takeoutapplication.utils.AliUtil
import com.example.takeoutapplication.utils.fenToYuan
import com.example.takeoutapplication.viewmodel.RestaurantViewModel

@Composable
fun MenuManageCard(restaurantViewModel: RestaurantViewModel, menu: Menu, menuIndex: Int, modifier: Modifier){
    Card(
        modifier = modifier.clickable {
            restaurantViewModel.currentMenuIndex = menuIndex
            restaurantViewModel.updateMenuName = menu.name
            restaurantViewModel.updateMenuDesc = menu.description
            restaurantViewModel.updateMenuPrice = fenToYuan(menu.price)
            restaurantViewModel.updateMenuImageFilename = menu.imageFilename
            restaurantViewModel.showUpdateMenuDialog = true
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        shape = RectangleShape
    ) {
        Row {
            AsyncImage(
                model = AliUtil.nameToUrl(menu.imageFilename),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(2.dp)
                    .aspectRatio(1f)
                    .clickable {
                        restaurantViewModel.currentMenuIndex = menuIndex
                        restaurantViewModel.showUpdateMenuImageDialog = true
                    }
            )
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        text = menu.name
                    )
                    Text(
                        fontSize = 14.sp,
                        text = "${fenToYuan(menu.price)}元"
                    )
                }
                Text(
                    fontSize = 13.sp,
                    overflow = TextOverflow.Ellipsis,
                    text = menu.description,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}