package com.example.data.local

import com.example.data.seed.MasterSyllabusSeedData
import com.example.data.seed.StoneAgeSeedData
import kotlinx.coroutines.flow.Flow

class CwExamRepository(private val dao: CwExamDao) {
    val allQuestions: Flow<List<QuestionEntity>> = dao.getAllQuestions()
    val allTestAttempts: Flow<List<TestAttemptEntity>> = dao.getAllTestAttempts()
    val allLiveClasses: Flow<List<LiveClassEntity>> = dao.getAllLiveClasses()
    val allPdfNotes: Flow<List<PdfNoteEntity>> = dao.getAllPdfNotes()
    val allDoubtPosts: Flow<List<DoubtPostEntity>> = dao.getAllDoubtPosts()

    suspend fun ensureSeedDataLoaded() {
        val count = dao.getQuestionCount()
        if (count == 0) {
            val allSeedQuestions = StoneAgeSeedData.getStoneAgeQuestions() +
                MasterSyllabusSeedData.getOtherSubjectsQuestions()
            dao.insertQuestions(allSeedQuestions)
            dao.insertLiveClasses(MasterSyllabusSeedData.getSeedLiveClasses())
            dao.insertPdfNotes(MasterSyllabusSeedData.getSeedPdfNotes())
            dao.insertDoubtPosts(MasterSyllabusSeedData.getSeedDoubtPosts())
            dao.insertTestAttempt(MasterSyllabusSeedData.getInitialSampleAttempt())
        } else {
            val pdfCount = dao.getPdfNoteCount()
            val seedNotes = MasterSyllabusSeedData.getSeedPdfNotes()
            if (pdfCount < seedNotes.size) {
                dao.insertPdfNotes(seedNotes.drop(pdfCount))
            }
        }
    }

    suspend fun toggleBookmark(question: QuestionEntity) {
        dao.updateQuestion(question.copy(isBookmarked = !question.isBookmarked))
    }

    suspend fun reportDoubtOnQuestion(question: QuestionEntity) {
        dao.updateQuestion(question.copy(isDoubtReported = true))
    }

    suspend fun addCustomQuestion(question: QuestionEntity) {
        dao.insertQuestion(question)
    }

    suspend fun addTestAttempt(attempt: TestAttemptEntity) {
        dao.insertTestAttempt(attempt)
    }

    suspend fun addLiveClass(liveClass: LiveClassEntity) {
        dao.insertLiveClass(liveClass)
    }

    suspend fun addPdfNote(pdfNote: PdfNoteEntity) {
        dao.insertPdfNote(pdfNote)
    }

    suspend fun toggleOfflinePdf(pdfNote: PdfNoteEntity) {
        dao.updatePdfNote(pdfNote.copy(isDownloadedOffline = !pdfNote.isDownloadedOffline))
    }

    suspend fun addDoubtPost(post: DoubtPostEntity) {
        dao.insertDoubtPost(post)
    }

    suspend fun upvoteDoubt(post: DoubtPostEntity) {
        dao.updateDoubtPost(post.copy(upvotes = post.upvotes + 1))
    }
}
