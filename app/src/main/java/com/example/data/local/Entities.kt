package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val chapter: String,
    val batchNumber: Int,
    val questionNumber: Int,
    val questionText: String,
    val examTag: String,
    val hasMatchTable: Boolean = false,
    val tableHeaderLeft: String = "",
    val tableHeaderRight: String = "",
    val tableRowsJson: String = "", // Pipe-separated rows "A. item1|1. item2;;B. item3|2. item4"
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: String, // "a", "b", "c", "d"
    val detailedExplanation: String,
    val summaryTableHeaderLeft: String = "",
    val summaryTableHeaderRight: String = "",
    val summaryTableRowsJson: String = "",
    val isBookmarked: Boolean = false,
    val isDoubtReported: Boolean = false
)

@Entity(tableName = "test_attempts")
data class TestAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val testTitle: String,
    val paperType: String,
    val totalQuestions: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val unattemptedCount: Int,
    val netScore: Double,
    val maxMarks: Double,
    val accuracyPercent: Double,
    val percentile: Double,
    val weakAreaSubject: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "live_classes")
data class LiveClassEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subject: String,
    val chapter: String,
    val educator: String,
    val videoIdOrLink: String,
    val startTime: String,
    val durationText: String,
    val isLiveNow: Boolean,
    val timestampsText: String, // "00:00 परिचय;;12:30 पाषाण काल स्तरीकरण;;28:45 PYQ विश्लेषण"
    val notesAttachmentTitle: String
)

@Entity(tableName = "pdf_notes")
data class PdfNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "Handwritten Topper Notes", "Official NCERT Gist", "Model Question Papers"
    val subject: String,
    val pageCount: Int,
    val fileSizeText: String,
    val isDownloadedOffline: Boolean = false,
    val contentMarkdown: String
)

@Entity(tableName = "doubt_posts")
data class DoubtPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentName: String,
    val subject: String,
    val questionReference: String,
    val doubtText: String,
    val mentorName: String,
    val mentorReply: String,
    val isVerifiedByTeacher: Boolean,
    val upvotes: Int,
    val timestampText: String
)
