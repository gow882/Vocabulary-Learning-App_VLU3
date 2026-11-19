package com.example.vocabularylearningapp

import kotlinx.coroutines.flow.Flow

class VocabRepository(private val vocabDao: VocabDao) {

    val allVocabularies: Flow<List<Vocabulary>> = vocabDao.getAllVocabularies()

    fun searchVocabularies(query: String): Flow<List<Vocabulary>> {
        return vocabDao.searchVocabularies(query)
    }

    suspend fun insertVocabulary(vocabulary: Vocabulary) {
        vocabDao.insertVocabulary(vocabulary)
    }

    suspend fun insertAll(vocabularies: List<Vocabulary>) {
        vocabDao.insertAll(vocabularies)
    }

    suspend fun updateVocabulary(vocabulary: Vocabulary) {
        vocabDao.updateVocabulary(vocabulary)
    }

    suspend fun deleteVocabulary(vocabulary: Vocabulary) {
        vocabDao.deleteVocabulary(vocabulary)
    }
}
