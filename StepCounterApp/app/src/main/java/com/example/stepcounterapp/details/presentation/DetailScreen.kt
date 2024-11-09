package com.example.stepcounterapp.details.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stepcounterapp.details.domain.models.NewStepTask
import com.example.stepcounterapp.details.domain.models.StepsDetails
import com.example.stepcounterapp.details.presentation.components.DetailsItem
import com.example.stepcounterapp.settings.presentation.components.NewInfoAlert
import com.example.stepcounterapp.ui.theme.HomeColBackground
import com.example.stepcounterapp.ui.theme.HomeRowBackground

@Composable
fun DetailScreen(
    windowSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    detailViewModel: DetailViewModel = hiltViewModel()
) {

    val details by detailViewModel.stepsDetails.collectAsStateWithLifecycle()
    Log.i("details", "DetailScreen: $details")

    val currentActiveStep by detailViewModel.id

    when (windowSizeClass) {
        WindowWidthSizeClass.Compact -> {
            val columns = 2
            CompactDetailScreen(
                details = details,
                onAddNewTask = { newTargetStep ->
                    detailViewModel.addNewTask(
                        newStepTask = NewStepTask(
                            target_steps = newTargetStep
                        )
                    )
                },
                onActivate = { currentTrack, newTrack ->
                    newTrack?.let { newTrackId ->
                        detailViewModel.updateTracker(
                            currentTrack = currentTrack,
                            newTrack = newTrackId
                        )
                    }
                },
                columns = columns,
                id = currentActiveStep,
                backGroundBrush = HomeColBackground
            )
        }

        else -> {
            val columns = 4
            CompactDetailScreen(
                details = details,
                onAddNewTask = { newTargetStep ->
                    detailViewModel.addNewTask(
                        newStepTask = NewStepTask(
                            target_steps = newTargetStep
                        )
                    )
                },
                onActivate = { currentTrack, newTrack ->
                    newTrack?.let { newTrackId ->
                        detailViewModel.updateTracker(
                            currentTrack = currentTrack,
                            newTrack = newTrackId
                        )
                    }
                },
                columns = columns,
                id = currentActiveStep,
                backGroundBrush = HomeRowBackground,
                weight = 0.25f
            )
        }
    }
}

@Preview(showBackground = true, name = "Portrait", widthDp = 384, heightDp = 853)
@Composable
private fun CompactDetailScreen(
    details: List<StepsDetails> = emptyList(),
    onActivate: (String, String?) -> Unit = {_, _ ->},
    onAddNewTask: (Int) -> Unit = {},
    columns: Int = 2,
    id: String = "",
    backGroundBrush: Brush = HomeColBackground,
    weight: Float = 0.5f
) {
    val context = LocalContext.current

    val gridMod = remember {
        Modifier
            .fillMaxWidth(weight)
            .height(300.dp)
            .padding(8.dp)
    }

    var isNewTaskClick by remember {
        mutableStateOf(false)
    }

    var newStepTask by remember {
        mutableStateOf("")
    }

    when(isNewTaskClick) {
        true -> {
            NewInfoAlert(
                onDismiss = {
                    isNewTaskClick = false
                },
                onConfirm = {
                    if (newStepTask.isNotEmpty()) {
                        onAddNewTask(newStepTask.toInt())
                        isNewTaskClick = false
                    } else {
                        Toast.makeText(context, "Invalid value", Toast.LENGTH_SHORT).show()
                    }
                },
                newInfo = newStepTask,
                onChange = {
                    newStepTask = it
                },
                title = "New steps"
            )
        } else -> Unit
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isNewTaskClick = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new task"
                )
            }
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = it,
            modifier = Modifier
                .fillMaxSize()
                .background(backGroundBrush)
        ) {

            items(details) { detail ->
                DetailsItem(
                    detail = detail,
                    modifier = gridMod,
                    id = id,
                    onActivate = { id1, id2 ->
                        onActivate(id1, id2)
                    }
                )
            }
        }
    }
}