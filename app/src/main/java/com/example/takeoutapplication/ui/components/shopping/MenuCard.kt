package com.example.takeoutapplication.ui.components.shopping

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.takeoutapplication.R
import com.example.takeoutapplication.model.entity.Menu
import com.example.takeoutapplication.utils.AliUtil
import com.example.takeoutapplication.utils.fenToYuan
import com.example.takeoutapplication.viewmodel.RestaurantViewModel
import com.example.takeoutapplication.viewmodel.ShoppingViewModel

@Composable
fun MenuCard(shoppingViewModel: ShoppingViewModel, menu: Menu, menuIndex: Int, modifier: Modifier){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            Modifier.padding(4.dp).fillMaxSize()
        ) {
            AsyncImage(
                model = AliUtil.nameToUrl(menu.imageFilename),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(2.dp)
                    .aspectRatio(1f)
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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = menu.description,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(shoppingViewModel.shoppingCar[menu] != null){
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
    }
}