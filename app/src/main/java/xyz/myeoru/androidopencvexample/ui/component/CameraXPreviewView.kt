package xyz.myeoru.androidopencvexample.ui.component

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraXPreviewView(
    modifier: Modifier = Modifier,
    useCases: Array<UseCase> = emptyArray()
) {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

    LaunchedEffect(key1 = lensFacing) {
        val cameraProvider = suspendCoroutine { continuation ->
            ProcessCameraProvider.getInstance(context).also { cameraProvider ->
                cameraProvider.addListener({
                    continuation.resume(cameraProvider.get())
                }, ContextCompat.getMainExecutor(context))
            }
        }
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            *useCases
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )
}