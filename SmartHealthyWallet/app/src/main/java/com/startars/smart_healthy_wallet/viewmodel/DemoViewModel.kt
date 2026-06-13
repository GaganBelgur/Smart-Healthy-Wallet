package com.startars.smart_healthy_wallet.viewmodel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startars.smart_healthy_wallet.data.model.InferenceResult
import com.startars.smart_healthy_wallet.domain.usecases.AnalyzeMedicalReceiptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val llmOutputJson: String = "",
    val isProcessing: Boolean = false,
    val ocrLatency: Long = 0,
    val llmLatency: Long = 0,
    val generationSpeed: Double = 0.0,
    val systemMemoryAllocatedMb: Long = 0
)
@HiltViewModel
class DemoViewModel @Inject constructor(
    private val analyzeReceiptUseCase: AnalyzeMedicalReceiptUseCase,
    private val appContext: Context
) : ViewModel(){

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        updateDeviceMemoryMetrics()
    }

    fun onOcrTextDetected(detectedText: String, extractionTimeMs: Long) {
        if (_uiState.value.isProcessing) return // Prevent overlapping triggers

        _uiState.update { it.copy(isProcessing = true, ocrLatency = extractionTimeMs) }

        viewModelScope.launch {
            analyzeReceiptUseCase(detectedText).collect { result ->
                when (result) {
                    is InferenceResult.Success -> {
                        _uiState.update { it.copy(
                            llmOutputJson = result.outputJson,
                            llmLatency = result.latencyMs,
                            generationSpeed = result.tokensPerSec,
                            isProcessing = false
                        )}
                        updateDeviceMemoryMetrics() // Refresh device profile footprint numbers
                    }
                    is InferenceResult.Failure -> {
                        _uiState.update { it.copy(
                            llmOutputJson = "{'error': '${result.throwable.message}'}",
                            isProcessing = false
                        )}
                    }
                }
            }
        }
    }

    private fun updateDeviceMemoryMetrics() {
        val runtime = Runtime.getRuntime()
        val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
        _uiState.update { it.copy(systemMemoryAllocatedMb = usedMemory) }
    }
}
