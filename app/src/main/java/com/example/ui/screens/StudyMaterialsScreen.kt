package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PdfNoteEntity
import com.example.data.seed.MasterSyllabusSeedData
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.NavyBlue
import com.example.ui.theme.NavyBlueDark
import com.example.ui.theme.ReviewPurple
import com.example.ui.theme.RoseRed

data class StudyMaterialCategoryInfo(
    val id: String,
    val titleHi: String,
    val subtitle: String,
    val emoji: String,
    val accentColor: Color
)

private val studyMaterialCategories = listOf(
    StudyMaterialCategoryInfo(
        id = "All",
        titleHi = "सभी अध्ययन सामग्री",
        subtitle = "संपूर्ण पीडीएफ व नोट्स",
        emoji = "📚",
        accentColor = NavyBlue
    ),
    StudyMaterialCategoryInfo(
        id = "Handwritten Topper Notes",
        titleHi = "टॉपर हस्तलिखित नोट्स",
        subtitle = "चयनित RO/ARO शॉर्ट नोट्स",
        emoji = "✍️",
        accentColor = IndigoAccent
    ),
    StudyMaterialCategoryInfo(
        id = "Official NCERT Gist",
        titleHi = "NCERT व मानक सार",
        subtitle = "कक्षा 6-12 एवं हरदेव बाहरी",
        emoji = "🏛️",
        accentColor = EmeraldGreen
    ),
    StudyMaterialCategoryInfo(
        id = "Purvavlokan 2-Column Notes",
        titleHi = "पूर्वावलोकन 2-कॉलम नोट्स",
        subtitle = "घटनाचक्र व परीक्षा वाणी सार",
        emoji = "📖",
        accentColor = AmberGold
    ),
    StudyMaterialCategoryInfo(
        id = "Model Question Papers",
        titleHi = "मॉडल प्रश्न-पत्र व PYQ",
        subtitle = "140 GS + 60 हिंदी पेपर्स",
        emoji = "📝",
        accentColor = RoseRed
    )
)

