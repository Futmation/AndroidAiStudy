package xyz.myeoru.aistudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.opencv.android.OpenCVLoader
import xyz.myeoru.aistudy.ui.navigation.MainNavHost
import xyz.myeoru.aistudy.ui.theme.AiStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        OpenCVLoader.initLocal()

        setContent {
            val navController = rememberNavController()

            AiStudyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainNavHost(
                        navController = navController,
                        onActivityFinish = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}