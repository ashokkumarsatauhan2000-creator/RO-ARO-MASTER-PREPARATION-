package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuestionEntity
import com.example.data.seed.MasterSyllabusSeedData
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.NavyBlue
import com.example.ui.theme.NavyBlueDark
import com.example.ui.theme.RoseRed
import com.example.ui.theme.RoseRedDark
import com.example.ui.viewmodel.QuestionBankLayoutMode
import com.example.util.PrintHelper

@Composable
fun QuestionBankScreen(
    allQuestions: List<QuestionEntity>,
    selectedSubject: String,
    selectedChapter: String,
    searchQuery: String,
    layoutMode: QuestionBankLayoutMode,
    showOnlyBookmarked: Boolean,
    onSelectSubject: (String) -> Unit,
    onSelectChapter: (String) -> Unit,
    onSetLayoutMode: (QuestionBankLayoutMode) -> Unit,
    onToggleShowOnlyBookmarked: () -> Unit,
    onToggleBookmark: (QuestionEntity) -> Unit,
    onReportDoubt: (QuestionEntity) -> Unit,
    onGenerateNextBatch: () -> Unit
) {
    val context = LocalContext.current
    val currentSubjectObj = MasterSyllabusSeedData.masterSyllabus.find { it.title == selectedSubject }
        ?: MasterSyllabusSeedData.masterSyllabus.first()

    val filteredQuestions = remember(
        allQuestions,
        selectedSubject,
        selectedChapter,
        searchQuery,
        showOnlyBookmarked
    ) {
        allQuestions.filter { q ->
            val matchesSubjectAndChapter = if (searchQuery.isNotBlank()) {
                q.questionText.contains(searchQuery, ignoreCase = true) ||
                    q.detailedExplanation.contains(searchQuery, ignoreCase = true) ||
                    q.examTag.contains(searchQuery, ignoreCase = true) ||
                    q.chapter.contains(searchQuery, ignoreCase = true)
            } else {
                q.subject == selectedSubject && q.chapter == selectedChapter
            }
            val matchesBookmark = if (showOnlyBookmarked) q.isBookmarked else true
            matchesSubjectAndChapter && matchesBookmark
        }.sortedBy { it.questionNumber }
    }

    var quizCurrentIndex by remember(filteredQuestions) { mutableIntStateOf(0) }
    var quizSelectedOption by remember(quizCurrentIndex, filteredQuestions) { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Control Bar: Subject Chips + Chapter Chips + Mode Selector + Print/Export Actions
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                // 8 GS + 1 Hindi Horizontal Subject Selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MasterSyllabusSeedData.masterSyllabus.forEach { subj ->
                        val isSelected = subj.title == selectedSubject
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectSubject(subj.title) },
                            label = {
                                Text(
                                    text = "${subj.emoji} ${subj.shortTitle}",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Chapter Horizontal Selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    currentSubjectObj.chapters.forEach { chap ->
                        val isSelected = chap == selectedChapter
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectChapter(chap) },
                            label = {
                                Text(
                                    text = chap,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = IndigoAccent.copy(alpha = 0.16f),
                                selectedLabelColor = IndigoAccent
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Mode Switcher & Print/Word Export Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mode 2: 2-Column Ghatna Chakra Mode
                    Surface(
                        onClick = { onSetLayoutMode(QuestionBankLayoutMode.TWO_COLUMN_PURVAVLOKAN) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (layoutMode == QuestionBankLayoutMode.TWO_COLUMN_PURVAVLOKAN) NavyBlue else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.testTag("mode_2_column_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = null,
                                tint = if (layoutMode == QuestionBankLayoutMode.TWO_COLUMN_PURVAVLOKAN) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "📰 2-Column पूर्वावलोकन मोड",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (layoutMode == QuestionBankLayoutMode.TWO_COLUMN_PURVAVLOKAN) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Mode 1: Interactive Quiz Mode
                    Surface(
                        onClick = { onSetLayoutMode(QuestionBankLayoutMode.INTERACTIVE_QUIZ) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (layoutMode == QuestionBankLayoutMode.INTERACTIVE_QUIZ) NavyBlue else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.testTag("mode_interactive_quiz_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Quiz,
                                contentDescription = null,
                                tint = if (layoutMode == QuestionBankLayoutMode.INTERACTIVE_QUIZ) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "📱 इंटरैक्टिव क्विज़ मोड",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (layoutMode == QuestionBankLayoutMode.INTERACTIVE_QUIZ) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Bookmarks Filter
                    Surface(
                        onClick = onToggleShowOnlyBookmarked,
                        shape = RoundedCornerShape(8.dp),
                        color = if (showOnlyBookmarked) AmberGold else MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔖 बुकमार्क",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (showOnlyBookmarked) NavyBlueDark else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Print 2-Column Sheet Button
                    Surface(
                        onClick = {
                            PrintHelper.printTwoColumnPurvavlokanSheet(
                                context = context,
                                subject = selectedSubject,
                                chapter = selectedChapter,
                                questions = filteredQuestions
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldGreen,
                        modifier = Modifier.testTag("print_2col_sheet_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Print,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "प्रिंट निकालें (A4 2-Col)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Copy Word Markdown Button
                    Surface(
                        onClick = {
                            PrintHelper.copyWordMarkdownToClipboard(
                                context = context,
                                subject = selectedSubject,
                                chapter = selectedChapter,
                                questions = filteredQuestions
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        color = IndigoAccent
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "MS Word कॉपी",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Share Button
                    IconButton(
                        onClick = {
                            PrintHelper.shareWordMarkdown(
                                context = context,
                                subject = selectedSubject,
                                chapter = selectedChapter,
                                questions = filteredQuestions
                            )
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "साझा करें",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Main Content Area
        if (filteredQuestions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📖", fontSize = 42.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "इस अध्याय में अभी कोई प्रश्न फ़िल्टर से मेल नहीं खाता।",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = onGenerateNextBatch) {
                        Text("प्रथम 25-प्रश्न बैच जनरेट करें")
                    }
                }
            }
        } else if (layoutMode == QuestionBankLayoutMode.INTERACTIVE_QUIZ) {
            // Mode 1: Interactive Quiz Mode
            val safeIndex = quizCurrentIndex.coerceIn(0, filteredQuestions.lastIndex)
            val q = filteredQuestions[safeIndex]

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = NavyBlue.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "प्रश्न ${safeIndex + 1} / ${filteredQuestions.size} (मूल क्र. ${q.questionNumber})",
                                        color = NavyBlue,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Surface(
                                    color = RoseRed.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = q.examTag,
                                        color = RoseRed,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "प्रश्न संख्या [${q.questionNumber.toString().padStart(2, '0')}]: ${q.questionText}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = RoseRedDark,
                                lineHeight = 23.sp
                            )

                            if (q.hasMatchTable && q.tableRowsJson.isNotBlank()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                MatchTwoColumnTableComposable(
                                    headerLeft = q.tableHeaderLeft,
                                    headerRight = q.tableHeaderRight,
                                    rowsJson = q.tableRowsJson
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            val opts = listOf(
                                "a" to q.optionA,
                                "b" to q.optionB,
                                "c" to q.optionC,
                                "d" to q.optionD
                            )

                            opts.forEach { (key, text) ->
                                val isChosen = quizSelectedOption == key
                                val isRight = q.correctOption.equals(key, ignoreCase = true)
                                val bgColor = when {
                                    quizSelectedOption == null -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    isRight -> EmeraldGreen.copy(alpha = 0.2f)
                                    isChosen -> RoseRed.copy(alpha = 0.2f)
                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                                }
                                val borderColor = when {
                                    quizSelectedOption == null -> MaterialTheme.colorScheme.outline
                                    isRight -> EmeraldGreen
                                    isChosen -> RoseRed
                                    else -> MaterialTheme.colorScheme.outline
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(bgColor)
                                        .border(1.5.dp, borderColor, RoundedCornerShape(10.dp))
                                        .clickable {
                                            if (quizSelectedOption == null) {
                                                quizSelectedOption = key
                                            }
                                        }
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "($key) $text",
                                        fontSize = 14.sp,
                                        fontWeight = if (isChosen || (quizSelectedOption != null && isRight)) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }

                            if (quizSelectedOption != null) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Surface(
                                    color = EmeraldGreen.copy(alpha = 0.10f),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, EmeraldGreen.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "उत्तर: (${q.correctOption})",
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldGreen,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        FormattedExplanationText(explanation = q.detailedExplanation)
                                        if (q.summaryTableRowsJson.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            MatchTwoColumnTableComposable(
                                                headerLeft = q.summaryTableHeaderLeft,
                                                headerRight = q.summaryTableHeaderRight,
                                                rowsJson = q.summaryTableRowsJson
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { if (quizCurrentIndex > 0) quizCurrentIndex-- },
                                    enabled = quizCurrentIndex > 0
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("पिछला")
                                }

                                Row {
                                    IconButton(onClick = { onToggleBookmark(q) }) {
                                        Icon(
                                            imageVector = if (q.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                            contentDescription = "बुकमार्क",
                                            tint = if (q.isBookmarked) AmberGold else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    IconButton(onClick = { onReportDoubt(q) }) {
                                        Icon(
                                            imageVector = Icons.Default.Flag,
                                            contentDescription = "डाउट रिपोर्ट करें",
                                            tint = if (q.isDoubtReported) RoseRed else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        if (quizCurrentIndex < filteredQuestions.lastIndex) {
                                            quizCurrentIndex++
                                        } else {
                                            onGenerateNextBatch()
                                        }
                                    }
                                ) {
                                    Text(if (quizCurrentIndex < filteredQuestions.lastIndex) "अगला प्रश्न" else "अगला बैच +")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Mode 2: 📰 'Ghatna Chakra / 2-Column Print Mode'
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val useTwoColumnsOnScreen = maxWidth >= 520.dp
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Print Sheet Header Banner
                    item {
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.5.dp, NavyBlue, RoundedCornerShape(10.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🏛️ घटनाचक्र पूर्वावलोकन एवं परीक्षा वाणी (MS Word 2-Column Print Layout)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "विषय: $selectedSubject  |  अध्याय: $selectedChapter",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    if (useTwoColumnsOnScreen) {
                        val pairedRows = filteredQuestions.chunked(2)
                        items(pairedRows) { pair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                PurvavlokanCompactQuestionCard(
                                    question = pair[0],
                                    onToggleBookmark = { onToggleBookmark(pair[0]) },
                                    onReportDoubt = { onReportDoubt(pair[0]) },
                                    modifier = Modifier.weight(1f)
                                )
                                if (pair.size > 1) {
                                    PurvavlokanCompactQuestionCard(
                                        question = pair[1],
                                        onToggleBookmark = { onToggleBookmark(pair[1]) },
                                        onReportDoubt = { onReportDoubt(pair[1]) },
                                        modifier = Modifier.weight(1f)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    } else {
                        items(filteredQuestions, key = { it.id }) { q ->
                            PurvavlokanCompactQuestionCard(
                                question = q,
                                onToggleBookmark = { onToggleBookmark(q) },
                                onReportDoubt = { onReportDoubt(q) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // Official Batch Status Box & "आगे / Next Batch" Automation Button
                    item {
                        val firstQ = filteredQuestions.firstOrNull()?.questionNumber?.toString()?.padStart(2, '0') ?: "01"
                        val lastQ = filteredQuestions.lastOrNull()?.questionNumber?.toString()?.padStart(2, '0') ?: "77"

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = NavyBlueDark),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "==================================================",
                                    fontFamily = FontFamily.Monospace,
                                    color = AmberGold,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "वर्तमान विषय: $selectedSubject\n" +
                                        "वर्तमान अध्याय: $selectedChapter\n" +
                                        "प्रगति: प्रश्न $firstQ से $lastQ / इस अध्याय के संभावित लक्ष्य: ~300\n" +
                                        "स्थिति: अध्याय जारी है",
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "==================================================",
                                    fontFamily = FontFamily.Monospace,
                                    color = AmberGold,
                                    fontSize = 11.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = onGenerateNextBatch,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = AmberGold,
                                            contentColor = NavyBlueDark
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("generate_next_batch_btn")
                                    ) {
                                        Text(
                                            text = "आगे / अगला सेट जोड़ें (Next Batch)",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp
                                        )
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            PrintHelper.printTwoColumnPurvavlokanSheet(
                                                context = context,
                                                subject = selectedSubject,
                                                chapter = selectedChapter,
                                                questions = filteredQuestions
                                            )
                                        },
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                                    ) {
                                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("प्रिंट", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PurvavlokanCompactQuestionCard(
    question: QuestionEntity,
    onToggleBookmark: () -> Unit,
    onReportDoubt: () -> Unit,
    modifier: Modifier = Modifier
) {
    val numStr = question.questionNumber.toString().padStart(2, '0')
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
            .testTag("question_card_${question.questionNumber}")
    ) {
        Column(modifier = Modifier.padding(11.dp)) {
            // Question Header with Red Bold text + PYQ Tag + Bookmark/Doubt icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = RoseRedDark,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("प्रश्न संख्या [$numStr]: ${question.questionText} ")
                        }
                        withStyle(
                            SpanStyle(
                                color = IndigoAccent,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp
                            )
                        ) {
                            append(question.examTag)
                        }
                    },
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.weight(1f)
                )

                Row {
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (question.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "बुकमार्क",
                            tint = if (question.isBookmarked) AmberGold else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    IconButton(
                        onClick = onReportDoubt,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = "डाउट",
                            tint = if (question.isDoubtReported) RoseRed else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // 2-Column Match Table if present
            if (question.hasMatchTable && question.tableRowsJson.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                MatchTwoColumnTableComposable(
                    headerLeft = question.tableHeaderLeft,
                    headerRight = question.tableHeaderRight,
                    rowsJson = question.tableRowsJson
                )
                Text(
                    text = "कूट:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Compact No-Spacing Options (a), (b), (c), (d)
            Text(text = "(a) ${question.optionA}", fontSize = 13.sp, lineHeight = 18.sp)
            Text(text = "(b) ${question.optionB}", fontSize = 13.sp, lineHeight = 18.sp)
            Text(text = "(c) ${question.optionC}", fontSize = 13.sp, lineHeight = 18.sp)
            Text(text = "(d) ${question.optionD}", fontSize = 13.sp, lineHeight = 18.sp)

            Spacer(modifier = Modifier.height(4.dp))

            // Bold Answer
            Text(
                text = "उत्तर: (${question.correctOption})",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = EmeraldGreen
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Detailed Explanation Box with Bold Key Terms
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
                    .padding(8.dp)
            ) {
                Column {
                    FormattedExplanationText(explanation = question.detailedExplanation)
                    if (question.summaryTableRowsJson.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        MatchTwoColumnTableComposable(
                            headerLeft = question.summaryTableHeaderLeft,
                            headerRight = question.summaryTableHeaderRight,
                            rowsJson = question.summaryTableRowsJson
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun FormattedExplanationText(explanation: String) {
    val parts = explanation.split("**")
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)) {
                append("विस्तृत व्याख्या: ")
            }
            parts.forEachIndexed { index, part ->
                if (index % 2 == 1) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)) {
                        append(part)
                    }
                } else {
                    append(part)
                }
            }
        },
        fontSize = 12.5.sp,
        lineHeight = 18.5.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun MatchTwoColumnTableComposable(
    headerLeft: String,
    headerRight: String,
    rowsJson: String
) {
    val rows = rowsJson.split(";;").filter { it.isNotBlank() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyBlue.copy(alpha = 0.14f))
                .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
            Text(
                text = headerLeft,
                fontWeight = FontWeight.Bold,
                fontSize = 11.5.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = headerRight,
                fontWeight = FontWeight.Bold,
                fontSize = 11.5.sp,
                modifier = Modifier.weight(1.2f)
            )
        }
        rows.forEach { rowStr ->
            val cols = rowStr.split("|")
            if (cols.size >= 2) {
                HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = cols[0],
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = cols[1],
                        fontSize = 11.5.sp,
                        modifier = Modifier.weight(1.2f)
                    )
                }
            }
        }
    }
}
