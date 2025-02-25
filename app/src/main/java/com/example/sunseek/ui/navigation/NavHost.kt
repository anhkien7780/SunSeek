package com.example.sunseek.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sunseek.ui.screen.Home
import com.example.sunseek.ui.screen.HomeScreen
import com.example.sunseek.ui.screen.Login
import com.example.sunseek.ui.screen.LoginScreen
import com.example.sunseek.viewmodel.AccountViewModel


@Composable
fun SunSeekNavHost() {

    val navController = rememberNavController()
    val accountViewModel: AccountViewModel = viewModel()
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                accountViewModel = accountViewModel,
                onLoginSuccess = {
                    navController.navigate(Home)
                    navController.clearBackStack<Login>()
                },
                context = LocalContext.current
            )
        }
        composable<Home> {
            HomeScreen(
                accountViewModel = accountViewModel,
                onSettingButtonClick = {},
                onFavoriteLocationsButtonClick = {},
                onSelectLocationButtonClick = {},
                onLogoutSuccess = {
                    navController.navigate(Login)
                    navController.clearBackStack<Home>()
                },
            )
        }
    }
}

