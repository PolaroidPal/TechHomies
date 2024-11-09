package com.example.stepcounterapp.home.presentation.components

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stepcounterapp.R

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    percentage: Float = 0.7f,
    userSetStep: Int = 10000,
    stepsCovered: Int = 0,
    fontSize: TextUnit = 28.sp,
    radius: Dp = 100.dp,
    color: Color = Color.Red,
    strokeWidth: Dp = 8.dp,
    animDuration: Int = 2000,
    animDelay: Int = 0,
    completed: Boolean
) {
    // Local variable to keep track of compositions without triggering recompositions
    val compositionCount = remember { mutableIntStateOf(0) }

    // Increment the count within LaunchedEffect to avoid recomposition loop
    LaunchedEffect(Unit) {
        compositionCount.intValue++
        Log.i("CompositionCount", "CircularProgressBar recomposed: ${compositionCount.intValue} times")
    }

    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val currentPercentage2 = animateFloatAsState(
        targetValue = if (animationPlayed) 1.0f else 0f,
        animationSpec = tween(
            durationMillis = 2000,
            delayMillis = 0
        )
    )

    val currentPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )

    val animatedStepsCovered by animateIntAsState(
        targetValue = if (animationPlayed) stepsCovered else 0,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )

    LaunchedEffect(key1 = Unit) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(radius * 2f)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = color,
                -90f,
                360 * currentPercentage2.value,
                useCenter = false,
                style = Stroke(1.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                -90f,
                360 * currentPercentage.value,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        if (!completed) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = animatedStepsCovered.toString(),
                    color = Color.White,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Medium
                )
                HorizontalDivider(
                    thickness = 4.dp,
                    color = Color.Red,
                    modifier = Modifier
                        .width(180.dp)
                        .padding(8.dp)
                )

                Text(
                    text = userSetStep.toString(),
                    color = Color.White,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            Image(painter = painterResource(
                id = R.drawable.completed_image),
                contentDescription = "Steps completed",
                contentScale = ContentScale.Inside
            )
        }
    }
}