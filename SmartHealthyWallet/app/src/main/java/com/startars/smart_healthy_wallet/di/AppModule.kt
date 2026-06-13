package com.startars.smart_healthy_wallet.di

import android.content.Context
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.startars.smart_healthy_wallet.data.repository.AIRepositoryImpl
import com.startars.smart_healthy_wallet.data.repository.AiRepository
import com.startars.smart_healthy_wallet.data.source.AILocalDataSource
import com.startars.smart_healthy_wallet.domain.usecases.AnalyzeMedicalReceiptUseCase
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context.applicationContext

    @Provides
    @Singleton
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    fun provideAiLocalDataSource(
        @ApplicationContext context: Context
    ): AILocalDataSource = AILocalDataSource(context)

    @Provides
    @Singleton
    fun provideAiRepository(
        localDataSource: AILocalDataSource
    ): AiRepository = AIRepositoryImpl(localDataSource)

    @Provides
    @Singleton
    fun provideAnalyzeMedicalReceiptUseCase(
        repository: AiRepository
    ): AnalyzeMedicalReceiptUseCase = AnalyzeMedicalReceiptUseCase(repository)
}