@Composable
fun StudyMaterialsScreen(
    pdfNotes: List<PdfNoteEntity> = MasterSyllabusSeedData.getSeedPdfNotes(),
    onToggleOffline: (PdfNoteEntity) -> Unit = {}
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedSubjectFilter by remember { mutableStateOf("सभी विषय") }
    var localSearchQuery by remember { mutableStateOf("") }
    var showOfflineOnly by remember { mutableStateOf(false) }

    var activeReadingPdf by remember(pdfNotes) { mutableStateOf(pdfNotes.firstOrNull()) }
    var isReaderExpanded by remember { mutableStateOf(true) }
    var readerZoomScale by remember { mutableFloatStateOf(1.0f) }
    var readerNightMode by remember { mutableStateOf(false) }

    val availableSubjects = remember(pdfNotes) {
        listOf("सभी विषय") + pdfNotes.map { it.subject }.distinct()
    }

    val filteredNotes = remember(
        pdfNotes,
        selectedCategory,
        selectedSubjectFilter,
        localSearchQuery,
        showOfflineOnly
    ) {
        pdfNotes.filter { note ->
            val matchesCategory = selectedCategory == "All" || note.category == selectedCategory
            val matchesSubject = selectedSubjectFilter == "सभी विषय" || note.subject == selectedSubjectFilter
            val matchesOffline = !showOfflineOnly || note.isDownloadedOffline
            val matchesSearch = localSearchQuery.isBlank() ||
                note.title.contains(localSearchQuery, ignoreCase = true) ||
                note.subject.contains(localSearchQuery, ignoreCase = true) ||
                note.category.contains(localSearchQuery, ignoreCase = true) ||
                note.contentMarkdown.contains(localSearchQuery, ignoreCase = true)
            matchesCategory && matchesSubject && matchesOffline && matchesSearch
        }
    }

    val totalPages = remember(pdfNotes) { pdfNotes.sumOf { it.pageCount } }
    val offlineSavedCount = remember(pdfNotes) { pdfNotes.count { it.isDownloadedOffline } }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("study_materials_lazy_vertical_grid"),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Hero Banner & Study Materials Statistics
        item(span = { GridItemSpan(maxLineSpan) }) {
            Card(
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(NavyBlueDark, NavyBlue, IndigoAccent)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = AmberGold,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "📚 UPPSC ई-लाइब्रेरी",
                                        color = NavyBlueDark,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = Color.White.copy(alpha = 0.16f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "2-Column Print Ready",
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Surface(
                                color = EmeraldGreen.copy(alpha = 0.22f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "✓ $offlineSavedCount ऑफ़लाइन सेव",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "UPPSC RO/ARO एवं PCS अध्ययन सामग्री व पीडीएफ नोट्स केंद्र",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "कुल ${pdfNotes.size} प्रामाणिक ई-बुक्स • $totalPages+ पृष्ठ • घटनाचक्र पूर्वावलोकन, परीक्षा वाणी एवं NCERT सार",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // 2. Search & Offline Filter Controls
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = localSearchQuery,
                    onValueChange = { localSearchQuery = it },
                    placeholder = {
                        Text(
                            text = "पीडीएफ, विषय या अध्याय खोजें (उदा: पाषाण काल, हिंदी, राजव्यवस्था)...",
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "नोट्स खोजें",
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (localSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { localSearchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "खोज साफ़ करें",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("study_materials_search_input")
                )

                FilterChip(
                    selected = showOfflineOnly,
                    onClick = { showOfflineOnly = !showOfflineOnly },
                    label = {
                        Text(
                            text = "ऑफ़लाइन ($offlineSavedCount)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = if (showOfflineOnly) Icons.Default.CloudDone else Icons.Default.FilterList,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldGreen.copy(alpha = 0.18f),
                        selectedLabelColor = EmeraldGreen,
                        selectedLeadingIconColor = EmeraldGreen
                    ),
                    modifier = Modifier.testTag("offline_only_filter_chip")
                )
            }
        }

        // 3. Section Header: Categorization Grid
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🗂️ श्रेणीवार अध्ययन सामग्री (Categories)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (selectedCategory != "All" || selectedSubjectFilter != "सभी विषय" || showOfflineOnly) {
                    Text(
                        text = "फ़िल्टर रीसेट करें",
                        color = IndigoAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                selectedCategory = "All"
                                selectedSubjectFilter = "सभी विषय"
                                showOfflineOnly = false
                                localSearchQuery = ""
                            }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                            .testTag("reset_study_filters_button")
                    )
                }
            }
        }

        // 4. Category Grid Items inside LazyVerticalGrid
        items(
            items = studyMaterialCategories,
            key = { "cat_${it.id}" }
        ) { categoryInfo ->
            val countForCategory = remember(pdfNotes, categoryInfo.id) {
                if (categoryInfo.id == "All") pdfNotes.size
                else pdfNotes.count { it.category == categoryInfo.id }
            }
            val isSelected = selectedCategory == categoryInfo.id

            StudyMaterialCategoryGridCard(
                categoryInfo = categoryInfo,
                noteCount = countForCategory,
                isSelected = isSelected,
                onClick = { selectedCategory = categoryInfo.id }
            )
        }

        // 5. Subject Filter Bar
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                availableSubjects.forEach { subject ->
                    val isSubjSelected = selectedSubjectFilter == subject
                    FilterChip(
                        selected = isSubjSelected,
                        onClick = { selectedSubjectFilter = subject },
                        label = {
                            Text(
                                text = subject,
                                fontSize = 12.sp,
                                fontWeight = if (isSubjSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        modifier = Modifier.testTag("subject_chip_$subject")
                    )
                }
            }
        }

        // 6. High-Speed In-App PDF / Note Reader Panel
        item(span = { GridItemSpan(maxLineSpan) }) {
            val activePdf = activeReadingPdf ?: filteredNotes.firstOrNull()
            AnimatedVisibility(visible = activePdf != null && isReaderExpanded) {
                if (activePdf != null) {
                    InAppPdfReaderCard(
                        activePdf = activePdf,
                        readerZoomScale = readerZoomScale,
                        readerNightMode = readerNightMode,
                        onZoomOut = { readerZoomScale = (readerZoomScale - 0.15f).coerceAtLeast(0.85f) },
                        onZoomIn = { readerZoomScale = (readerZoomScale + 0.15f).coerceAtMost(1.6f) },
                        onToggleNightMode = { readerNightMode = !readerNightMode },
                        onCopyNotes = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(
                                ClipData.newPlainText(activePdf.title, activePdf.contentMarkdown)
                            )
                            Toast.makeText(
                                context,
                                "नोट्स क्लिपबोर्ड पर कॉपी हो गए!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onCloseReader = { isReaderExpanded = false }
                    )
                }
            }
        }

        // 7. Organized PDFs & Notes Grid Section Header
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📂 ई-लाइब्रेरी संग्रह (${filteredNotes.size} पीडीएफ उपलब्ध)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (!isReaderExpanded && activeReadingPdf != null) {
                    Surface(
                        onClick = { isReaderExpanded = true },
                        shape = RoundedCornerShape(8.dp),
                        color = NavyBlue.copy(alpha = 0.12f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = NavyBlue,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "रीडर खोलें",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyBlue
                            )
                        }
                    }
                }
            }
        }

        // 8. Empty State or Categorized PDF & Study Notes Cards in LazyVerticalGrid
        if (filteredNotes.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = IndigoAccent,
                            modifier = Modifier.size(42.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "इस श्रेणी या खोज में कोई पीडीएफ नोट्स नहीं मिले",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "सभी विषयों के नोट्स देखने के लिए फ़िल्टर रीसेट करें।",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(
                items = filteredNotes,
                key = { note -> "pdf_note_${note.id}" }
            ) { note ->
                val isCurrentlyReading = activeReadingPdf?.id == note.id && isReaderExpanded
                StudyMaterialPdfGridCard(
                    note = note,
                    isCurrentlyReading = isCurrentlyReading,
                    onReadClick = {
                        activeReadingPdf = note
                        isReaderExpanded = true
                    },
                    onToggleOffline = { onToggleOffline(note) }
                )
            }
        }
    }
}

@Composable
private fun StudyMaterialCategoryGridCard(
    categoryInfo: StudyMaterialCategoryInfo,
    noteCount: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) {
        categoryInfo.accentColor.copy(alpha = 0.14f)
    } else {
        MaterialTheme.colorScheme.surface
    }
    val borderColor = if (isSelected) {
        categoryInfo.accentColor
    } else {
        MaterialTheme.colorScheme.outline
    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 1.8.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(14.dp)
            )
            .testTag("category_grid_card_${categoryInfo.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(categoryInfo.accentColor.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = categoryInfo.emoji, fontSize = 18.sp)
                }

                Surface(
                    color = if (isSelected) categoryInfo.accentColor else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "$noteCount PDF",
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = categoryInfo.titleHi,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = categoryInfo.subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StudyMaterialPdfGridCard(
    note: PdfNoteEntity,
    isCurrentlyReading: Boolean,
    onReadClick: () -> Unit,
    onToggleOffline: () -> Unit
) {
    val categoryColor = when (note.category) {
        "Handwritten Topper Notes" -> IndigoAccent
        "Official NCERT Gist" -> EmeraldGreen
        "Purvavlokan 2-Column Notes" -> AmberGold
        "Model Question Papers" -> RoseRed
        else -> ReviewPurple
    }

    val categoryLabelHi = when (note.category) {
        "Handwritten Topper Notes" -> "✍️ टॉपर नोट्स"
        "Official NCERT Gist" -> "🏛️ NCERT सार"
        "Purvavlokan 2-Column Notes" -> "📖 2-कॉलम नोट्स"
        "Model Question Papers" -> "📝 मॉडल पेपर"
        else -> note.category
    }

    Card(
        onClick = onReadClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrentlyReading) 4.dp else 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isCurrentlyReading) 1.8.dp else 1.dp,
                color = if (isCurrentlyReading) NavyBlue else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("study_material_card_${note.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Top Row: Category Badge & PDF Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = categoryColor.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = categoryLabelHi,
                            color = categoryColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(NavyBlue.copy(alpha = 0.10f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            tint = NavyBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Subject Label
                Text(
                    text = note.subject,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IndigoAccent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Note Title
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Metadata Row: Pages & Size
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${note.pageCount} पृष्ठ • ${note.fileSizeText}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Actions: Read & Offline Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onReadClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCurrentlyReading) EmeraldGreen else NavyBlue,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .testTag("study_material_read_${note.id}")
                ) {
                    Text(
                        text = if (isCurrentlyReading) "पढ़ रहे हैं" else "पढ़ें",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                Surface(
                    onClick = onToggleOffline,
                    shape = RoundedCornerShape(8.dp),
                    color = if (note.isDownloadedOffline) {
                        EmeraldGreen.copy(alpha = 0.15f)
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("pdf_offline_toggle_${note.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (note.isDownloadedOffline) {
                                Icons.Default.CloudDone
                            } else {
                                Icons.Default.CloudDownload
                            },
                            contentDescription = "ऑफ़लाइन डाउनलोड",
                            tint = if (note.isDownloadedOffline) {
                                EmeraldGreen
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (note.isDownloadedOffline) "ऑफ़लाइन सेव" else "डाउनलोड",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (note.isDownloadedOffline) {
                                EmeraldGreen
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InAppPdfReaderCard(
    activePdf: PdfNoteEntity,
    readerZoomScale: Float,
    readerNightMode: Boolean,
    onZoomOut: () -> Unit,
    onZoomIn: () -> Unit,
    onToggleNightMode: () -> Unit,
    onCopyNotes: () -> Unit,
    onCloseReader: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (readerNightMode) Color(0xFF0F172A) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, NavyBlue, RoundedCornerShape(18.dp))
            .testTag("in_app_pdf_reader_card")
    ) {
        Column {
            // Reader Toolbar (Zoom, Night Mode, Copy, Close)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyBlueDark)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "📖 इन-ऐप हाई-स्पीड PDF रीडर • ${activePdf.subject}",
                        color = AmberGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = activePdf.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onZoomOut,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ZoomOut,
                            contentDescription = "Zoom Out",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "${(readerZoomScale * 100).toInt()}%",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onZoomIn,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ZoomIn,
                            contentDescription = "Zoom In",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onToggleNightMode,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = "Night Mode",
                            tint = if (readerNightMode) AmberGold else Color.White
                        )
                    }
                    IconButton(
                        onClick = onCopyNotes,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Notes",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = onCloseReader,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Reader",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Reader Content Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = IndigoAccent.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${activePdf.category} • ${activePdf.pageCount} पृष्ठ",
                            color = if (readerNightMode) AmberGold else IndigoAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    if (activePdf.isDownloadedOffline) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.BookmarkAdded,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ऑफ़लाइन उपलब्ध",
                                color = EmeraldGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = activePdf.contentMarkdown,
                    fontSize = (13.5f * readerZoomScale).sp,
                    lineHeight = (21f * readerZoomScale).sp,
                    color = if (readerNightMode) Color(0xFFF8FAFC) else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
