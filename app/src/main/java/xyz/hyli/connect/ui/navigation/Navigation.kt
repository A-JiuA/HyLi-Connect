package xyz.hyli.connect.ui.navigation


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.TabletAlt
import xyz.hyli.connect.R
import xyz.hyli.connect.ui.HyliConnectViewModel
import xyz.hyli.connect.ui.pages.connectScreen
import xyz.hyli.connect.ui.pages.devicesScreen
import xyz.hyli.connect.ui.pages.settingsScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun compactScreen(viewModel: HyliConnectViewModel, navController: NavHostController, currentSelect: MutableState<Int>) {
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Share, contentDescription = null) },
                label = { Text(stringResource(id = R.string.page_connect)) },
                selected = currentSelect.value == 0,
                onClick = {
                    currentSelect.value = 0
                    navController.navigate("connectScreen") {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            NavigationBarItem(
                icon = { Icon(FontAwesomeIcons.Solid.TabletAlt, contentDescription = null, modifier = Modifier.size(24.dp)) },
                label = { Text(stringResource(id = R.string.page_devices)) },
                selected = currentSelect.value == 1,
                onClick = {
                    currentSelect.value = 1
                    navController.navigate("devicesScreen") {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                label = { Text(stringResource(id = R.string.page_settings)) },
                selected = currentSelect.value == 2,
                onClick = {
                    currentSelect.value = 2
                    navController.navigate("settingsScreen") {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }, content = {
        innerPadding -> println(innerPadding)
        AnimatedNavHost(navController = navController, startDestination = "connectScreen",
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) },
            popEnterTransition = { fadeIn(animationSpec = tween(400)) },
            popExitTransition = { fadeOut(animationSpec = tween(400)) }) {
            composable("connectScreen") { connectScreen(viewModel, navController, currentSelect) }
            composable("devicesScreen") { devicesScreen(viewModel, navController, currentSelect) }
            composable("settingsScreen") { settingsScreen(viewModel, navController, currentSelect) }
        }
    })
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun mediumScreen(viewModel: HyliConnectViewModel, navController: NavHostController, currentSelect: MutableState<Int>) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        content = {
            innerPadding -> println(innerPadding)
            Row {
                NavigationRail {
                    Spacer(modifier = Modifier.weight(1f))
                    NavigationRailItem(
                        icon = { Icon(Icons.Filled.Share, contentDescription = null) },
                        label = { Text(stringResource(id = R.string.page_connect)) },
                        selected = currentSelect.value == 0,
                        onClick = {
                            currentSelect.value = 0
                            navController.navigate("connectScreen") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationRailItem(
                        icon = { Icon(FontAwesomeIcons.Solid.TabletAlt, contentDescription = null, modifier = Modifier.size(24.dp)) },
                        label = { Text(stringResource(id = R.string.page_devices)) },
                        selected = currentSelect.value == 1,
                        onClick = {
                            currentSelect.value = 1
                            navController.navigate("devicesScreen") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationRailItem(
                        icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                        label = { Text(stringResource(id = R.string.page_settings)) },
                        selected = currentSelect.value == 2,
                        onClick = {
                            currentSelect.value = 2
                            navController.navigate("settingsScreen") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                AnimatedNavHost(navController = navController, startDestination = "connectScreen",
                    enterTransition = { fadeIn(animationSpec = tween(400)) },
                    exitTransition = { fadeOut(animationSpec = tween(400)) },
                    popEnterTransition = { fadeIn(animationSpec = tween(400)) },
                    popExitTransition = { fadeOut(animationSpec = tween(400)) }) {
                    composable("connectScreen") { connectScreen(viewModel, navController, currentSelect) }
                    composable("devicesScreen") { devicesScreen(viewModel, navController, currentSelect) }
                    composable("settingsScreen") { settingsScreen(viewModel, navController, currentSelect) }
                }
            }
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun expandedScreen(viewModel: HyliConnectViewModel, navController: NavHostController, currentSelect: MutableState<Int>) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        content = {
            innerPadding -> println(innerPadding)
            Row {
                PermanentDrawerSheet {
                    Spacer(modifier = Modifier.weight(1f))
                    NavigationDrawerItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        icon = { Icon(Icons.Filled.Share, contentDescription = null) },
                        label = { Text(stringResource(id = R.string.page_connect)) },
                        selected = currentSelect.value == 0,
                        onClick = {
                            currentSelect.value = 0
                            navController.navigate("connectScreen") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationDrawerItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        icon = { Icon(FontAwesomeIcons.Solid.TabletAlt, contentDescription = null, modifier = Modifier.size(24.dp)) },
                        label = { Text(stringResource(id = R.string.page_devices)) },
                        selected = currentSelect.value == 1,
                        onClick = {
                            currentSelect.value = 1
                            navController.navigate("devicesScreen") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationDrawerItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                        label = { Text(stringResource(id = R.string.page_settings)) },
                        selected = currentSelect.value == 2,
                        onClick = {
                            currentSelect.value = 2
                            navController.navigate("settingsScreen") {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                AnimatedNavHost(navController = navController, startDestination = "connectScreen",
                    enterTransition = { fadeIn(animationSpec = tween(400)) },
                    exitTransition = { fadeOut(animationSpec = tween(400)) },
                    popEnterTransition = { fadeIn(animationSpec = tween(400)) },
                    popExitTransition = { fadeOut(animationSpec = tween(400)) }) {
                    composable("connectScreen") { connectScreen(viewModel, navController, currentSelect) }
                    composable("devicesScreen") { devicesScreen(viewModel, navController, currentSelect) }
                    composable("settingsScreen") { settingsScreen(viewModel, navController, currentSelect) }
                }
            }
        }
    )
}