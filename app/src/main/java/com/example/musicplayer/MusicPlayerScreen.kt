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

enum class MusicPlayerScreen {
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
            startDestination = MusicPlayerScreen.Authorization.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            composable(route = MusicPlayerScreen.Authorization.name) {
                AuthorizationScreen(
                    onAuthorizationPassed = {
                        navController.navigate(MusicPlayerScreen.MainAppScreen.name)
                    },
                    onRegisterButtonClicked = {
                        navController.navigate(MusicPlayerScreen.Registration.name)
                    }
                )
            }
            composable(route = MusicPlayerScreen.Registration.name) {
                RegistrationScreen(
                    onRegisterButtonClicked = {
                        navController.navigate(MusicPlayerScreen.MainAppScreen.name)
                    },
                    onCancelButtonClicked = {
                        navController.popBackStack(MusicPlayerScreen.Authorization.name, inclusive = false)
                    }
                )
            }
            composable(route = MusicPlayerScreen.MainAppScreen.name) {
                MainAppScreen(viewModel)
            }
        }
    }
}