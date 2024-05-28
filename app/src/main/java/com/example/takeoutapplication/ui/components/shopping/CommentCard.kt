package com.example.takeoutapplication.ui.components.shopping

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.takeoutapplication.model.entity.RestaurantComment
import com.example.takeoutapplication.ui.screen.mine.RateStars
import com.example.takeoutapplication.utils.DateUtils

@Composable
fun CommentCard(comment: RestaurantComment, modifier: Modifier){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp)
    ) {
        val paddingModifier = Modifier.padding(horizontal = 6.dp)
        Text(
            text = comment.username!!,
            modifier = paddingModifier
        )
        RateStars(
            readOnly = true,
            defaultValue = comment.rate,
            modifier = paddingModifier
        )
        Text(
            text = DateUtils.instantToString2(comment.createTime!!),
            modifier = paddingModifier
        )
        Text(
            modifier = paddingModifier.padding(vertical = 10.dp),
            text = comment.content
        )
    }
}