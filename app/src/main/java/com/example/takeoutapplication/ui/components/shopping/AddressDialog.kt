package com.example.takeoutapplication.ui.components.shopping

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.takeoutapplication.R
import com.example.takeoutapplication.viewmodel.ShoppingViewModel

@Composable
fun AddressDialog(shoppingViewModel: ShoppingViewModel){
    val addressCardModifier = Modifier
        .padding(4.dp)
        .fillMaxWidth()
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp)
        ) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ){
                items(shoppingViewModel.addresses){
                    AddressCard(
                        address = it,
                        modifier = addressCardModifier,
                        colors = if(it == shoppingViewModel.selectedAddress) CardDefaults.cardColors(containerColor = colorResource(
                            id = R.color.taken_green
                        ))
                        else CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        onClick = {
                            shoppingViewModel.selectedAddress = it
                            shoppingViewModel.showAddressDialog = false
                        }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    shoppingViewModel.showAddAddressDialog = true
                }) {
                    Text(text = "添加地址")
                }
                Button(onClick = {
                    shoppingViewModel.showAddressDialog = false
                }) {
                    Text(text = "关闭")
                }
            }
        }
    }
}