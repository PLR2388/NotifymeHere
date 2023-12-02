package fr.wonderfulappstudio.notifymehere.presentation.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {
    @Provides
    @ServiceScoped
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
}