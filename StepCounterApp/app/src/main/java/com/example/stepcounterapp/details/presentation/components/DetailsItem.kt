package com.example.stepcounterapp.details.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stepcounterapp.R
import com.example.stepcounterapp.details.domain.models.StepsDetails

@Preview(showBackground = true)
@Composable
fun DetailsItem(
    detail: StepsDetails = StepsDetails(),
    onActivate: (String, String?) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    id: String = "",
    cardColor: Color = Color.LightGray
) {

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        colors = CardDefaults.cardColors(cardColor),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp
                    )
                )
                .background(Color.White)
        ) {
            if (!detail.completed) {
                StepProgressBar(
                    textColor = Color.Black,
                    radius = 140f,
                    dividerWidth = 80.dp,
                    fontSize = 16.sp,
                    steps = detail.stepsCovered,
                    totalSteps = detail.target_steps
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.completed_image),
                    contentDescription = "Step task completed",
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }

        Button(
            onClick = {
                onActivate(id, detail.id)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
            enabled = !detail.tracking,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                disabledContainerColor = Color.DarkGray
            )
        ) {
            Text(
                text = "Activate"
            )
        }
    }
}