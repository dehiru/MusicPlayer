package com.example.musicplayer.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.R
import com.example.musicplayer.ui.MusicViewModel
import com.example.musicplayer.ui.screens.playlists.PlaylistsScreen
import com.example.musicplayer.ui.screens.playlists.SelectedPlaylistScreen
import com.example.musicplayer.ui.screens.profile.EditProfileScreen
import com.example.musicplayer.ui.screens.profile.ProfileScreen

enum class NavigationBarScreens {
    Home,
    Favorite,
    Playlists,
    Profile
}

enum class SecondaryScreens {
    EditProfile,
    SelectedPlaylist,
    TrackPlayer
}

data class BottomNavigationItem(
    val routeName: NavigationBarScreens,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MainAppScreen(
    viewModel: MusicViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val items = listOf(
        BottomNavigationItem(
            routeName = NavigationBarScreens.Home,
            title = stringResource(R.string.home),
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            routeName = NavigationBarScreens.Favorite,
            title = stringResource(R.string.favorite),
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder
        ),
        BottomNavigationItem(
            routeName = NavigationBarScreens.Playlists,
            title = stringResource(R.string.playlists),
            selectedIcon = ImageVector.vectorResource(R.drawable.library_music),
            unselectedIcon = ImageVector.vectorResource(R.drawable.library_music)
        ),
        BottomNavigationItem(
            routeName = NavigationBarScreens.Profile,
            title = stringResource(R.string.profile),
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle
        ),
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item -> 
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            if (item.routeName.name == NavigationBarScreens.Home.name) {
                                viewModel.selectPlaylistTracks(viewModel.getHomePlaylist())
                                navController.popBackStack(NavigationBarScreens.Home.name, inclusive = false)
                            } else {
                                if(item.routeName.name == NavigationBarScreens.Favorite.name) {
                                    viewModel.selectPlaylistTracks(viewModel.getFavoritePlaylist())
                                }
                                navController.navigate(item.routeName.name) {
                                    popUpTo(NavigationBarScreens.Home.name) { inclusive = false }
                                }
                            }
                        },
                        label = {
                            Text(text = item.title)
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        },
    ){
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationBarScreens.Home.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = NavigationBarScreens.Home.name) {
                if (selectedItemIndex != 0) selectedItemIndex = 0
                HomeScreen(
                    viewModel,
                    onTrackClicked = {
                        navController.navigate(SecondaryScreens.TrackPlayer.name)
                    }
                )
            }
            composable(route = NavigationBarScreens.Favorite.name) {
                if (selectedItemIndex != 1) selectedItemIndex = 1
                FavoriteScreen(
                    viewModel,
                    onTrackClicked = {
                        navController.navigate(SecondaryScreens.TrackPlayer.name)
                    }
                )
            }
            composable(route = NavigationBarScreens.Playlists.name) {
                if (selectedItemIndex != 2) selectedItemIndex = 2
                PlaylistsScreen(
                    viewModel,
                    onPlaylistClicked =  {
                        navController.navigate(SecondaryScreens.SelectedPlaylist.name)
                    }
                )
            }
            composable(route = NavigationBarScreens.Profile.name) {
                if (selectedItemIndex != 3) selectedItemIndex = 3
                ProfileScreen(
                    onEditButtonClicked =  {
                        navController.navigate(SecondaryScreens.EditProfile.name)
                    }
                )
            }
            composable(route = SecondaryScreens.EditProfile.name) {
                EditProfileScreen(
                    onButtonClicked =  {
                        navController.navigateUp()
                    }
                )
            }
            composable(route = SecondaryScreens.SelectedPlaylist.name) {
                SelectedPlaylistScreen(
                    viewModel = viewModel,
                    onArrowBackClicked = {
                        navController.navigateUp()
                    },
                    onTrackClicked = {
                        navController.navigate(SecondaryScreens.TrackPlayer.name)
                    }
                )
            }
            composable(route = SecondaryScreens.TrackPlayer.name) {
                TrackPlayer(viewModel = viewModel)
            }
        }
    }

}