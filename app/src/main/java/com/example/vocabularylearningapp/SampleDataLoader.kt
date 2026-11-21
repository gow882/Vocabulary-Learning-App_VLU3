package com.example.vocabularylearningapp


import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SampleDataLoader {
    suspend fun seedIfEmpty(context: Context, dao: VocabDao) {
        if (dao.count() > 0) return

        val json = context.assets.open("sample_words.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Vocabulary>>() {}.type
        val list: List<Vocabulary> = Gson().fromJson(json, type)

        dao.insertAll(list)
    }
}