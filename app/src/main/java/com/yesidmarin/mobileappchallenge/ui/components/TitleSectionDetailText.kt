package com.yesidmarin.mobileappchallenge.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TitleSectionDetailText(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Light,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview
@Composable
fun TitleSectionDetailTextPreview() {
    TitleSectionDetailText(title = "Product Details")
}