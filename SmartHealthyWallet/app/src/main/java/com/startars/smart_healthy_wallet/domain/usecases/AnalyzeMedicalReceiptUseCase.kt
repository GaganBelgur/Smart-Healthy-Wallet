package com.startars.smart_healthy_wallet.domain.usecases


import com.startars.smart_healthy_wallet.data.model.InferenceResult
import com.startars.smart_healthy_wallet.data.repository.AiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnalyzeMedicalReceiptUseCase(private val repository: AiRepository) {
    operator fun invoke(ocrText: String): Flow<InferenceResult> {
        if (ocrText.isBlank()) return flow {
            emit(InferenceResult.Failure(IllegalArgumentException("Empty text parsed")))
        }
        return repository.processReceipt(ocrText)
    }
}
