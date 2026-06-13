package com.startars.smart_healthy_wallet.data.repository

import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import com.startars.smart_healthy_wallet.data.model.InferenceResult
import com.startars.smart_healthy_wallet.data.source.AILocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class AIRepositoryImpl(private val localDataSource: AILocalDataSource): AiRepository {
    override fun processReceipt(rawOcrText: String): Flow<InferenceResult> = flow {
        val downloadDirectory = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val targetFile = File(downloadDirectory, "gemma-2b-it-cpu-int4.bin")

        localDataSource.initEngine(targetFile.absolutePath)
        emit(localDataSource.runReasoning(rawOcrText))
    }
}