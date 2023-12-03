package fr.wonderfulappstudio.notifymehere.presentation.data

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.wonderfulappstudio.notifymehere.presentation.dao.InterestPointDao
import fr.wonderfulappstudio.notifymehere.presentation.model.room.RoomInterestPoint

@Database(entities = [RoomInterestPoint::class], version = 1)
abstract class NotifyMeHereDatabase : RoomDatabase() {
    abstract fun interestPointDao(): InterestPointDao
}