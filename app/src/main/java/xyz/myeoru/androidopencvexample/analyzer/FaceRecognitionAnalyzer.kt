package xyz.myeoru.androidopencvexample.analyzer

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Rect
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.nio.ByteBuffer

class FaceRecognitionAnalyzer(
    context: Context,
    private val onFaceRecognized: (rects: Array<Rect>, width: Float, height: Float) -> Unit
): ImageAnalysis.Analyzer {
    private var cascadeClassifier: CascadeClassifier? = null

    init {
        val inputStream = context.assets.open("opencv/haarcascade_frontalface_default.xml")
        val file = File(context.getDir("cascade", Context.MODE_PRIVATE), "haarcascade_frontalface_default.xml")
        val fileOutputStream = file.outputStream()
        val data = ByteArray(4096)
        var readBytes: Int
        while (inputStream.read(data).also { readBytes = it } != -1) {
            fileOutputStream.write(data, 0, readBytes)
        }

        cascadeClassifier = CascadeClassifier(file.absolutePath)
        inputStream.close()
        fileOutputStream.close()
        file.delete()
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        val data = ByteArray(remaining())
        get(data)
        return data
    }

    override fun analyze(image: ImageProxy) {
        val buffer = image.planes[0].buffer

        val yData = buffer.toByteArray()
        val yMat = Mat(image.height, image.width, CvType.CV_8UC1)
        yMat.put(0, 0, yData)

        val tyMat = yMat.t()
        yMat.release()

        val facesRects = MatOfRect()
        cascadeClassifier?.detectMultiScale(tyMat, facesRects)
        onFaceRecognized(facesRects.toArray(), image.height.toFloat(), image.width.toFloat())

        tyMat.release()
        facesRects.release()
        image.close()
    }
}