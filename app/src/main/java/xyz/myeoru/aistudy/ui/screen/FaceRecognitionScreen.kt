package xyz.myeoru.aistudy.ui.screen

import android.app.Activity
import android.view.WindowManager
import androidx.camera.core.ImageAnalysis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.opencv.core.Rect
import xyz.myeoru.aistudy.analyzer.FaceRecognitionAnalyzer
import xyz.myeoru.aistudy.ui.component.CameraXPreviewView
import java.util.concurrent.Executors

@Composable
fun FaceRecognitionScreen() {
    val recognizedFaces = remember { mutableStateListOf<Rect>() }
    var recognizedWidth by remember { mutableFloatStateOf(0f) }
    var recognizedHeight by remember { mutableFloatStateOf(0f) }

    val context = LocalContext.current
    val cameraExecutor = Executors.newSingleThreadExecutor()
    val faceRecognitionAnalyzer = remember {
        FaceRecognitionAnalyzer(context) { rects, width, height ->
            recognizedFaces.clear()
            recognizedFaces.addAll(rects)
            recognizedWidth = width
            recognizedHeight = height
        }
    }
    val imageAnalyzer = ImageAnalysis.Builder().build().also {
        it.setAnalyzer(cameraExecutor, faceRecognitionAnalyzer)
    }

    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            cameraExecutor.shutdown()
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CameraXPreviewView(
                modifier = Modifier.fillMaxSize(),
                useCases = arrayOf(imageAnalyzer)
            )
            Canvas(modifier = Modifier.fillMaxSize()) {
                for (rect in recognizedFaces) {
                    val scaleX = size.width / recognizedWidth
                    val scaleY = size.height / recognizedHeight

                    val left = size.width - (rect.x + rect.width) * scaleX
                    val right = size.width - rect.x * scaleX

                    drawRect(
                        color = Color.Green,
                        topLeft = Offset(left, rect.y * scaleY),
                        size = Size(
                            width = right - left,
                            height = rect.height * scaleY
                        ),
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }
        }
    }
}