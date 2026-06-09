package com.startars.smart_healthy_wallet.data.repository

import com.startars.smart_healthy_wallet.data.model.InferenceResult
import kotlinx.coroutines.flow.Flow

interface AiRepository {
    fun processReceipt(rawOcrText: String): Flow<InferenceResult>
}