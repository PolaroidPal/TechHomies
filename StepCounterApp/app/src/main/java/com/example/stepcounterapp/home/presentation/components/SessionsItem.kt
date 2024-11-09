package com.example.stepcounterapp.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun SessionsItem(
    kilometer: String = "0.123",
    steps: String = "45",
    modifier: Modifier = Modifier,
    cornerSize: Dp = 10.dp
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cornerSize),
        border = BorderStroke(color = Color.Black, width = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = "Steps: $steps",
                fontSize = 18.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = "Kilometers: $kilometer",
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}