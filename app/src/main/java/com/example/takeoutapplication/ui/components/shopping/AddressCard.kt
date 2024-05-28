package com.example.takeoutapplication.ui.components.shopping

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.takeoutapplication.model.entity.Address

@Composable
fun AddressCard(address: Address, modifier: Modifier, colors: CardColors = CardDefaults.cardColors(), onClick: () -> Unit){
    Card(
        colors = colors,
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth()
        ) {
            Text(text = address.name)
            Text(text = address.phone)
        }
        Text(text = address.address)
    }
}