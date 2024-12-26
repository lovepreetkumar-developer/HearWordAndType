package com.hear_word_and_type.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hear_word_and_type.app.data.db.converters.AllTypeConverters
import com.hear_word_and_type.app.data.db.entities.WordModel

@Database(
    entities = [WordModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(AllTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "BingoVocab"
            ).build()
    }
}