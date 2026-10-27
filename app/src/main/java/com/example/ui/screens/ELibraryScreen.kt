package com.example.ui.screens

import androidx.compose.runtime.Composable
import com.example.data.local.PdfNoteEntity
import com.example.data.seed.MasterSyllabusSeedData

@Composable
fun ELibraryScreen(
    pdfNotes: List<PdfNoteEntity> = MasterSyllabusSeedData.getSeedPdfNotes(),
    onToggleOffline: (PdfNoteEntity) -> Unit = {}
) {
    StudyMaterialsScreen(
        pdfNotes = pdfNotes,
        onToggleOffline = onToggleOffline
    )
}
