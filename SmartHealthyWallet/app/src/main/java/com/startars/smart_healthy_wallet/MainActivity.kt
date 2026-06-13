package com.startars.smart_healthy_wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.startars.smart_healthy_wallet.ui.screens.OnDeviceAiDemoScreen
import com.startars.smart_healthy_wallet.ui.screens.PerformanceMetrics
import com.startars.smart_healthy_wallet.ui.theme.SmartHealthyWalletTheme
import com.startars.smart_healthy_wallet.viewmodel.DemoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: DemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartHealthyWalletTheme {
                val uiState by viewModel.uiState.collectAsState()

                OnDeviceAiDemoScreen(
                    onScanReceiptClick = { detectedText ->
                        viewModel.onOcrTextDetected(detectedText, extractionTimeMs = 0)
                    },
                    llmOutput = uiState.llmOutputJson,
                    isProcessing = uiState.isProcessing,
                    metrics = PerformanceMetrics(
                        ocrLatencyMs = uiState.ocrLatency,
                        llmLatencyMs = uiState.llmLatency,
                        tokenGenerationSpeed = uiState.generationSpeed,
                        memoryUsageMb = uiState.systemMemoryAllocatedMb
                    )
                )
            }
        }
    }
}