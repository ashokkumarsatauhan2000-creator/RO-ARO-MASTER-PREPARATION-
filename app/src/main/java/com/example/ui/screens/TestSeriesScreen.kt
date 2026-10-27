package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuestionEntity
import com.example.data.local.TestAttemptEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.NavyBlue
import com.example.ui.theme.NavyBlueDark
import com.example.ui.theme.ReviewPurple
import com.example.ui.theme.RoseRed
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TestSeriesScreen(
    allQuestions: List<QuestionEntity>,
    testAttempts: List<TestAttemptEntity>,
    onSaveTestAttempt: (TestAttemptEntity) -> Unit
) {
    var isSimulatorActive by remember { mutableStateOf(false) }
    var activeTestTitle by remember { mutableStateOf("UPPSC RO/ARO GS पेपर-1 एवं सामान्य हिंदी OMR सिमुलेटर") }
    var activePaperType by remember { mutableStateOf("GS Paper-1 (140 पैटर्न) • -0.33 नेगेटिव मार्किंग") }
    var currentQuestionIdx by remember { mutableIntStateOf(0) }
    var remainingSeconds by remember { mutableIntStateOf(15 * 60) } // 15 min rapid simulator

    val selectedAnswers = remember { mutableStateMapOf<Int, String>() }
    val markedForReview = remember { mutableStateMapOf<Int, Boolean>() }
    val visitedQuestions = remember { mutableStateMapOf<Int, Boolean>() }

    var latestSubmittedAttempt by remember { mutableStateOf<TestAttemptEntity?>(null) }

    val testQuestions = remember(allQuestions) {
        allQuestions.take(15)
    }

    LaunchedEffect(isSimulatorActive, remainingSeconds) {
        if (isSimulatorActive && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
        }
    }

    if (isSimulatorActive && testQuestions.isNotEmpty()) {
        val safeIdx = currentQuestionIdx.coerceIn(0, testQuestions.lastIndex)
        visitedQuestions[safeIdx] = true
        val currentQ = testQuestions[safeIdx]

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // OMR Simulator Top Bar with Timer & Submit
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyBlueDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = activeTestTitle,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "सही: +1.0 | गलत: -0.33 (UPPSC आधिकारिक स्कीम)",
                                color = AmberGold,
                                fontSize = 11.sp
                            )
                        }

                        Surface(
                            color = RoseRed,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                val mins = (remainingSeconds / 60).toString().padStart(2, '0')
                                val secs = (remainingSeconds % 60).toString().padStart(2, '0')
                                Text(
                                    text = "$mins:$secs",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            // Question Palette (Green: Answered, Red: Unanswered, Purple: Review, Gray: Not Visited)
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "🎯 OMR प्रश्न पैलेट (Question Palette):",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            PaletteLegendChip("हल किया", EmeraldGreen)
                            PaletteLegendChip("छोड़ा गया", RoseRed)
                            PaletteLegendChip("पुनर्विचार", ReviewPurple)
                            PaletteLegendChip("अपठित", Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            testQuestions.forEachIndexed { idx, _ ->
                                val isAnswered = selectedAnswers.containsKey(idx)
                                val isReview = markedForReview[idx] == true
                                val isVisited = visitedQuestions[idx] == true

                                val circleColor = when {
                                    isReview -> ReviewPurple
                                    isAnswered -> EmeraldGreen
                                    isVisited -> RoseRed
                                    else -> Color.Gray.copy(alpha = 0.45f)
                                }

                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(circleColor)
                                        .border(
                                            width = if (idx == safeIdx) 2.5.dp else 0.dp,
                                            color = if (idx == safeIdx) AmberGold else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { currentQuestionIdx = idx },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = (idx + 1).toString().padStart(2, '0'),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Current Question & OMR Bubble Selection
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "प्रश्न संख्या [${safeIdx + 1}/${testQuestions.size}]: ${currentQ.questionText} ${currentQ.examTag}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = RoseRed
                        )

                        if (currentQ.hasMatchTable && currentQ.tableRowsJson.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            MatchTwoColumnTableComposable(
                                headerLeft = currentQ.tableHeaderLeft,
                                headerRight = currentQ.tableHeaderRight,
                                rowsJson = currentQ.tableRowsJson
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val opts = listOf(
                            "a" to currentQ.optionA,
                            "b" to currentQ.optionB,
                            "c" to currentQ.optionC,
                            "d" to currentQ.optionD
                        )

                        opts.forEach { (key, label) ->
                            val isSelected = selectedAnswers[safeIdx] == key
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) NavyBlue.copy(alpha = 0.14f)
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) NavyBlue else MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedAnswers[safeIdx] = key }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) NavyBlue else Color.Transparent)
                                        .border(1.5.dp, NavyBlue, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = key.uppercase(),
                                        color = if (isSelected) Color.White else NavyBlue,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = label,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Mark for Review + Navigation + Submit Test
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val curr = markedForReview[safeIdx] == true
                                    markedForReview[safeIdx] = !curr
                                }
                            ) {
                                Icon(Icons.Default.BookmarkAdd, contentDescription = null, tint = ReviewPurple, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("पुनर्विचार (Review)", color = ReviewPurple, fontSize = 12.sp)
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (safeIdx < testQuestions.lastIndex) {
                                    Button(onClick = { currentQuestionIdx = safeIdx + 1 }) {
                                        Text("सेव व अगला")
                                    }
                                }
                                Button(
                                    onClick = {
                                        var correct = 0
                                        var wrong = 0
                                        testQuestions.forEachIndexed { index, q ->
                                            val ans = selectedAnswers[index]
                                            if (ans != null) {
                                                if (ans.equals(q.correctOption, ignoreCase = true)) {
                                                    correct++
                                                } else {
                                                    wrong++
                                                }
                                            }
                                        }
                                        val unattempted = testQuestions.size - (correct + wrong)
                                        val rawScore = correct * 1.0 - (wrong * 0.33)
                                        val roundedScore = ((rawScore * 100.0).roundToInt() / 100.0).coerceAtLeast(0.0)
                                        val attempted = correct + wrong
                                        val accuracy = if (attempted > 0) ((correct.toDouble() / attempted) * 1000.0).roundToInt() / 10.0 else 0.0
                                        val percentile = (75.0 + (accuracy * 0.23)).coerceAtMost(99.8)

                                        val attempt = TestAttemptEntity(
                                            testTitle = activeTestTitle,
                                            paperType = activePaperType,
                                            totalQuestions = testQuestions.size,
                                            correctCount = correct,
                                            incorrectCount = wrong,
                                            unattemptedCount = unattempted,
                                            netScore = roundedScore,
                                            maxMarks = testQuestions.size.toDouble(),
                                            accuracyPercent = accuracy,
                                            percentile = ((percentile * 10.0).roundToInt() / 10.0),
                                            weakAreaSubject = "जनगणना एवं उत्तर प्रदेश विशेष"
                                        )
                                        onSaveTestAttempt(attempt)
                                        latestSubmittedAttempt = attempt
                                        isSimulatorActive = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                    modifier = Modifier.testTag("submit_omr_test_btn")
                                ) {
                                    Text("OMR सबमिट करें", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        // Test Series Dashboard & Post-Test Analytics View
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Post-Test Analytics Banner if available
            val displayAttempt = latestSubmittedAttempt ?: testAttempts.firstOrNull()
            if (displayAttempt != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, EmeraldGreen, RoundedCornerShape(18.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Analytics, contentDescription = null, tint = EmeraldGreen)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "📊 पोस्ट-टेस्ट एनालिटिक्स एवं स्कोरकार्ड",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                                Surface(
                                    color = AmberGold.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = "रैंक #14 / 12,450",
                                        color = NavyBlueDark,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                AnalyticsMetricBox("नेट स्कोर (-0.33)", "${displayAttempt.netScore} / ${displayAttempt.maxMarks.toInt()}", EmeraldGreen)
                                AnalyticsMetricBox("शुद्धता (Accuracy)", "${displayAttempt.accuracyPercent}%", IndigoAccent)
                                AnalyticsMetricBox("परसेंटाइल", "${displayAttempt.percentile}%ile", AmberGold)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "✔️ सही: ${displayAttempt.correctCount}  |  ❌ गलत: ${displayAttempt.incorrectCount}  |  ⚪ अनुत्तरित: ${displayAttempt.unattemptedCount}",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "⚠️ सुधार क्षेत्र (Weak Area): ${displayAttempt.weakAreaSubject}",
                                fontSize = 12.sp,
                                color = RoseRed,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Full Length Test Series Cards
            item {
                Text(
                    text = "📝 UPPSC RO/ARO एवं PCS टेस्ट सीरीज़ (आयोग पैटर्न)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                MockTestLauncherCard(
                    title = "UPPSC RO/ARO GS पेपर-1 फुल सिमुलेटर (140 प्रश्न पैटर्न)",
                    subtitle = "इतिहास, राजव्यवस्था, भूगोल, यूपी विशेष, विज्ञान व जनगणना • -0.33 नेगेटिव मार्किंग",
                    badge = "अति-महत्वपूर्ण • पेपर 1",
                    accentColor = NavyBlue,
                    onStart = {
                        activeTestTitle = "UPPSC RO/ARO GS पेपर-1 सिमुलेटर"
                        activePaperType = "GS Paper-1 (140 Marks Pattern)"
                        selectedAnswers.clear()
                        markedForReview.clear()
                        visitedQuestions.clear()
                        currentQuestionIdx = 0
                        remainingSeconds = 15 * 60
                        isSimulatorActive = true
                    }
                )
            }

            item {
                MockTestLauncherCard(
                    title = "UPPSC RO/ARO सामान्य हिंदी पेपर-2 सिमुलेटर (60 प्रश्न पैटर्न)",
                    subtitle = "विलोम, वाक्य-वर्तनी, अनेक शब्द, तत्सम-तद्भव, विशेषण-विशेष्य, पर्यायवाची",
                    badge = "60/60 टारगेट • पेपर 2",
                    accentColor = IndigoAccent,
                    onStart = {
                        activeTestTitle = "RO/ARO सामान्य हिंदी पेपर-2 (60 अंक पैटर्न)"
                        activePaperType = "Hindi Paper-2 (60 Marks Pattern)"
                        selectedAnswers.clear()
                        markedForReview.clear()
                        visitedQuestions.clear()
                        currentQuestionIdx = 0
                        remainingSeconds = 10 * 60
                        isSimulatorActive = true
                    }
                )
            }

            // Test History List
            if (testAttempts.isNotEmpty()) {
                item {
                    Text(
                        text = "📜 विगत हल किए गए मॉक टेस्ट का इतिहास",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                items(testAttempts, key = { it.id }) { attempt ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(attempt.testTitle, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                                Text(
                                    text = "सही: ${attempt.correctCount} | गलत: ${attempt.incorrectCount} | Accuracy: ${attempt.accuracyPercent}%",
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "${attempt.netScore} अंक",
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldGreen,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaletteLegendChip(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 11.sp)
    }
}

@Composable
private fun AnalyticsMetricBox(label: String, value: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = value, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = color)
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun MockTestLauncherCard(
    title: String,
    subtitle: String,
    badge: String,
    accentColor: Color,
    onStart: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(
                color = accentColor.copy(alpha = 0.14f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = badge,
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("start_mock_test_btn")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("OMR टेस्ट प्रारंभ करें", fontWeight = FontWeight.Bold)
            }
        }
    }
}
