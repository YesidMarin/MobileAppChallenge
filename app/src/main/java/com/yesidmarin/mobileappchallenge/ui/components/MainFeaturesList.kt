package com.yesidmarin.mobileappchallenge.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainFeaturesList(features: List<String?>) {
    features.forEach { featureText ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "• ",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0x8DE2E2E2),
                fontWeight = FontWeight.Normal
            )
            Text(
                text = featureText ?: "",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
fun MainFeaturesListPreview() {
    MainFeaturesList(
        features = listOf(
            "Feature 1: High quality"
        )
    )
}