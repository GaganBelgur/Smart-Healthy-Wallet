package com.startars.smart_healthy_wallet.utils

import android.os.SystemClock
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ReceiptFrameAnalyzer(
    private val onTextExtracted: (String, Long) -> Unit,
) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val startTime = SystemClock.elapsedRealtime()
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Heuristic constraint: ensure it looks like a medical bill/receipt before processing
                    if (visionText.text.isNotBlank() &&
                        (visionText.text.contains("total", ignoreCase = true) ||
                                visionText.text.contains("balance", ignoreCase = true))
                    ) {

                        val latency = SystemClock.elapsedRealtime() - startTime
                        onTextExtracted(visionText.text, latency)
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close() // ALWAYS release frame to avoid memory freeze leaks
                }
        } else {
            imageProxy.close()
        }
    }
}
