package com.example.stepcounterapp.details.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val canvasMod = Modifier
    .fillMaxWidth()
    .fillMaxHeight()
    .padding(4.dp)

@Preview
@Composable
fun StepProgressBar(
    steps: Int = 100,
    totalSteps: Int = 1000,
    modifier: Modifier = Modifier,
    radius: Float = 100f,
    circleColor: Color = Color.Black,
    textColor: Color = Color.White,
    fontSize: TextUnit = 8.sp,
    dividerWidth: Dp = 40.dp
) {

    Box {
        Text(
            text = "$steps\n\n",
            fontSize = fontSize,
            color = textColor,
            modifier = Modifier
                .align(Alignment.Center)
        )

        HorizontalDivider(
            color = Color.Red,
            thickness = 1.dp,
            modifier = Modifier
                .width(dividerWidth)
                .align(Alignment.Center)
        )

        Text(
            text = "\n\n$totalSteps",
            fontSize = fontSize,
            color = textColor,
            modifier = Modifier
                .align(Alignment.Center)
        )

        Canvas(modifier = canvasMod.align(Alignment.Center)) {
            this.drawCircle(
                color = circleColor,
                radius = radius,
                center = this.center,
                style = Stroke(10f)
            )
        }
    }
}