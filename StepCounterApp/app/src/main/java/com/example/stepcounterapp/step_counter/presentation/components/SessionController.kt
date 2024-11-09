package com.example.stepcounterapp.step_counter.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.stepcounterapp.step_counter.presentation.sessionButton

@Composable
fun SessionController(
    onSessionCancelClick: () -> Unit,
    onSessionSubmitClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        SessionButton(
            modifier = sessionButton,
            fontSize = 12.sp,
            text = "Cancel session",
            onclick = onSessionCancelClick,
            fontWeight = FontWeight.Normal
        )
        SessionButton(
            modifier = sessionButton,
            fontSize = 12.sp,
            onclick = onSessionSubmitClick,
            fontWeight = FontWeight.Normal
        )
    }
}