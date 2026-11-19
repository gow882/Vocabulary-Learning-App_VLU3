package com.example.vocabularylearningapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Vocabulary::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vocabDao(): VocabDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vocabulary_database"
                )
                    .addCallback(AppDatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabaseFromAssets(context, database.vocabDao())
                    }
                }
            }
        }

        suspend fun populateDatabaseFromAssets(context: Context, vocabDao: VocabDao) {
            try {
                if (vocabDao.getVocabCount() == 0) {
                    val jsonString = context.assets.open("vocab_data.json")
                        .bufferedReader()
                        .use { it.readText() }
                    val type = object : TypeToken<List<Vocabulary>>() {}.type
                    val vocabList: List<Vocabulary> = Gson().fromJson(jsonString, type)
                    if (!vocabList.isNullOrEmpty()) {
                        vocabDao.insertAll(vocabList)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}