package com.example.stepcounterapp.step_counter.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun StepCounterComponent(
    color: Color = Color.White,
    radius: Float = 100f,
    text: String = "\n4000\n",
    heightWeight: Float = 0.5f
) {
    Box {
        TextComponent(
            text = text,
            modifier = Modifier
                .align(Alignment.Center),
            fontWeight = FontWeight.Medium,
            fontSize = 40.sp,
            color = color
        )
        TextComponent(
            text = "\n\n\nsteps",
            modifier = Modifier
                .align(Alignment.Center),
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = color
        )
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(heightWeight)
        ) {
            this.drawCircle(
                color = Color.White,
                radius = radius,
                style = Stroke(10f)
            )
        }
    }
}