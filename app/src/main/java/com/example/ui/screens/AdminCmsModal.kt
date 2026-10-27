package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.seed.MasterSyllabusSeedData
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NavyBlue
import com.example.ui.theme.NavyBlueDark

@Composable
fun AdminCmsModal(
    onDismiss: () -> Unit,
    onAddQuestion: (
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
    ) -> Unit,
    onScheduleLiveClass: (title: String, subject: String, educator: String, link: String, startTime: String) -> Unit,
    onUploadPdf: (title: String, category: String, subject: String, contentMarkdown: String) -> Unit,
    onSendPushAlert: (title: String, message: String) -> Unit
) {
    val context = LocalContext.current
    var activeCmsTab by remember { mutableIntStateOf(0) }
    val cmsTabs = listOf("➕ नया प्रश्न", "🔴 लाइव शेड्यूलर", "📄 PDF अपलोड", "🔔 पुश अलर्ट")

    // Question Form State
    var qSubject by remember { mutableStateOf(MasterSyllabusSeedData.masterSyllabus.first().title) }
    var qChapter by remember { mutableStateOf(MasterSyllabusSeedData.masterSyllabus.first().chapters.first()) }
    var qText by remember { mutableStateOf("") }
    var qExamTag by remember { mutableStateOf("[UPPSC RO/ARO Pre 2021]") }
    var qOptA by remember { mutableStateOf("") }
    var qOptB by remember { mutableStateOf("") }
    var qOptC by remember { mutableStateOf("") }
    var qOptD by remember { mutableStateOf("") }
    var qCorrect by remember { mutableStateOf("a") }
    var qExplanation by remember { mutableStateOf("") }
    var qHasTable by remember { mutableStateOf(false) }
    var qTableRows by remember { mutableStateOf("A. लोथल|1. गोदीबाड़ा;;B. कालीबंगा|2. जुते हुए खेत") }

    // Live Class Form State
    var lcTitle by remember { mutableStateOf("") }
    var lcSubject by remember { mutableStateOf("भारतीय इतिहास") }
    var lcEducator by remember { mutableStateOf("डॉ. आलोक पाण्डेय") }
    var lcLink by remember { mutableStateOf("https://youtube.com/live/ro_aro_2026") }
    var lcTime by remember { mutableStateOf("आज रात 09:00 बजे") }

    // PDF Upload Form State
    var pdfTitle by remember { mutableStateOf("") }
    var pdfCategory by remember { mutableStateOf("Handwritten Topper Notes") }
    var pdfSubject by remember { mutableStateOf("सामान्य हिंदी") }
    var pdfContent by remember { mutableStateOf("# 2-कॉलम रिवीजन नोट्स\n- महत्वपूर्ण तथ्य एवं सारणी") }

    // Push Alert Form State
    var alertTitle by remember { mutableStateOf("") }
    var alertMessage by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NavyBlueDark)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "⚙️ APEX CMS एडमिन कंट्रोल पैनल",
                            color = AmberGold,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "प्रश्न-बैंक, लाइव क्लास, पीडीएफ एवं छात्र अलर्ट प्रबंधन",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "बंद करें", tint = Color.White)
                    }
                }

                // CMS Tab Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    cmsTabs.forEachIndexed { idx, label ->
                        FilterChip(
                            selected = activeCmsTab == idx,
                            onClick = { activeCmsTab = idx },
                            label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (activeCmsTab) {
                        0 -> {
                            Text(
                                text = "नया 2-कॉलम पूर्वावलोकन प्रश्न जोड़ें",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            OutlinedTextField(
                                value = qSubject,
                                onValueChange = { qSubject = it },
                                label = { Text("विषय (Subject)", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = qChapter,
                                onValueChange = { qChapter = it },
                                label = { Text("अध्याय (Chapter)", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = qText,
                                onValueChange = { qText = it },
                                label = { Text("प्रश्न टेक्स्ट (Question Text)", fontSize = 12.sp) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("cms_question_text_input")
                            )
                            OutlinedTextField(
                                value = qExamTag,
                                onValueChange = { qExamTag = it },
                                label = { Text("PYQ परीक्षा वर्ष टैग (उदा. [UPPSC RO/ARO Pre 2021])", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = qHasTable, onCheckedChange = { qHasTable = it })
                                Text("2-कॉलम सूची मिलान टेबल जोड़ें", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            if (qHasTable) {
                                OutlinedTextField(
                                    value = qTableRows,
                                    onValueChange = { qTableRows = it },
                                    label = { Text("टेबल पंक्तियां (A. मद|1. तथ्य;;B. मद|2. तथ्य)", fontSize = 11.sp) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = qOptA,
                                    onValueChange = { qOptA = it },
                                    label = { Text("विकल्प (a)", fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = qOptB,
                                    onValueChange = { qOptB = it },
                                    label = { Text("विकल्प (b)", fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = qOptC,
                                    onValueChange = { qOptC = it },
                                    label = { Text("विकल्प (c)", fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = qOptD,
                                    onValueChange = { qOptD = it },
                                    label = { Text("विकल्प (d)", fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            OutlinedTextField(
                                value = qCorrect,
                                onValueChange = { qCorrect = it },
                                label = { Text("सही उत्तर कुंजी (a / b / c / d)", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = qExplanation,
                                onValueChange = { qExplanation = it },
                                label = { Text("विस्तृत व्याख्या (**बोल्ड** समर्थन के साथ)", fontSize = 12.sp) },
                                minLines = 3,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    if (qText.isNotBlank() && qOptA.isNotBlank()) {
                                        onAddQuestion(
                                            qSubject,
                                            qChapter,
                                            qText,
                                            qExamTag,
                                            qOptA,
                                            qOptB,
                                            qOptC,
                                            qOptD,
                                            qCorrect,
                                            qExplanation,
                                            qHasTable,
                                            qTableRows
                                        )
                                        Toast.makeText(context, "नया प्रश्न 2-कॉलम बैंक में जुड़ गया!", Toast.LENGTH_SHORT).show()
                                        onDismiss()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("cms_save_question_btn")
                            ) {
                                Text("प्रश्न-बैंक में प्रकाशित करें", fontWeight = FontWeight.Bold)
                            }
                        }

                        1 -> {
                            Text("नया लाइव क्लास सत्र शेड्यूल करें", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            OutlinedTextField(
                                value = lcTitle,
                                onValueChange = { lcTitle = it },
                                label = { Text("लेक्चर का शीर्षक", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = lcSubject,
                                onValueChange = { lcSubject = it },
                                label = { Text("विषय", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = lcEducator,
                                onValueChange = { lcEducator = it },
                                label = { Text("शिक्षक / मेंटर का नाम", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = lcLink,
                                onValueChange = { lcLink = it },
                                label = { Text("YouTube Video ID या Zoom Link", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = lcTime,
                                onValueChange = { lcTime = it },
                                label = { Text("प्रारंभ समय", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    if (lcTitle.isNotBlank()) {
                                        onScheduleLiveClass(lcTitle, lcSubject, lcEducator, lcLink, lcTime)
                                        Toast.makeText(context, "लाइव क्लास शेड्यूल हो गई!", Toast.LENGTH_SHORT).show()
                                        onDismiss()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NavyBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("लाइव क्लास पब्लिश करें", fontWeight = FontWeight.Bold)
                            }
                        }

                        2 -> {
                            Text("ई-लाइब्रेरी में नई PDF / स्टडी नोट्स अपलोड करें", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            OutlinedTextField(
                                value = pdfTitle,
                                onValueChange = { pdfTitle = it },
                                label = { Text("पीडीएफ शीर्षक", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = pdfCategory,
                                onValueChange = { pdfCategory = it },
                                label = { Text("श्रेणी (Handwritten Topper Notes / Official NCERT Gist)", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = pdfSubject,
                                onValueChange = { pdfSubject = it },
                                label = { Text("विषय", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = pdfContent,
                                onValueChange = { pdfContent = it },
                                label = { Text("नोट्स की मुख्य सामग्री (Markdown)", fontSize = 12.sp) },
                                minLines = 3,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    if (pdfTitle.isNotBlank()) {
                                        onUploadPdf(pdfTitle, pdfCategory, pdfSubject, pdfContent)
                                        Toast.makeText(context, "पीडीएफ ई-लाइब्रेरी में अपलोड हो गई!", Toast.LENGTH_SHORT).show()
                                        onDismiss()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("ई-लाइब्रेरी में जोड़ें", fontWeight = FontWeight.Bold)
                            }
                        }

                        3 -> {
                            Text("छात्रों को तुरंत पुश नोटिफिकेशन अलर्ट भेजें", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            OutlinedTextField(
                                value = alertTitle,
                                onValueChange = { alertTitle = it },
                                label = { Text("अलर्ट शीर्षक (उदा. 🔴 लाइव टेस्ट शुरू!)", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = alertMessage,
                                onValueChange = { alertMessage = it },
                                label = { Text("संदेश विवरण", fontSize = 12.sp) },
                                minLines = 2,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    if (alertTitle.isNotBlank()) {
                                        onSendPushAlert(alertTitle, alertMessage)
                                        Toast.makeText(context, "पुश अलर्ट सभी छात्रों को भेज दिया गया!", Toast.LENGTH_SHORT).show()
                                        onDismiss()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NavyBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("पुश नोटिफिकेशन भेजें", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
