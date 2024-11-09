package com.example.stepcounterapp.home.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Details
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalInformation
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stepcounterapp.core.util.NavigationEvent
import com.example.stepcounterapp.home.domain.models.StepsProgress
import com.example.stepcounterapp.home.domain.models.TotalStepsWithSession
import com.example.stepcounterapp.home.presentation.components.CircularProgressBar
import com.example.stepcounterapp.home.presentation.components.SessionsItem
import com.example.stepcounterapp.home.presentation.wrapper.MenuItem
import com.example.stepcounterapp.ui.theme.HomeColBackground

val screenPadding = Modifier
    .padding(start = 18.dp, end = 18.dp, top = 18.dp)

val screenPaddingPortrait = Modifier
    .padding(start = 8.dp, end = 8.dp, top = 18.dp)

val sessionItem = Modifier
    .fillMaxWidth()
    .padding(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 8.dp)
    .height(60.dp)

val sessionItemPortrait = Modifier
    .fillMaxWidth()
    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
    .height(60.dp)

@Composable
fun HomeScreen(
    onNavigate: (NavigationEvent) -> Unit = {},
    windowSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val compositionCount = remember { mutableIntStateOf(0) }

    var id by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        id = viewModel.getTotalStepsId()
        Log.i("stepId", "HomeScreen: id is $id")
    }

    // Increment the count within LaunchedEffect to avoid recomposition loop
    LaunchedEffect(Unit) {
        compositionCount.intValue++
        Log.i("CompositionCount", "HomeScreen recomposed: ${compositionCount.intValue} times")
    }

//    val densityDpi = Resources.getSystem().displayMetrics.densityDpi
//    Log.i("window", "HomeScreen: $densityDpi")

    val userSetProgress by viewModel.state.collectAsStateWithLifecycle()
    val stepsProgress by viewModel.stepsProgress

    var stepsPercentage by rememberSaveable {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(key1 = stepsProgress.stepsCovered) {
        if (stepsProgress.stepsCovered != 0 && userSetProgress.target_steps != 0) {
            stepsPercentage =
                stepsProgress.stepsCovered / userSetProgress.target_steps.toFloat()
        }
    }

    when (windowSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CompactHomeScreen(
                totalStepsWithSession = userSetProgress,
                stepsProgress = stepsProgress,
                stepsPercentage = stepsPercentage,
                onNavigate = onNavigate,
                stepsCovered = stepsProgress.stepsCovered,
                id = id
            )
        }

        else -> {
            PortraitHomeScreen(
                totalStepsWithSession = userSetProgress,
                stepsProgress = stepsProgress,
                stepsPercentage = stepsPercentage,
                onNavigate = onNavigate,
                stepsCovered = stepsProgress.stepsCovered,
                id = id
            )
        }
    }
}

val items = listOf(
    MenuItem(
        id = "home",
        title = "Home",
        contentDescription = "Go to home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    MenuItem(
        id = "detail",
        title = "All steps",
        contentDescription = "Go to steps detail",
        selectedIcon = Icons.Filled.Details,
        unselectedIcon = Icons.Outlined.Details
    ),
    MenuItem(
        id = "add_medicine_detail",
        title = "Add medicine detail",
        contentDescription = "Go to add screen",
        selectedIcon = Icons.Filled.Medication,
        unselectedIcon = Icons.Outlined.Medication
    ),
    MenuItem(
        id = "medicine_detail",
        title = "Medicine detail",
        contentDescription = "Go to medine detail",
        selectedIcon = Icons.Filled.MedicalInformation,
        unselectedIcon = Icons.Outlined.MedicalInformation
    ),
    MenuItem(
        id = "settings",
        title = "Settings",
        contentDescription = "Go to settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)

@Preview(showBackground = true, name = "Portrait", widthDp = 384, heightDp = 853)
@Composable
private fun CompactHomeScreen(
    onNavigate: (NavigationEvent) -> Unit = {},
    onTrackStepsClick: () -> Unit = {},
    stepsPercentage: Float = 0f,
    stepsCovered: Int = 0,
    totalStepsWithSession: TotalStepsWithSession = TotalStepsWithSession(),
    stepsProgress: StepsProgress = StepsProgress(),
    id: String? = null
) {

    val compositionCount = remember { mutableIntStateOf(0) }

    // Increment the count within LaunchedEffect to avoid recomposition loop
    LaunchedEffect(Unit) {
        compositionCount.intValue++
        Log.i("CompositionCount", "Compact recomposed: ${compositionCount.intValue} times")
    }


    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(HomeColBackground)
    ) {
        item {
            CircularProgressBar(
                modifier = Modifier
                    .clickable {
                        Log.i("stepsId", "CompactHomeScreen: $id")
                        id?.let {
                            onNavigate(NavigationEvent.StepCounterScreen(id = id))
                        }
                    }
                    .padding(vertical = 18.dp),
                radius = 140.dp,
                color = Color.White,
                percentage = stepsPercentage,
                userSetStep = totalStepsWithSession.target_steps,
                stepsCovered = stepsCovered,
                completed = totalStepsWithSession.completed
            )

            Text(
                text = if (stepsProgress.kilometerCovered == "0") {
                    "0 Kilometers covered"
                } else {
                    String.format("%.4f", stepsProgress.kilometerCovered.toDouble()) + " Kilometers covered"
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = screenPadding
            )
        }

        items(totalStepsWithSession.stepsSession) { session ->
            SessionsItem(
                modifier = sessionItem,
                kilometer = session.kilometers,
                steps = session.steps.toString()
            )
        }
    }
}

@Preview(showBackground = true, name = "Portrait", widthDp = 853, heightDp = 384)
@Composable
private fun PortraitHomeScreen(
    onNavigate: (NavigationEvent) -> Unit = {},
    onTrackStepsClick: () -> Unit = {},
    stepsPercentage: Float = 0f,
    stepsCovered: Int = 0,
    totalStepsWithSession: TotalStepsWithSession = TotalStepsWithSession(),
    stepsProgress: StepsProgress = StepsProgress(),
    id: String? = null
) {

    val compositionCount = remember { mutableIntStateOf(0) }

    // Increment the count within LaunchedEffect to avoid recomposition loop
    LaunchedEffect(Unit) {
        compositionCount.intValue++
        Log.i("CompositionCount", "Compact recomposed: ${compositionCount.intValue} times")
    }
    LazyRow(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(HomeColBackground)
    ) {
        item {
            CircularProgressBar(
                modifier = Modifier
                    .clickable {
                        Log.i("stepsId", "PortraitHomeScreen: $id")
                        id?.let {
                            onNavigate(NavigationEvent.StepCounterScreen(id = id))
                        }
                    }
                    .padding(vertical = 18.dp),
                radius = 140.dp,
                color = Color.White,
                percentage = stepsPercentage,
                userSetStep = totalStepsWithSession.target_steps,
                stepsCovered = stepsCovered,
                completed = totalStepsWithSession.completed
            )
        }

        item {
            LazyColumn {
                item {
                    Text(
                        text = if (stepsProgress.kilometerCovered == "0") {
                            "0 Kilometers covered"
                        } else {
                            String.format("%.4f", stepsProgress.kilometerCovered.toDouble()) + " Kilometers covered"
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = screenPaddingPortrait
                    )
                }
                items(totalStepsWithSession.stepsSession) { session ->
                    SessionsItem(
                        modifier = sessionItemPortrait,
                        kilometer = session.kilometers,
                        steps = session.steps.toString()
                    )
                }
            }
        }
    }
}
