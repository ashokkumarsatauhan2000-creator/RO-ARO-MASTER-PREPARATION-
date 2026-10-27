package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.ui.viewmodel.MainNavTab

@Composable
fun HomeScreen(
    questions: List<QuestionEntity>,
    selectedSubject: String,
    selectedChapter: String,
    onNavigateToTab: (MainNavTab) -> Unit,
    onSelectSubjectAndOpenBank: (String, String) -> Unit
) {
    var miniQuizIndex by remember { mutableIntStateOf(0) }
    var selectedMiniOption by remember { mutableStateOf<String?>(null) }
    var miniQuizScore by remember { mutableIntStateOf(128) } // Base leaderboard points

    val miniChallengeQuestions = remember(questions) {
        questions.take(10)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Banner Carousel Card with Custom Canvas Graphic
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(NavyBlueDark, NavyBlue, IndigoAccent)
                            )
                        )
                ) {
                    // Decorative Canvas Rays & Circles
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawCircle(
                            color = AmberGold.copy(alpha = 0.14f),
                            radius = size.minDimension * 0.55f,
                            center = Offset(size.width * 0.9f, size.height * 0.2f)
                        )
                        drawCircle(
                            color = Color.White.copy(alpha = 0.08f),
                            radius = size.minDimension * 0.35f,
                            center = Offset(size.width * 0.82f, size.height * 0.75f)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = AmberGold,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "🎯 UPPSC RO/ARO & PCS 2026",
                                    color = NavyBlueDark,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Surface(
                                color = RoseRed,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "शून्य से शिखर बैच",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "घटनाचक्र पूर्वावलोकन एवं परीक्षा वाणी आधारित 2-कॉलम मास्टर प्रश्न-बैंक",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "\"सफलता का कोई शॉर्टकट नहीं होता—प्रामाणिक स्रोत, नियमित 25-25 प्रश्नों का अभ्यास और शून्य त्रुटि ही सचिवालय की कुंजी है।\"",
                            color = Color.White.copy(alpha = 0.88f),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { onNavigateToTab(MainNavTab.QUESTION_BANK) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AmberGold,
                                    contentColor = NavyBlueDark
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("hero_open_question_bank_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Print,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "2-कॉलम प्रश्न-बैंक खोलें",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            OutlinedButton(
                                onClick = { onNavigateToTab(MainNavTab.TEST_SERIES) },
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                            ) {
                                Text(
                                    text = "OMR मॉक टेस्ट",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Quick Action Grid (5 Core Features)
        item {
            Text(
                text = "⚡ त्वरित अध्ययन केंद्र (Quick Actions)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        emoji = "📖",
                        title = "पूर्वावलोकन प्रश्न-बैंक",
                        subtitle = "2-कॉलम प्रिंट व क्विज़ मोड",
                        accentColor = NavyBlue,
                        tag = "quick_action_qbank",
                        onClick = { onNavigateToTab(MainNavTab.QUESTION_BANK) }
                    )
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🔴",
                        title = "लाइव क्लास व डाउट",
                        subtitle = "मैराथन व स्पीड कंट्रोल",
                        accentColor = RoseRed,
                        tag = "quick_action_live",
                        onClick = { onNavigateToTab(MainNavTab.LIVE_CLASSES) }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        emoji = "📝",
                        title = "टेस्ट सीरीज़ व OMR",
                        subtitle = "-0.33 नेगेटिव मार्किंग",
                        accentColor = EmeraldGreen,
                        tag = "quick_action_tests",
                        onClick = { onNavigateToTab(MainNavTab.TEST_SERIES) }
                    )
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        emoji = "📚",
                        title = "ई-बुक्स व क्लास नोट्स",
                        subtitle = "टॉपर हस्तलिखित PDF",
                        accentColor = IndigoAccent,
                        tag = "quick_action_pdf",
                        onClick = { onNavigateToTab(MainNavTab.E_LIBRARY) }
                    )
                }
                QuickActionCard(
                    modifier = Modifier.fillMaxWidth(),
                    emoji = "💬",
                    title = "24x7 डाउट समाधान एवं मेंटरशिप फ़ोरम (✔️ शिक्षक सत्यापित)",
                    subtitle = "चयनित समीक्षा अधिकारियों व विषय विशेषज्ञों से सीधे संवाद",
                    accentColor = AmberGold,
                    tag = "quick_action_doubt",
                    onClick = { onNavigateToTab(MainNavTab.DOUBT_FORUM) }
                )
            }
        }

        // 3. Continue Learning Progress Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NavyBlue.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🏛️", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "अध्ययन जारी रखें (Continue Learning)",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$selectedChapter • बैच 03 (प्रश्न 51-75)",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        IconButtonOnContinue(onClick = { onNavigateToTab(MainNavTab.QUESTION_BANK) })
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val chapterCount = questions.count { it.chapter == selectedChapter }
                    val progress = (chapterCount / 125f).coerceIn(0.15f, 1f)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "अध्याय प्रगति: प्रश्न 01 से 77 / संभावित लक्ष्य ~125",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50)),
                        color = EmeraldGreen,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }

        // 4. Master Syllabus Subject Carousel (8 GS + 1 General Hindi)
        item {
            Text(
                text = "📚 मास्टर सिलेबस (8 GS खंड + सामान्य हिंदी 60 अंक)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(MasterSyllabusSeedData.masterSyllabus) { subj ->
                    Card(
                        onClick = {
                            onSelectSubjectAndOpenBank(subj.title, subj.chapters.first())
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .width(200.dp)
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(subj.emoji, fontSize = 22.sp)
                                Surface(
                                    color = IndigoAccent.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = "${subj.chapters.size} अध्याय",
                                        fontSize = 10.sp,
                                        color = IndigoAccent,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = subj.shortTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = subj.chapters.first(),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // 5. Daily 10-Question Mini Challenge + Instant Leaderboard
        item {
            if (miniChallengeQuestions.isNotEmpty()) {
                val currentQ = miniChallengeQuestions[miniQuizIndex % miniChallengeQuestions.size]
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, IndigoAccent.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = AmberGold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "दैनिक 10-प्रश्न मिनी चैलेंज",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                            Surface(
                                color = EmeraldGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "प्रश्न ${(miniQuizIndex % miniChallengeQuestions.size) + 1} / ${miniChallengeQuestions.size}",
                                    color = EmeraldGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "${currentQ.questionText} ${currentQ.examTag}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = RoseRed
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val options = listOf(
                            "a" to currentQ.optionA,
                            "b" to currentQ.optionB,
                            "c" to currentQ.optionC,
                            "d" to currentQ.optionD
                        )

                        options.forEach { (key, label) ->
                            val isSelected = selectedMiniOption == key
                            val isCorrect = currentQ.correctOption.equals(key, ignoreCase = true)
                            val bgColor = when {
                                selectedMiniOption == null -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                isCorrect -> EmeraldGreen.copy(alpha = 0.18f)
                                isSelected -> RoseRed.copy(alpha = 0.18f)
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            }
                            val borderColor = when {
                                selectedMiniOption == null -> MaterialTheme.colorScheme.outline
                                isCorrect -> EmeraldGreen
                                isSelected -> RoseRed
                                else -> MaterialTheme.colorScheme.outline
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(bgColor)
                                    .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                                    .clickable {
                                        if (selectedMiniOption == null) {
                                            selectedMiniOption = key
                                            if (isCorrect) miniQuizScore += 4
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "($key) $label",
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected || (selectedMiniOption != null && isCorrect)) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )
                                if (selectedMiniOption != null && isCorrect) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = EmeraldGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        if (selectedMiniOption != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "सही उत्तर: (${currentQ.correctOption}) • ${currentQ.detailedExplanation.replace("**", "").take(140)}...",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = AmberGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "आपका लीडरबोर्ड स्कोर: $miniQuizScore अंक (रैंक #04)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = {
                                    selectedMiniOption = null
                                    miniQuizIndex++
                                },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("अगला प्रश्न", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    subtitle: String,
    accentColor: Color,
    tag: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .testTag(tag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun IconButtonOnContinue(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = NavyBlue
    ) {
        Box(
            modifier = Modifier.size(38.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "जारी रखें",
                tint = Color.White
            )
        }
    }
}
