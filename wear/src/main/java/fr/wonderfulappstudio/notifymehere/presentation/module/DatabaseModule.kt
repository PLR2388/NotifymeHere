package fr.wonderfulappstudio.notifymehere.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.wonderfulappstudio.notifymehere.presentation.dao.InterestPointDao
import fr.wonderfulappstudio.notifymehere.presentation.data.NotifyMeHereDatabase
import fr.wonderfulappstudio.notifymehere.presentation.datasource.InterestPointDatasource
import fr.wonderfulappstudio.notifymehere.presentation.repository.InterestPointRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNotifyMeHereDatabase(@ApplicationContext context: Context): NotifyMeHereDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            NotifyMeHereDatabase::class.java,
            "notify_me_here_database"
        ).build()


    @Provides
    fun provideInterestPointDao(notifyMeHereDatabase: NotifyMeHereDatabase): InterestPointDao =
        notifyMeHereDatabase.interestPointDao()


    @Provides
    fun provideInterestPointDatasource(interestPointDao: InterestPointDao): InterestPointDatasource =
        InterestPointDatasource(interestPointDao)


    @Provides
    fun provideInterestPointRepository(interestPointDatasource: InterestPointDatasource): InterestPointRepository =
        InterestPointRepository(interestPointDatasource)

}