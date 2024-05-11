package xyz.myeoru.androidopencvexample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import xyz.myeoru.androidopencvexample.ui.screen.FaceRecognitionScreen
import xyz.myeoru.androidopencvexample.ui.screen.MainScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    onActivityFinish: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Main.route,
    ) {
        composable(
            route = Screens.Main.route
        ) {
            MainScreen(
                onActivityFinish = onActivityFinish,
                onNavigateToFaceRecognitionScreen = {
                    navController.navigate(Screens.FaceRecognition.route)
                }
            )
        }
        composable(
            route = Screens.FaceRecognition.route
        ) {
            FaceRecognitionScreen()
        }
    }
}