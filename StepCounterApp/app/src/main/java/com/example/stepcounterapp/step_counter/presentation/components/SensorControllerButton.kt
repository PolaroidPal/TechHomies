package com.example.stepcounterapp.step_counter.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SensorControllerButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    icon: ImageVector = Icons.Default.PlayArrow
) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Star or stop sensor"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SensorControllerButtonPreview() {
    SensorControllerButton()
}