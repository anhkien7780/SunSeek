package com.example.sunseek.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sunseek.ui.screen.FavoriteLocation
import com.example.sunseek.ui.screen.FavoriteLocationScreen
import com.example.sunseek.ui.screen.Home
import com.example.sunseek.ui.screen.HomeScreen
import com.example.sunseek.ui.screen.Login
import com.example.sunseek.ui.screen.LoginScreen
import com.example.sunseek.ui.screen.Map
import com.example.sunseek.ui.screen.MapScreen
import com.example.sunseek.viewmodel.AccountViewModel
import com.example.sunseek.viewmodel.LocationViewModel
import com.example.sunseek.viewmodel.MapViewModel
import com.example.sunseek.viewmodel.OpenWeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient


@Composable
fun SunSeekNavHost(fusedLocationProviderClient: FusedLocationProviderClient) {

    val navController = rememberNavController()
    val accountViewModel: AccountViewModel = viewModel()
    val locationViewModel: LocationViewModel = viewModel()
    val mapViewModel: MapViewModel = viewModel()
    val openWeatherViewModel: OpenWeatherViewModel = viewModel()
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                accountViewModel = accountViewModel,
                locationViewModel = locationViewModel,
                onLoginSuccess = {
                    navController.navigate(Home)
                    navController.clearBackStack<Login>()
                }
            )
        }
        composable<Home> {
            HomeScreen(
                accountViewModel = accountViewModel,
                weatherViewModel = openWeatherViewModel,
                onSettingButtonClick = {},
                onFavoriteLocationsButtonClick = {
                    navController.navigate(FavoriteLocation)
                },
                onMapButtonClick = {
                    navController.navigate(Map)
                },
                onLogoutSuccess = {
                    navController.navigate(Login)
                    navController.clearBackStack<Home>()
                },
            )
        }
        composable<FavoriteLocation> {
            FavoriteLocationScreen(
                openWeatherViewModel = openWeatherViewModel,
                locationViewModel = locationViewModel,
                onBack = { navController.popBackStack() },
                onAddressSelected = {
                    navController.navigate(Home)
                },
                onEdit = {},
                onAddLocation = {
                    navController.navigate(Map)
                },
                onDelete = {}
            )
        }
        composable<Map> {
            MapScreen(
                fusedLocationClient = fusedLocationProviderClient,
                locationViewModel = locationViewModel,
                onBack = { navController.popBackStack() },
                mapViewModel = mapViewModel
            )
        }
    }
}

