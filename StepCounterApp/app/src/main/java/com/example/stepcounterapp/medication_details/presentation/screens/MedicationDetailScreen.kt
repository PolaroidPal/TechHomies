package com.example.stepcounterapp.medication_details.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stepcounterapp.medication_details.domain.model.AllMedicationDetails
import com.example.stepcounterapp.medication_details.presentation.components.SingleItem
import com.example.stepcounterapp.medication_details.presentation.viewmodels.MedicationDetailViewModel
import com.example.stepcounterapp.ui.theme.HomeColBackground

val listMod = Modifier
    .fillMaxWidth()
    .height(100.dp)
    .padding(start = 18.dp, end = 18.dp, bottom = 14.dp)

//val listModRow = Modifier
//    .fillMaxWidth()
//    .padding(start = 18.dp, end = 18.dp, bottom = 14.dp)

@Composable
fun MedicationDetailScreen(
    windowWidthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    medications: List<AllMedicationDetails> = emptyList(),
    onChecked: (String) -> Unit = {}
) {

    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(HomeColBackground)
            .padding(start = 18.dp, end = 18.dp, top = 20.dp, bottom = 20.dp)
    ) {
        items(medications) { medication ->
            SingleItem(
                modifier = listMod,
                name = medication.medicationName,
                timeStamp = medication.medicationTime.toDate().toString(),
                id = medication.id,
                onCheck = onChecked
            )
        }
    }
}

@Preview(showBackground = true, name = "Portrait", widthDp = 384, heightDp = 853)
@Preview
@Composable
private fun MedicationDetailScreenPreview() {
    MedicationDetailScreen()
}