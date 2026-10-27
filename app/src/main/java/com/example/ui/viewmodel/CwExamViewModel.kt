package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CwExamRepository
import com.example.data.local.DoubtPostEntity
import com.example.data.local.LiveClassEntity
import com.example.data.local.PdfNoteEntity
import com.example.data.local.QuestionEntity
import com.example.data.local.TestAttemptEntity
import com.example.data.seed.MasterSyllabusSeedData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainNavTab(val titleHi: String, val badgeText: String = "") {
    HOME("होम"),
    QUESTION_BANK("प्रश्न-बैंक"),
    LIVE_CLASSES("लाइव क्लास", "LIVE"),
    TEST_SERIES("टेस्ट सीरीज़"),
    E_LIBRARY("ई-लाइब्रेरी"),
    DOUBT_FORUM("डाउट फ़ोरम")
}

enum class QuestionBankLayoutMode {
    TWO_COLUMN_PURVAVLOKAN,
    INTERACTIVE_QUIZ
}

data class PushAlertNotification(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val message: String,
    val timeText: String = "अभी"
)

class CwExamViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CwExamRepository

    val allQuestions: StateFlow<List<QuestionEntity>>
    val allTestAttempts: StateFlow<List<TestAttemptEntity>>
    val allLiveClasses: StateFlow<List<LiveClassEntity>>
    val allPdfNotes: StateFlow<List<PdfNoteEntity>>
    val allDoubtPosts: StateFlow<List<DoubtPostEntity>>

    private val _currentTab = MutableStateFlow(MainNavTab.HOME)
    val currentTab: StateFlow<MainNavTab> = _currentTab.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSubject = MutableStateFlow(MasterSyllabusSeedData.masterSyllabus.first().title)
    val selectedSubject: StateFlow<String> = _selectedSubject.asStateFlow()

    private val _selectedChapter = MutableStateFlow(MasterSyllabusSeedData.masterSyllabus.first().chapters.first())
    val selectedChapter: StateFlow<String> = _selectedChapter.asStateFlow()

    private val _layoutMode = MutableStateFlow(QuestionBankLayoutMode.TWO_COLUMN_PURVAVLOKAN)
    val layoutMode: StateFlow<QuestionBankLayoutMode> = _layoutMode.asStateFlow()

    private val _showOnlyBookmarked = MutableStateFlow(false)
    val showOnlyBookmarked: StateFlow<Boolean> = _showOnlyBookmarked.asStateFlow()

    private val _showAdminModal = MutableStateFlow(false)
    val showAdminModal: StateFlow<Boolean> = _showAdminModal.asStateFlow()

    private val _showNotificationsSheet = MutableStateFlow(false)
    val showNotificationsSheet: StateFlow<Boolean> = _showNotificationsSheet.asStateFlow()

    private val _notifications = MutableStateFlow(
        listOf(
            PushAlertNotification(
                id = 1L,
                title = "🔴 LIVE: पाषाण काल व सैंधव सभ्यता मैराथन",
                message = "डॉ. आलोक पाण्डेय की 2-कॉलम पूर्वावलोकन लाइव क्लास शुरू हो चुकी है। तुरंत जुड़ें!",
                timeText = "10 मिनट पूर्व"
            ),
            PushAlertNotification(
                id = 2L,
                title = "📝 RO/ARO फुल मॉक टेस्ट #02 उपलब्ध",
                message = "आयोग के नवीनतम कूट एवं कथन-कारण पैटर्न पर आधारित नया टेस्ट लाइव हो गया है।",
                timeText = "2 घंटे पूर्व"
            ),
            PushAlertNotification(
                id = 3L,
                title = "🎯 UPPSC RO/ARO परीक्षा तिथि अलर्ट",
                message = "लक्ष्य 2026 के लिए दैनिक 25-25 प्रश्नों का पूर्वावलोकन बैच नियमित रूप से हल करें।",
                timeText = "आज सुबह"
            )
        )
    )
    val notifications: StateFlow<List<PushAlertNotification>> = _notifications.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application).cwExamDao()
        repository = CwExamRepository(dao)

        allQuestions = repository.allQuestions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        allTestAttempts = repository.allTestAttempts.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        allLiveClasses = repository.allLiveClasses.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        allPdfNotes = repository.allPdfNotes.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        allDoubtPosts = repository.allDoubtPosts.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        viewModelScope.launch {
            repository.ensureSeedDataLoaded()
        }
    }

    fun selectTab(tab: MainNavTab) {
        _currentTab.value = tab
    }

    fun toggleDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectSubject(subjectTitle: String) {
        _selectedSubject.value = subjectTitle
        val syllabusObj = MasterSyllabusSeedData.masterSyllabus.find { it.title == subjectTitle }
        syllabusObj?.chapters?.firstOrNull()?.let {
            _selectedChapter.value = it
        }
    }

    fun selectChapter(chapterTitle: String) {
        _selectedChapter.value = chapterTitle
    }

    fun setQuestionBankLayoutMode(mode: QuestionBankLayoutMode) {
        _layoutMode.value = mode
    }

    fun toggleShowOnlyBookmarked() {
        _showOnlyBookmarked.value = !_showOnlyBookmarked.value
    }

    fun setShowAdminModal(show: Boolean) {
        _showAdminModal.value = show
    }

    fun setShowNotificationsSheet(show: Boolean) {
        _showNotificationsSheet.value = show
    }

    fun toggleBookmark(question: QuestionEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(question)
        }
    }

    fun reportDoubt(question: QuestionEntity) {
        viewModelScope.launch {
            repository.reportDoubtOnQuestion(question)
            repository.addDoubtPost(
                DoubtPostEntity(
                    studentName = "अभ्यर्थी (आप)",
                    subject = question.subject.substringBefore(" ("),
                    questionReference = "प्रश्न संख्या [${question.questionNumber}]: ${question.questionText.take(45)}...",
                    doubtText = "कृपया इस प्रश्न के आयोग द्वारा मान्य नवीनतम स्रोत एवं तथ्य की पुष्टि करें।",
                    mentorName = "CW EXAM विशेषज्ञ पैनल",
                    mentorReply = "आपका डाउट प्राप्त हुआ है। घटनाचक्र पूर्वावलोकन एवं मानक स्रोत के अनुसार दी गई व्याख्या पूर्णतः सत्यापित है।",
                    isVerifiedByTeacher = true,
                    upvotes = 1,
                    timestampText = "अभी"
                )
            )
        }
    }

    fun generateNextQuestionBatch() {
        viewModelScope.launch {
            val currentList = allQuestions.value.filter {
                it.subject == _selectedSubject.value && it.chapter == _selectedChapter.value
            }
            val nextNumber = (currentList.maxOfOrNull { it.questionNumber } ?: 0) + 1
            val batchNum = ((nextNumber - 1) / 25) + 1
            val newQuestions = listOf(
                QuestionEntity(
                    subject = _selectedSubject.value,
                    chapter = _selectedChapter.value,
                    batchNumber = batchNum,
                    questionNumber = nextNumber,
                    questionText = "${_selectedChapter.value} के संदर्भ में आयोग द्वारा पूछे गए प्रामाणिक कालानुक्रमिक एवं विश्लेषणात्मक तथ्य की पहचान कीजिए:",
                    examTag = "[UPPSC RO/ARO अति-संभावित बैच #$batchNum]",
                    optionA = "प्राथमिक पुरातात्विक एवं संवैधानिक साक्ष्य पूर्णतः सुमेलित हैं",
                    optionB = "केवल द्वितीयक साहित्यिक साक्ष्य मान्य हैं",
                    optionC = "उत्खनन स्तरों में कालक्रमिक व्युत्क्रम पाया गया है",
                    optionD = "उपर्युक्त में से कोई नहीं",
                    correctOption = "a",
                    detailedExplanation = "**घटनाचक्र पूर्वावलोकन** एवं **एनसीईआरटी (NCERT)** के प्रामाणिक विश्लेषण के अनुसार **${_selectedChapter.value}** से संबंधित यह प्रश्न आगामी **UPPSC RO/ARO एवं PCS प्रारंभिक परीक्षा** के लिए अति-संभावित श्रेणी में रखा गया है।"
                ),
                QuestionEntity(
                    subject = _selectedSubject.value,
                    chapter = _selectedChapter.value,
                    batchNumber = batchNum,
                    questionNumber = nextNumber + 1,
                    questionText = "सूची-I को सूची-II से सुमेलित कीजिए (${_selectedChapter.value} - विशेष पुनरीक्षण):",
                    examTag = "[UPPSC PCS / RO/ARO कूट प्रश्न]",
                    hasMatchTable = true,
                    tableHeaderLeft = "सूची-I (प्रमुख संदर्भ/तथ्य)",
                    tableHeaderRight = "सूची-II (संबंधित विशेषता/स्रोत)",
                    tableRowsJson = "A. प्राथमिक साक्ष्य|1. प्रामाणिक अभिलेखीय/पुरातात्विक स्रोत;;B. मानक ग्रंथ|2. आयोग द्वारा मान्य संदर्भ;;C. क्षेत्रीय विस्तार|3. उत्तर प्रदेश एवं गंगा घाटी क्षेत्र;;D. कालानुक्रम|4. वैज्ञानिक कार्बन-14 तिथि निर्धारण",
                    optionA = "A-1, B-2, C-3, D-4",
                    optionB = "A-2, B-1, C-4, D-3",
                    optionC = "A-3, B-4, C-1, D-2",
                    optionD = "A-4, B-3, C-2, D-1",
                    correctOption = "a",
                    detailedExplanation = "आयोग की नवीनतम परीक्षा प्रणाली में **कूट मिलान** एवं **कथन-कारण** के प्रश्नों का अनुपात 60% से अधिक रहता है। **${_selectedChapter.value}** के अंतर्गत चारों युग्म पूर्णतः सुमेलित हैं।"
                )
            )
            newQuestions.forEach { repository.addCustomQuestion(it) }
        }
    }

    fun addCustomQuestionFromCms(
        subject: String,
        chapter: String,
        questionText: String,
        examTag: String,
        optionA: String,
        optionB: String,
        optionC: String,
        optionD: String,
        correctOption: String,
        explanation: String,
        hasTable: Boolean,
        tableRows: String
    ) {
        viewModelScope.launch {
            val currentMax = allQuestions.value
                .filter { it.subject == subject && it.chapter == chapter }
                .maxOfOrNull { it.questionNumber } ?: 0
            repository.addCustomQuestion(
                QuestionEntity(
                    subject = subject,
                    chapter = chapter,
                    batchNumber = (currentMax / 25) + 1,
                    questionNumber = currentMax + 1,
                    questionText = questionText,
                    examTag = examTag.ifBlank { "[UPPSC RO/ARO अति-संभावित]" },
                    hasMatchTable = hasTable,
                    tableHeaderLeft = if (hasTable) "सूची-I" else "",
                    tableHeaderRight = if (hasTable) "सूची-II" else "",
                    tableRowsJson = if (hasTable) tableRows else "",
                    optionA = optionA,
                    optionB = optionB,
                    optionC = optionC,
                    optionD = optionD,
                    correctOption = correctOption.lowercase(),
                    detailedExplanation = explanation
                )
            )
        }
    }

    fun scheduleLiveClassFromCms(
        title: String,
        subject: String,
        educator: String,
        videoLink: String,
        startTime: String
    ) {
        viewModelScope.launch {
            repository.addLiveClass(
                LiveClassEntity(
                    title = title,
                    subject = subject,
                    chapter = "विशेष सत्र",
                    educator = educator,
                    videoIdOrLink = videoLink,
                    startTime = startTime,
                    durationText = "01:30:00",
                    isLiveNow = true,
                    timestampsText = "00:00 परिचय एवं मुख्य अवधारणा;;20:00 महत्वपूर्ण PYQ चर्चा;;45:00 छात्र डाउट समाधान",
                    notesAttachmentTitle = "${title.take(15)}_Notes.pdf"
                )
            )
        }
    }

    fun uploadPdfFromCms(
        title: String,
        category: String,
        subject: String,
        contentMarkdown: String
    ) {
        viewModelScope.launch {
            repository.addPdfNote(
                PdfNoteEntity(
                    title = title,
                    category = category,
                    subject = subject,
                    pageCount = 24,
                    fileSizeText = "3.5 MB",
                    isDownloadedOffline = true,
                    contentMarkdown = contentMarkdown
                )
            )
        }
    }

    fun sendPushNotificationFromCms(title: String, message: String) {
        _notifications.value = listOf(
            PushAlertNotification(title = title, message = message, timeText = "अभी")
        ) + _notifications.value
    }

    fun saveTestAttempt(attempt: TestAttemptEntity) {
        viewModelScope.launch {
            repository.addTestAttempt(attempt)
        }
    }

    fun toggleOfflinePdf(pdfNote: PdfNoteEntity) {
        viewModelScope.launch {
            repository.toggleOfflinePdf(pdfNote)
        }
    }

    fun postStudentDoubt(studentName: String, subject: String, reference: String, doubtText: String) {
        viewModelScope.launch {
            repository.addDoubtPost(
                DoubtPostEntity(
                    studentName = studentName.ifBlank { "सिविल सेवा अभ्यर्थी" },
                    subject = subject,
                    questionReference = reference.ifBlank { "सामान्य पाठ्यक्रम जिज्ञासा" },
                    doubtText = doubtText,
                    mentorName = "डॉ. आलोक पाण्डेय (वरिष्ठ मेंटर)",
                    mentorReply = "उत्कृष्ट प्रश्न! आयोग के मानक स्रोतों (एनसीईआरटी एवं घटनाचक्र पूर्वावलोकन) के अनुसार इसके मूल तथ्य को 2-कॉलम नोट्स में रेखांकित किया गया है।",
                    isVerifiedByTeacher = true,
                    upvotes = 1,
                    timestampText = "अभी"
                )
            )
        }
    }

    fun upvoteDoubt(post: DoubtPostEntity) {
        viewModelScope.launch {
            repository.upvoteDoubt(post)
        }
    }
}
