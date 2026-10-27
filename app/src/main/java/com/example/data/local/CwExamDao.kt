package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CwExamDao {
    @Query("SELECT * FROM questions ORDER BY subject ASC, chapter ASC, questionNumber ASC")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Query("SELECT * FROM test_attempts ORDER BY timestamp DESC")
    fun getAllTestAttempts(): Flow<List<TestAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestAttempt(attempt: TestAttemptEntity)

    @Query("SELECT * FROM live_classes ORDER BY isLiveNow DESC, id DESC")
    fun getAllLiveClasses(): Flow<List<LiveClassEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveClasses(classes: List<LiveClassEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveClass(liveClass: LiveClassEntity)

    @Query("SELECT * FROM pdf_notes ORDER BY id DESC")
    fun getAllPdfNotes(): Flow<List<PdfNoteEntity>>

    @Query("SELECT COUNT(*) FROM pdf_notes")
    suspend fun getPdfNoteCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPdfNotes(notes: List<PdfNoteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPdfNote(note: PdfNoteEntity)

    @Update
    suspend fun updatePdfNote(note: PdfNoteEntity)

    @Query("SELECT * FROM doubt_posts ORDER BY id DESC")
    fun getAllDoubtPosts(): Flow<List<DoubtPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoubtPosts(posts: List<DoubtPostEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoubtPost(post: DoubtPostEntity)

    @Update
    suspend fun updateDoubtPost(post: DoubtPostEntity)
}
