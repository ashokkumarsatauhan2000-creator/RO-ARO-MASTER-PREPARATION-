package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CwBottomNavigationBar
import com.example.ui.components.CwNavigationRail
import com.example.ui.components.CwTopHeader
import com.example.ui.screens.AdminCmsModal
import com.example.ui.screens.DoubtForumScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveClassScreen
import com.example.ui.screens.QuestionBankScreen
import com.example.ui.screens.StudyMaterialsScreen
import com.example.ui.screens.TestSeriesScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CwExamViewModel
import com.example.ui.viewmodel.MainNavTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: CwExamViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = isDarkTheme) {
                CwExamRootApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CwExamRootApp(viewModel: CwExamViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedSubject by viewModel.selectedSubject.collectAsStateWithLifecycle()
    val selectedChapter by viewModel.selectedChapter.collectAsStateWithLifecycle()
    val layoutMode by viewModel.layoutMode.collectAsStateWithLifecycle()
    val showOnlyBookmarked by viewModel.showOnlyBookmarked.collectAsStateWithLifecycle()
    val showAdminModal by viewModel.showAdminModal.collectAsStateWithLifecycle()
    val showNotificationsSheet by viewModel.showNotificationsSheet.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    val allQuestions by viewModel.allQuestions.collectAsStateWithLifecycle()
    val allTestAttempts by viewModel.allTestAttempts.collectAsStateWithLifecycle()
    val allLiveClasses by viewModel.allLiveClasses.collectAsStateWithLifecycle()
    val allPdfNotes by viewModel.allPdfNotes.collectAsStateWithLifecycle()
    val allDoubtPosts by viewModel.allDoubtPosts.collectAsStateWithLifecycle()

    // Handle back button on secondary tabs to return to HOME
    if (currentTab != MainNavTab.HOME) {
        BackHandler {
            viewModel.selectTab(MainNavTab.HOME)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpandedScreen = maxWidth >= 600.dp

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                CwTopHeader(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { query ->
                        viewModel.updateSearchQuery(query)
                        if (query.isNotBlank() && currentTab != MainNavTab.QUESTION_BANK) {
                            viewModel.selectTab(MainNavTab.QUESTION_BANK)
                        }
                    },
                    isDarkTheme = isDarkTheme,
                    onToggleDarkTheme = { viewModel.toggleDarkTheme() },
                    notificationCount = notifications.size,
                    onOpenNotifications = { viewModel.setShowNotificationsSheet(true) },
                    onOpenAdminCms = { viewModel.setShowAdminModal(true) },
                    onLiveBadgeClick = { viewModel.selectTab(MainNavTab.LIVE_CLASSES) }
                )
            },
            bottomBar = {
                if (!isExpandedScreen) {
                    CwBottomNavigationBar(
                        currentTab = currentTab,
                        onSelectTab = { viewModel.selectTab(it) }
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isExpandedScreen) {
                    CwNavigationRail(
                        currentTab = currentTab,
                        onSelectTab = { viewModel.selectTab(it) }
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    when (currentTab) {
                        MainNavTab.HOME -> HomeScreen(
                            questions = allQuestions,
                            selectedSubject = selectedSubject,
                            selectedChapter = selectedChapter,
                            onNavigateToTab = { viewModel.selectTab(it) },
                            onSelectSubjectAndOpenBank = { subj, chap ->
                                viewModel.selectSubject(subj)
                                viewModel.selectChapter(chap)
                                viewModel.selectTab(MainNavTab.QUESTION_BANK)
                            }
                        )

                        MainNavTab.QUESTION_BANK -> QuestionBankScreen(
                            allQuestions = allQuestions,
                            selectedSubject = selectedSubject,
                            selectedChapter = selectedChapter,
                            searchQuery = searchQuery,
                            layoutMode = layoutMode,
                            showOnlyBookmarked = showOnlyBookmarked,
                            onSelectSubject = { viewModel.selectSubject(it) },
                            onSelectChapter = { viewModel.selectChapter(it) },
                            onSetLayoutMode = { viewModel.setQuestionBankLayoutMode(it) },
                            onToggleShowOnlyBookmarked = { viewModel.toggleShowOnlyBookmarked() },
                            onToggleBookmark = { viewModel.toggleBookmark(it) },
                            onReportDoubt = { viewModel.reportDoubt(it) },
                            onGenerateNextBatch = { viewModel.generateNextQuestionBatch() }
                        )

                        MainNavTab.LIVE_CLASSES -> LiveClassScreen(
                            liveClasses = allLiveClasses
                        )

                        MainNavTab.TEST_SERIES -> TestSeriesScreen(
                            allQuestions = allQuestions,
                            testAttempts = allTestAttempts,
                            onSaveTestAttempt = { viewModel.saveTestAttempt(it) }
                        )

                        MainNavTab.E_LIBRARY -> StudyMaterialsScreen(
                            pdfNotes = allPdfNotes,
                            onToggleOffline = { viewModel.toggleOfflinePdf(it) }
                        )

                        MainNavTab.DOUBT_FORUM -> DoubtForumScreen(
                            doubtPosts = allDoubtPosts,
                            onPostDoubt = { name, subj, ref, txt ->
                                viewModel.postStudentDoubt(name, subj, ref, txt)
                            },
                            onUpvoteDoubt = { viewModel.upvoteDoubt(it) }
                        )
                    }
                }
            }
        }
    }

    if (showAdminModal) {
        AdminCmsModal(
            onDismiss = { viewModel.setShowAdminModal(false) },
            onAddQuestion = { subj, chap, qText, tag, a, b, c, d, corr, exp, hasTbl, rows ->
                viewModel.addCustomQuestionFromCms(subj, chap, qText, tag, a, b, c, d, corr, exp, hasTbl, rows)
            },
            onScheduleLiveClass = { title, subj, edu, link, time ->
                viewModel.scheduleLiveClassFromCms(title, subj, edu, link, time)
            },
            onUploadPdf = { title, cat, subj, md ->
                viewModel.uploadPdfFromCms(title, cat, subj, md)
            },
            onSendPushAlert = { title, msg ->
                viewModel.sendPushNotificationFromCms(title, msg)
            }
        )
    }

    if (showNotificationsSheet) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowNotificationsSheet(false) },
            title = {
                Text(
                    text = "🔔 परीक्षा व बैच सूचनाएं (Notifications)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column {
                    notifications.forEach { item ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "${item.title} • ${item.timeText}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.message,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.setShowNotificationsSheet(false) }) {
                    Text("बंद करें")
                }
            }
        )
    }
}
