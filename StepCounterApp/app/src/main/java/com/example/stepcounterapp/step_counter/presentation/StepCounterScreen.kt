package com.example.stepcounterapp.step_counter.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stepcounterapp.step_counter.presentation.components.SensorControllerButton
import com.example.stepcounterapp.step_counter.presentation.components.SessionController
import com.example.stepcounterapp.step_counter.presentation.components.StepCounterComponent
import com.example.stepcounterapp.ui.theme.HomeColBackground
import com.example.stepcounterapp.ui.theme.HomeRowBackground
import com.example.stepcounterapp.ui.theme.NavyBlue

val screenColModifier = Modifier
    .fillMaxSize()
    .background(HomeColBackground)

val screenRowModifier = Modifier
    .fillMaxSize()
    .background(HomeRowBackground)

val sessionButton = Modifier
    .padding(start = 8.dp, end = 8.dp)

@Composable
fun StepCounterScreen(
    text: String = "\n4000\n",
    onSensorControllerClickStart: () -> Unit = {},
    onSensorControllerClickStop: () -> Unit = {},
    onSessionSubmitClick: () -> Unit = {},
    onSessionCancelClick: () -> Unit = {},
    windowSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact
) {

    var clicked by rememberSaveable {
        mutableStateOf(false)
    }

    var icon by remember {
        mutableStateOf(Icons.Default.PlayArrow)
    }

    LaunchedEffect(key1 = clicked) {
        icon = if (clicked) {
            Icons.Default.Pause
        } else {
            Icons.Default.PlayArrow
        }
    }

    when (windowSizeClass) {
        WindowWidthSizeClass.Compact -> {
            StepCounterCompactScreen(
                text = text,
                isClicked = clicked,
                onSensorControllerClickStart = onSensorControllerClickStart,
                onSensorControllerClickStop = onSensorControllerClickStop,
                onSessionCancelClick = onSessionCancelClick,
                onSessionSubmitClick = onSessionSubmitClick,
                onIconChange = {
                    clicked = !clicked
                },
                icon = icon
            )
        }

        else -> {
            StepCounterMediumScreen(
                text = text,
                onSensorControllerClickStart = onSensorControllerClickStart,
                onSensorControllerClickStop = onSensorControllerClickStop,
                onSessionCancelClick = onSessionCancelClick,
                onSessionSubmitClick = onSessionSubmitClick,
                onIconChange = {
                    clicked = !clicked
                },
                icon = icon
            )
        }
    }
}


@Preview(name = "Compact", showBackground = true, widthDp = 315, heightDp = 640)
@Composable
private fun StepCounterScreenPreview() {
    StepCounterScreen()
}

@Preview(name = "Medium", showBackground = true, widthDp = 384, heightDp = 853)
@Composable
fun StepCounterCompactScreen(
    text: String = "\n4000\n",
    isClicked: Boolean = false,
    onSensorControllerClickStart: () -> Unit = {},
    onSensorControllerClickStop: () -> Unit = {},
    onSessionCancelClick: () -> Unit = {},
    onSessionSubmitClick: () -> Unit = {},
    onIconChange: () -> Unit = {},
    icon: ImageVector = Icons.Default.PlayArrow
) {
    Log.i("screenConfiguration", "StepCounterCompactScreen: Inside compact layout")
    Column(
        modifier = screenColModifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        StepCounterComponent(
            color = Color.Black,
            radius = 280f,
            text = text
        )

        SensorControllerButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(NavyBlue)
                .size(60.dp),
            onClick = {
                onIconChange()
                if(!isClicked) {
                    onSensorControllerClickStart()
                } else {
                    onSensorControllerClickStop()
                }
            },
            icon = icon
        )

        SessionController(
            onSessionCancelClick = onSessionCancelClick,
            onSessionSubmitClick = onSessionSubmitClick
        )
    }
}

@Preview(name = "Landscape", showBackground = true, widthDp = 812, heightDp = 375)
@Composable
fun StepCounterMediumScreen(
    text: String = "\n4000\n",
    isClicked: Boolean = false,
    onSensorControllerClickStart: () -> Unit = {},
    onSensorControllerClickStop: () -> Unit = {},
    onSessionCancelClick: () -> Unit = {},
    onSessionSubmitClick: () -> Unit = {},
    onIconChange: () -> Unit = {},
    icon: ImageVector = Icons.Default.PlayArrow
) {
    Log.i("screenConfiguration", "StepCounterMediumScreen: Inside medium layout")
    Row(
        modifier = screenRowModifier
            .padding(start = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StepCounterComponent(
                color = Color.White,
                radius = 240f,
                text = text,
                0.3f
            )

            SensorControllerButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(NavyBlue)
                    .size(50.dp),
                onClick = {
                    onIconChange()
                    if(!isClicked) {
                        onSensorControllerClickStart()
                    } else {
                        onSensorControllerClickStop()
                    }
                },
                icon = icon
            )
        }

        SessionController(
            onSessionCancelClick = onSessionCancelClick,
            onSessionSubmitClick = onSessionSubmitClick
        )
    }
}