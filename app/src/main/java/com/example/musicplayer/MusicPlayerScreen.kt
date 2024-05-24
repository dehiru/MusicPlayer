package com.example.musicplayer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.ui.MusicViewModel
import com.example.musicplayer.ui.screens.auth.AuthorizationScreen
import com.example.musicplayer.ui.screens.MainAppScreen
import com.example.musicplayer.ui.screens.auth.RegistrationScreen

enum class MusicPlayerScreens {
    Authorization,
    Registration,
    MainAppScreen
}

@Composable
fun MusicPlayerApp(
    viewModel: MusicViewModel = MusicViewModel(),
    navController: NavHostController = rememberNavController()
) {
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = MusicPlayerScreens.Authorization.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            composable(route = MusicPlayerScreens.Authorization.name) {
                AuthorizationScreen(
                    onAuthorizationPassed = {
                        navController.navigate(MusicPlayerScreens.MainAppScreen.name) {
                            popUpTo(MusicPlayerScreens.Authorization.name) { inclusive = true }
                        }
                    },
                    onRegisterButtonClicked = {
                        navController.navigate(MusicPlayerScreens.Registration.name)
                    }
                )
            }
            composable(route = MusicPlayerScreens.Registration.name) {
                RegistrationScreen(
                    onRegisterButtonClicked = {
                        navController.navigate(MusicPlayerScreens.MainAppScreen.name) {
                            popUpTo(MusicPlayerScreens.Authorization.name) { inclusive = true }
                        }
                    },
                    onCancelButtonClicked = {
                        navController.navigateUp()
                    }
                )
            }
            composable(route = MusicPlayerScreens.MainAppScreen.name) {
                MainAppScreen(viewModel)
            }
        }
    }
}