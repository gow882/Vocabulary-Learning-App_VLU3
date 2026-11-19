package com.example.vocabularylearningapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabDao {
    @Query("SELECT * FROM vocabulary")
    fun getAllVocabularies(): Flow<List<Vocabulary>>

    @Query("SELECT * FROM vocabulary WHERE word LIKE '%' || :query || '%' OR meaning LIKE '%' || :query || '%' OR example LIKE '%' || :query || '%' ORDER BY word ASC")
    fun searchVocabularies(query: String): Flow<List<Vocabulary>>

    @Query("SELECT COUNT(*) FROM vocabulary")
    suspend fun getVocabCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabulary(vocabulary: Vocabulary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vocabularies: List<Vocabulary>)

    @Update
    suspend fun updateVocabulary(vocabulary: Vocabulary)

    @Delete
    suspend fun deleteVocabulary(vocabulary: Vocabulary)
}