package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LiveClassEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.NavyBlue
import com.example.ui.theme.NavyBlueDark
import com.example.ui.theme.RoseRed

data class LiveChatMessage(
    val sender: String,
    val message: String,
    val isEducator: Boolean = false
)

@Composable
fun LiveClassScreen(liveClasses: List<LiveClassEntity>) {
    val context = LocalContext.current
    var selectedClass by remember(liveClasses) {
        mutableStateOf(liveClasses.firstOrNull())
    }
    var isPlaying by remember { mutableStateOf(true) }
    var playbackSpeed by remember { mutableStateOf("1x") }
    var activeTimestamp by remember { mutableStateOf("14:20") }
    var videoProgress by remember { mutableFloatStateOf(0.36f) }
    var chatInput by remember { mutableStateOf("") }

    val liveChatMessages = remember {
        mutableStateListOf(
            LiveChatMessage("डॉ. आलोक पाण्डेय (Educator)", "सभी समीक्षा अधिकारी अभ्यर्थियों का स्वागत है! आज हम पाषाण काल के महदहा, दमदमा व लहुरादेवा के PYQs का 2-कॉलम विश्लेषण कर रहे हैं।", isEducator = true),
            LiveChatMessage("रोहित कुमार (प्रयागराज)", "सर, दमदमा से कुल 41 शवाधान मिले हैं जिनमें 1 त्रि-मानव शवाधान है, क्या यह 2016 में पूछा गया था?"),
            LiveChatMessage("डॉ. आलोक पाण्डेय (Educator)", "बिल्कुल सही रोहित! UPPCS Pre 2016 और RO/ARO दोनों में यह तथ्य पूछा जा चुका है।", isEducator = true),
            LiveChatMessage("नेहा वर्मा (लखनऊ)", "सर, आपके 2-कॉलम प्रिंट नोट्स बहुत स्पष्ट हैं, सीधे रिवीजन हो रहा है।")
        )
    }

    val speeds = listOf("1x", "1.25x", "1.5x", "2x")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Active Video / Live Stream Player Card
        item {
            val active = selectedClass ?: liveClasses.firstOrNull()
            if (active != null) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        // Simulated Embedded YouTube Live / Classroom Blackboard Player
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(215.dp)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color(0xFF090D16), NavyBlueDark, Color(0xFF0F172A))
                                    )
                                )
                        ) {
                            Canvas(modifier = Modifier.matchParentSize()) {
                                drawCircle(
                                    color = IndigoAccent.copy(alpha = 0.18f),
                                    radius = size.minDimension * 0.45f,
                                    center = Offset(size.width * 0.85f, size.height * 0.25f)
                                )
                            }

                            // Top Bar inside Video Player
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = if (active.isLiveNow) RoseRed else IndigoAccent,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (active.isLiveNow) "🔴 LIVE STREAM • 1,428 वाचिंग" else "🎬 RECORDED HD • $playbackSpeed",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Surface(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "स्पीड: $playbackSpeed | $activeTimestamp / ${active.durationText}",
                                        color = AmberGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            // Center Digital Board Slide Preview
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(horizontal = 20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = active.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    lineHeight = 21.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "प्रवक्ता: ${active.educator}",
                                    color = AmberGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    onClick = { isPlaying = !isPlaying },
                                    shape = CircleShape,
                                    color = AmberGold
                                ) {
                                    Box(
                                        modifier = Modifier.size(46.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = "Play/Pause",
                                            tint = NavyBlueDark,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }
                            }

                            // Bottom Progress Bar
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {
                                LinearProgressIndicator(
                                    progress = { videoProgress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(50)),
                                    color = RoseRed,
                                    trackColor = Color.White.copy(alpha = 0.25f)
                                )
                            }
                        }

                        // Speed Control + Timestamp Markers + Lecture Notes Download
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Speed,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    speeds.forEach { spd ->
                                        FilterChip(
                                            selected = playbackSpeed == spd,
                                            onClick = { playbackSpeed = spd },
                                            label = { Text(spd, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        Toast.makeText(
                                            context,
                                            "लेक्चर नोट्स '${active.notesAttachmentTitle}' डाउनलोड हो गया!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("क्लास PDF", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "⏱️ अध्याय टाइमस्टैम्प (सीधे टॉपिक पर जाएं):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            val timestamps = active.timestampsText.split(";;").filter { it.isNotBlank() }
                            timestamps.forEachIndexed { idx, ts ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                        .clickable {
                                            activeTimestamp = ts.substringBefore(" ")
                                            videoProgress = ((idx + 1).toFloat() / timestamps.size.toFloat()).coerceIn(0.15f, 0.95f)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = IndigoAccent,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = ts,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Real-time Interactive Live Chat Box
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "💬 लाइव क्लास चैट व तत्काल डाउट बॉक्स",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    liveChatMessages.forEach { msg ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (msg.isEducator) AmberGold.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = if (msg.isEducator) "✔️ ${msg.sender}" else msg.sender,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    color = if (msg.isEducator) NavyBlue else IndigoAccent
                                )
                                Text(
                                    text = msg.message,
                                    fontSize = 12.5.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = chatInput,
                            onValueChange = { chatInput = it },
                            placeholder = { Text("लाइव क्लास में अपना प्रश्न पूछें...", fontSize = 12.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("live_chat_input")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (chatInput.isNotBlank()) {
                                    val userQ = chatInput
                                    liveChatMessages.add(LiveChatMessage("आप (RO/ARO अभ्यर्थी)", userQ))
                                    liveChatMessages.add(
                                        LiveChatMessage(
                                            "डॉ. आलोक पाण्डेय (Educator)",
                                            "बहुत बढ़िया प्रश्न! आयोग के मानक स्रोतों में इस तथ्य को 2-कॉलम शीट में बोल्ड किया गया है।",
                                            isEducator = true
                                        )
                                    )
                                    chatInput = ""
                                }
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(NavyBlue)
                                .testTag("live_chat_send_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "भेजें",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 3. Recorded & Upcoming Lectures List
        item {
            Text(
                text = "📼 सभी लाइव एवं रिकॉर्डेड विषयवार लेक्चर",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(liveClasses, key = { it.id }) { lecture ->
            val isSelected = selectedClass?.id == lecture.id
            Card(
                onClick = { selectedClass = lecture },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) NavyBlue.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) NavyBlue else MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(14.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (lecture.isLiveNow) RoseRed else NavyBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = lecture.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${lecture.educator} • ${lecture.startTime}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
