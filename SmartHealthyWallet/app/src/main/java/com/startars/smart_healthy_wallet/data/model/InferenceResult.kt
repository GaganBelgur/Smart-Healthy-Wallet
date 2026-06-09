package com.startars.smart_healthy_wallet.data.model

sealed interface InferenceResult {
    data class Success(val outputJson: String, val latencyMs: Long, val tokensPerSec: Double) : InferenceResult
    data class Failure(val throwable: Throwable) : InferenceResult
}
