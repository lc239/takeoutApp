package com.example.takeoutapplication.ui.components.shopping

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.takeoutapplication.R
import com.example.takeoutapplication.model.entity.Menu
import com.example.takeoutapplication.utils.fenToYuan
import com.example.takeoutapplication.viewmodel.ShoppingViewModel

@Composable
fun OrderedMenuCard(shoppingViewModel: ShoppingViewModel, menu: Menu, modifier: Modifier){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = shoppingViewModel.shoppingCar[menu].toString())
            IconButton(
                onClick = {
                    if(shoppingViewModel.shoppingCar.getOrDefault(menu, 0) <= 1) shoppingViewModel.shoppingCar.remove(menu)
                    else shoppingViewModel.shoppingCar[menu] = shoppingViewModel.shoppingCar[menu]!! - 1
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.padding(horizontal = 6.dp).size(30.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.minus), contentDescription = null)
            }
            IconButton(
                onClick = {
                    shoppingViewModel.shoppingCar[menu] = shoppingViewModel.shoppingCar.getOrDefault(menu, 0) + 1
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.size(30.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    }
}