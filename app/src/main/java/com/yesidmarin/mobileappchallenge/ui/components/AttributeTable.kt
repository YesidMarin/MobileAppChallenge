package com.yesidmarin.mobileappchallenge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yesidmarin.mobileappchallenge.data.response.ProductDetail.Attribute

@Composable
fun AttributeTable(
    attributes: List<Attribute>,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val visible = if (expanded) attributes else attributes.take(5)

    visible.forEachIndexed { index, attr ->
        val rowBg = if (index % 2 == 0) Color.White else Color(0x8DE2E2E2)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = 0.dp,
                    color = Color(0x8DE2E2E2)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(rowBg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .padding(vertical = 10.dp),
                    text = "${attr.name}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = attr.valueName.orEmpty(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }

    if (attributes.size > 5) {
        val arrow = if (expanded)
            Icons.AutoMirrored.Filled.KeyboardArrowLeft
        else
            Icons.AutoMirrored.Filled.KeyboardArrowRight

        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (expanded) "Ver menos" else "Ver todas las características",
                    style = MaterialTheme.typography.bodySmall
                )
                Icon(arrow, contentDescription = null)
            }
        }
    }
}
