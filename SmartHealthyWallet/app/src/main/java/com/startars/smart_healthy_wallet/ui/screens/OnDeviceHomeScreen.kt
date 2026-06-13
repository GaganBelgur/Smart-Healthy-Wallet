package com.startars.smart_healthy_wallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Simple UI State to hold live processing numbers
data class PerformanceMetrics(
    val ocrLatencyMs: Long = 0,
    val llmLatencyMs: Long = 0,
    val tokenGenerationSpeed: Double = 0.0,
    val memoryUsageMb: Long = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnDeviceAiDemoScreen(
    onScanReceiptClick: (String) -> Unit,
    llmOutput: String,
    isProcessing: Boolean,
    metrics: PerformanceMetrics
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HealthyWallet On-Device Demo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Replace the top half container Box in OnDeviceAiDemoScreen with this:
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .background(Color.Black)
            ) {
                CameraPreviewAnalysisView(
                    onTextExtracted = { text, runtime ->
                        onScanReceiptClick(text) // This hooks directly into ViewModel's `onOcrTextDetected`
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Offline indicator pill overlay
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd),
                    color = Color(0xCC000000),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "✈️ 100% Offline Mode",
                        color = Color.Green,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }


            // Replace the top half container Box in OnDeviceAiDemoScreen with this:
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .background(Color.Black)
            ) {
                CameraPreviewAnalysisView(
                    onTextExtracted = { text, _ ->
                        onScanReceiptClick(text) // This hooks directly into ViewModel's `onOcrTextDetected`
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Offline indicator pill overlay
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd),
                    color = Color(0xCC000000),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "✈️ 100% Offline Mode",
                        color = Color.Green,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // MIDDLE SECTION: Live AI Telemetry Dashboard (Crucial for the demo)
            TelemetryDashboard(metrics)

            // BOTTOM HALF: Local Engine JSON Output Console
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFF1E1E1E)) // Dark code editor background
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LOCAL GEMMA 2B JSON INFERENCE",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Code Terminal Display
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)),
                    color = Color(0xFF121212)
                ) {
                    Text(
                        text = if (llmOutput.isEmpty()) "// Align medical receipt in viewport to trigger..." else llmOutput,
                        color = if (llmOutput.isEmpty()) Color.DarkGray else Color(0xFF9CDCFE),
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TelemetryDashboard(metrics: PerformanceMetrics) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MetricItem(label = "OCR Latency", value = "${metrics.ocrLatencyMs}ms")
        MetricItem(label = "LLM Latency", value = "${metrics.llmLatencyMs}ms")
        MetricItem(label = "Generation", value = "${metrics.tokenGenerationSpeed} t/s")
        MetricItem(label = "VRAM Allocation", value = "${metrics.memoryUsageMb}MB")
    }
}

@Composable
fun MetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
