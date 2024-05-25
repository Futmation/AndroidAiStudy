package xyz.myeoru.aistudy.ui.navigation

sealed class Screens(val route: String) {
    data object Main : Screens("main")
    data object FaceRecognition : Screens("faceRecognition")
}