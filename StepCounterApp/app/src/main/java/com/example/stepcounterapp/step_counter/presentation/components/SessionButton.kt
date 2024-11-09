package com.example.stepcounterapp.step_counter.presentation.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun SessionButton(
    modifier: Modifier = Modifier,
    onclick: () -> Unit = {},
    text: String = "Submit session",
    fontWeight: FontWeight = FontWeight.Medium,
    fontSize: TextUnit = 14.sp,
    weight: Float = 1f
) {

    Button(
        onClick = { onclick() },
        modifier = modifier
    ) {
        TextComponent(
            text = text,
            fontWeight = fontWeight,
            fontSize = fontSize
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SessionButtonPreview() {
    SessionButton()
}