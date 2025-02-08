package com.example.sunseek.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sunseek.ui.screen.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
object Login


@Composable
fun SunSeekNavHost(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login){
        composable<Login> { LoginScreen() }
    }
}

