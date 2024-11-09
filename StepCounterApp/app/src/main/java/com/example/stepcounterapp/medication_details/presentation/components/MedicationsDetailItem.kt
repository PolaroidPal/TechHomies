package com.example.stepcounterapp.medication_details.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SingleItem(
    onCheck: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    name: String = "Aurochs",
    timeStamp: String = "21/05/2024",
    id: String = ""
) {

    var checked by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = 2.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 20.dp)
                    .weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 22.sp,
                    color = Color.Black
                )
                Text(
                    text = timeStamp,
                    fontSize = 22.sp,
                    color = Color.Black
                )
            }

            Checkbox(
                checked = checked,
                onCheckedChange = {
                    onCheck(id)
                    checked = !checked
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleItemPreview() {
    SingleItem()
}