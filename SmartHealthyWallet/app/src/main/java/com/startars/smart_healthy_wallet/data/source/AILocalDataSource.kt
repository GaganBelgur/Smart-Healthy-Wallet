package com.startars.smart_healthy_wallet.data.source

import android.content.Context
import android.os.SystemClock
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.startars.smart_healthy_wallet.data.model.InferenceResult
import com.startars.smart_healthy_wallet.domain.ai.PromptTemplates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AILocalDataSource(private val context: Context) {
    private var llmInference: LlmInference? = null

    fun initEngine(modelPath: String) {
        if (llmInference != null) return
        val options = LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelPath)
            .setMaxTokens(256)
            .setTemperature(0.1f)
            .build()
        llmInference = LlmInference.createFromOptions(context, options)
    }

    suspend fun runReasoning(rawText: String): InferenceResult = withContext(Dispatchers.Default) {
        val startTime = SystemClock.elapsedRealtime()

        // Injecting the highly structured token constraint sequence
        val structuredPrompt = PromptTemplates.buildMedicalReceiptPrompt(rawText)

        return@withContext try {
            val response = llmInference?.generateResponse(structuredPrompt) ?: "Error: Engine dead"
            val latency = SystemClock.elapsedRealtime() - startTime

            // Sanitize any accidental trailing junk data characters the model might output
            val cleanJsonResponse = response.trim().substringAfter("<start_of_turn>model").trim()

            val tokenCount = cleanJsonResponse.length / 4
            val tokensPerSec = if (latency > 0) (tokenCount.toDouble() / (latency / 1000.0)) else 0.0

            InferenceResult.Success(cleanJsonResponse, latency, tokensPerSec)
        } catch (e: Exception) {
            InferenceResult.Failure(e)
        }
    }
}
