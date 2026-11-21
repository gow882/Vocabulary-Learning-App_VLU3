package com.example.vocabularylearningapp

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getDatabase(this@MyApp).vocabDao()
            SampleDataLoader.seedIfEmpty(this@MyApp, dao)
        }
    }
}