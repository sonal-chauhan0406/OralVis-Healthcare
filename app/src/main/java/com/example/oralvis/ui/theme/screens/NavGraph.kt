package com.example.oralvis.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.oralvis.ui.screens.*
import com.example.oralvis.ui.screens.settings.SettingsScreen
import com.example.oralvis.viewmodel.SessionViewModel

object Routes {
    const val HOME = "home"
    const val CAPTURE = "capture"
    const val SEARCH = "search"
    const val HISTORY = "history"
    const val DETAIL = "details"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavHost(vm: SessionViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(vm = vm,
                onStartSession = { navController.navigate(Routes.CAPTURE) },
                onSearch = { navController.navigate(Routes.SEARCH) },
                onHistory = { navController.navigate(Routes.HISTORY) },
                onSettings = { navController.navigate(Routes.SETTINGS) })
        }
        composable(Routes.CAPTURE) {
            CaptureScreen(vm = vm, onBack = { navController.popBackStack() })
        }
        composable(Routes.SEARCH) {
            SearchScreen(vm = vm, onBack = { navController.popBackStack() })
        }
        composable(Routes.HISTORY) {
            HistoryScreen(vm = vm, onBack = { navController.popBackStack() }, onOpenDetail = { id ->
                navController.navigate("${Routes.DETAIL}/$id")
            })
        }
        composable("${Routes.DETAIL}/{sessionId}", arguments = listOf(navArgument("sessionId"){ type= NavType.StringType })) { backStackEntry ->
            val sid = backStackEntry.arguments?.getString("sessionId") ?: ""
            DetailScreen(vm = vm, sessionId = sid, onBack = { navController.popBackStack() })
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
