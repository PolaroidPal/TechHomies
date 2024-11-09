package com.example.stepcounterapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.stepcounterapp.auth.presentation.sign_in.LoginScreen
import com.example.stepcounterapp.auth.presentation.sign_in.LoginViewModel
import com.example.stepcounterapp.core.util.NavigationEvent
import com.example.stepcounterapp.home.presentation.HomeScreen
import com.example.stepcounterapp.ui.theme.StepCounterAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import com.example.stepcounterapp.auth.domain.GoogleSignIn
import com.example.stepcounterapp.auth.presentation.sign_in.GoogleSignInClient
import com.example.stepcounterapp.details.presentation.DetailScreen
import com.example.stepcounterapp.home.presentation.HomeViewModel
import com.example.stepcounterapp.home.presentation.components.AppBar
import com.example.stepcounterapp.home.presentation.components.DrawerHeader
import com.example.stepcounterapp.medication_details.presentation.screens.AddMediationScreen
import com.example.stepcounterapp.medication_details.presentation.screens.MedicationDetailScreen
import com.example.stepcounterapp.medication_details.presentation.viewmodels.MedicationDetailViewModel
import com.example.stepcounterapp.settings.presentation.SettingsScreen
import com.example.stepcounterapp.settings.presentation.SettingsViewModel
import com.example.stepcounterapp.step_counter.presentation.StepCounterScreen
import com.example.stepcounterapp.step_counter.presentation.StepCounterScreenViewModel
import com.example.stepcounterapp.step_counter.presentation.components.TextComponent
import com.example.stepcounterapp.ui.theme.DrawerColor
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var googleSignInRepo: GoogleSignIn

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StepCounterAppTheme {
                val windowSizeClass = calculateWindowSizeClass(activity = this)
                AppContent(googleSignInRepo, windowSizeClass.widthSizeClass)
            }
        }
    }
}

@Composable
fun AppContent(
    googleSignInRepo: GoogleSignIn,
    windowSizeClass: WindowWidthSizeClass
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val stepCounterViewModel: StepCounterScreenViewModel = hiltViewModel()
    val navController = rememberNavController()
    val context = LocalContext.current
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val medicationViewModel: MedicationDetailViewModel = hiltViewModel()

    val details by medicationViewModel.medicationDetails.collectAsStateWithLifecycle()

    var showAppBar by remember {
        mutableStateOf(true)
    }

    val googleSignIn = remember {
        GoogleSignInClient(googleSignInRepo, context as ComponentActivity)
    }

    val state by googleSignIn.state.collectAsStateWithLifecycle()

    val userProfile by homeViewModel.userProfile.collectAsStateWithLifecycle()

    Log.i("profile", "AppContent: User data is $userProfile")

    val startDestination = if (isLoggedIn) {
        showAppBar = true
        NavigationEvent.HomeScreen
    } else {
        NavigationEvent.LoginScreen
    }

    val stepsCovered by stepCounterViewModel.stepsCovered.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = viewModel.navigationEvent) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is NavigationEvent.HomeScreen -> {
                    navController.navigate(NavigationEvent.HomeScreen) {
                        popUpTo(NavigationEvent.LoginScreen) { inclusive = true }
                    }
                    viewModel.resetNavigation()
                }

                else -> Unit
            }
        }
    }

    LaunchedEffect(key1 = state.isSignedIn) {
        if (state.isSignedIn) {
            Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()
            viewModel.navigateTo(NavigationEvent.HomeScreen)
            googleSignIn.resetSignInState()
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DrawerColor
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    item {
                        DrawerHeader(userProfile = userProfile)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    itemsIndexed(com.example.stepcounterapp.home.presentation.items) { index, item ->
                        NavigationDrawerItem(
                            label = {
                                TextComponent(
                                    text = item.title,
                                    color = Color.Black
                                )
                            },
                            selected = index == selectedIndex,
                            onClick = {
                                selectedIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                                when (item.id) {
                                    "home" -> navController.popBackStack(
                                        route = NavigationEvent.HomeScreen,
                                        inclusive = false
                                    )
                                    "detail" -> navController.navigate(NavigationEvent.DetailsScreen)
                                    "add_medicine_detail" -> navController.navigate(NavigationEvent.AddMedicationDetailScreen)
                                    "medicine_detail" -> navController.navigate(NavigationEvent.MedicationDetailsScreen)
                                    else -> navController.navigate(NavigationEvent.SettingsScreen)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selectedIndex == index) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    },
                                    contentDescription = item.contentDescription
                                )
                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                if (showAppBar) {
                    AppBar(
                        onNavigationIconClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    )
                }
            }
        ) {
            // Update navigation logic based on login state
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(it)
            ) {
                composable<NavigationEvent.LoginScreen> {
                    LoginScreen(
                        state = state,
                        onSignIn = {
                            googleSignIn.handleSignIn()
                        }
                    )
                }

                composable<NavigationEvent.HomeScreen> {
                    showAppBar = true
                    HomeScreen(
                        onNavigate = { navEvent ->
                            when (navEvent) {
                                is NavigationEvent.StepCounterScreen -> navController.navigate(NavigationEvent.StepCounterScreen(navEvent.id))
                                else -> Unit
                            }
                        },
                        windowSizeClass = windowSizeClass
                    )
                }

                composable<NavigationEvent.StepCounterScreen> { backStackEntry ->
                    showAppBar = false
                    val id = requireNotNull(backStackEntry.toRoute<NavigationEvent.StepCounterScreen>())
                    val contact by settingsViewModel.userName.collectAsStateWithLifecycle()
                    StepCounterScreen(
                        text = stepsCovered.toString(),
                        onSensorControllerClickStart = {
                            if (contact.isNotEmpty()) {
                                stepCounterViewModel.startStepCounter(contact)
                            }
                        },
                        onSensorControllerClickStop = {
                            stepCounterViewModel.stopTracking()
                        },
                        onSessionCancelClick = {
                            stepCounterViewModel.stopTracking()
                            navController.popBackStack()
                        },
                        onSessionSubmitClick = {
                            stepCounterViewModel.saveSession(id = id.id)
                            stepCounterViewModel.stopTracking()
                            navController.popBackStack()
                        },
                        windowSizeClass = windowSizeClass
                    )
                }

                composable<NavigationEvent.SettingsScreen> {
                    showAppBar = true
                    SettingsScreen()
                }

                composable<NavigationEvent.DetailsScreen> {
                    showAppBar = true
                    DetailScreen(
                        windowSizeClass = windowSizeClass
                    )
                }

                composable<NavigationEvent.AddMedicationDetailScreen> {
                    showAppBar = true
                    AddMediationScreen(windowSizeClass)
                }

                composable<NavigationEvent.MedicationDetailsScreen> {
                    showAppBar = true
                    MedicationDetailScreen(
                        windowSizeClass,
                        medications = details,
                        onChecked = {
                            medicationViewModel.update(it)
                        }
                    )
                }
            }
        }
    }
}

//@Composable
//fun calculateWindowSizeClass(activity: ComponentActivity): WindowSizeClassCustom {
//    val windowMetrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
//    val widthDp = windowMetrics.bounds.width() / activity.resources.displayMetrics.density
//    val heightDp = windowMetrics.bounds.height() / activity.resources.displayMetrics.density
//    Log.i("windowSize", "width: $widthDp")
//    Log.i("windowSize", "height: $heightDp")
//
//    return when {
//        widthDp < 600 -> WindowSizeClassCustom.Compact
//        widthDp < 840 -> WindowSizeClassCustom.Medium
//        else -> WindowSizeClassCustom.Expanded
//    }
//}
