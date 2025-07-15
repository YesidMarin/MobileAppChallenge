package com.yesidmarin.mobileappchallenge.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProductIdentifierCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Búsqueda por:")
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
        Text("Código de producto")
    }
}

@Preview
@Composable
fun ProductIdentifierCheckboxPreview() {
    ProductIdentifierCheckbox(
        isChecked = true,
        onCheckedChange = {}
    )
}