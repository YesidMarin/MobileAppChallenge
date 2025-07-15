package com.yesidmarin.mobileappchallenge.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.yesidmarin.mobileappchallenge.R

@Composable
fun ProductCard(modifier: Modifier, name: String, imageUrl: String?, onClick: () -> Unit) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .border(0.1.dp, Color(0x1E736C28)),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Product Image",
                modifier = Modifier .fillMaxWidth()
                    .height(150.dp),
                error = painterResource(R.drawable.placeholder_image),
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}