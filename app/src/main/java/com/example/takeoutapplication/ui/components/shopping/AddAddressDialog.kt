package com.example.takeoutapplication.ui.components.shopping

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.takeoutapplication.utils.PatternUtils
import com.example.takeoutapplication.viewmodel.ShoppingViewModel

@Composable
fun AddAddressDialog(shoppingViewModel: ShoppingViewModel){
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(400.dp)
        ) {
            TextField(
                modifier = Modifier.padding(10.dp),
                value = shoppingViewModel.newAddress.name,
                onValueChange = {
                    if(it.length <= 4) shoppingViewModel.newAddress = shoppingViewModel.newAddress.copy(name = it)
                },
                label = {
                    Text(text = "姓名")
                }
            )
            TextField(
                modifier = Modifier.padding(10.dp),
                value = shoppingViewModel.newAddress.phone,
                onValueChange = {
                    if(it.length <= 11) shoppingViewModel.newAddress = shoppingViewModel.newAddress.copy(phone = it)
                },
                label = {
                    Text(text = "手机")
                }
            )
            TextField(
                modifier = Modifier.padding(10.dp),
                value = shoppingViewModel.newAddress.address,
                onValueChange = {
                    if(it.length <= 30) shoppingViewModel.newAddress = shoppingViewModel.newAddress.copy(address = it)
                },
                label = {
                    Text(text = "地址")
                }
            )
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    modifier = Modifier.padding(end = 10.dp),
                    onClick = {
                        if(shoppingViewModel.newAddress.name.isNotEmpty() && PatternUtils.checkPhone(shoppingViewModel.newAddress.phone)){
                            shoppingViewModel.addAddress()
                        }
                        shoppingViewModel.showAddAddressDialog = false
                    }
                ) {
                    Text(text = "提交")
                }
                Button(onClick = {
                    shoppingViewModel.showAddAddressDialog = false
                }) {
                    Text(text = "取消")
                }
            }
        }
    }
}