package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.data.local.QuestionEntity

object PrintHelper {

    fun formatBoldHtml(text: String): String {
        val regex = Regex("\\*\\*(.*?)\\*\\*")
        return regex.replace(text) { matchResult ->
            "<strong>${matchResult.groupValues[1]}</strong>"
        }
    }

    fun buildMsWordMarkdown(
        subject: String,
        chapter: String,
        questions: List<QuestionEntity>
    ): String {
        val sb = StringBuilder()
        questions.forEach { q ->
            val numStr = q.questionNumber.toString().padStart(2, '0')
            sb.append("**प्रश्न संख्या [$numStr]: ${q.questionText}** ${q.examTag}  \n")
            if (q.hasMatchTable && q.tableRowsJson.isNotBlank()) {
                sb.append("\n| ${q.tableHeaderLeft} | ${q.tableHeaderRight} |\n")
                sb.append("| :--- | :--- |\n")
                q.tableRowsJson.split(";;").forEach { row ->
                    val cols = row.split("|")
                    if (cols.size >= 2) {
                        sb.append("| ${cols[0]} | ${cols[1]} |\n")
                    }
                }
                sb.append("\nकूट:  \n")
            }
            sb.append("(a) ${q.optionA}  \n")
            sb.append("(b) ${q.optionB}  \n")
            sb.append("(c) ${q.optionC}  \n")
            sb.append("(d) ${q.optionD}  \n")
            sb.append("**उत्तर: (${q.correctOption})**  \n")
            sb.append("**विस्तृत व्याख्या:** ${q.detailedExplanation}  \n")
            if (q.summaryTableRowsJson.isNotBlank()) {
                sb.append("\n| ${q.summaryTableHeaderLeft} | ${q.summaryTableHeaderRight} |\n")
                sb.append("| :--- | :--- |\n")
                q.summaryTableRowsJson.split(";;").forEach { row ->
                    val cols = row.split("|")
                    if (cols.size >= 2) {
                        sb.append("| ${cols[0]} | ${cols[1]} |\n")
                    }
                }
            }
            sb.append("---\n")
        }
        val firstNum = questions.firstOrNull()?.questionNumber?.toString()?.padStart(2, '0') ?: "01"
        val lastNum = questions.lastOrNull()?.questionNumber?.toString()?.padStart(2, '0') ?: "25"
        sb.append("\n==================================================\n")
        sb.append("वर्तमान विषय: $subject\n")
        sb.append("वर्तमान अध्याय: $chapter\n")
        sb.append("प्रगति: प्रश्न $firstNum से $lastNum / इस अध्याय के संभावित लक्ष्य: ~300\n")
        sb.append("स्थिति: अध्याय जारी है\n")
        sb.append("==================================================\n")
        return sb.toString()
    }

    fun copyWordMarkdownToClipboard(
        context: Context,
        subject: String,
        chapter: String,
        questions: List<QuestionEntity>
    ) {
        val markdown = buildMsWordMarkdown(subject, chapter, questions)
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("RO_ARO_2_Column_Word_Markdown", markdown)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(
            context,
            "MS Word 2-कॉलम मार्कडाउन क्लिपबोर्ड पर कॉपी हो गया!",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun shareWordMarkdown(
        context: Context,
        subject: String,
        chapter: String,
        questions: List<QuestionEntity>
    ) {
        val markdown = buildMsWordMarkdown(subject, chapter, questions)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "RO/ARO लक्ष्य 2026 - $chapter (2-Column Notes)")
            putExtra(Intent.EXTRA_TEXT, markdown)
        }
        context.startActivity(Intent.createChooser(intent, "प्रिंट-रेडी नोट्स साझा करें"))
    }

    fun printTwoColumnPurvavlokanSheet(
        context: Context,
        subject: String,
        chapter: String,
        questions: List<QuestionEntity>
    ) {
        val webView = WebView(context)
        val htmlContent = buildHtmlForPrint(subject, chapter, questions)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                val jobName = "CW_EXAM_RO_ARO_${chapter.take(15)}"
                val printAdapter = webView.createPrintDocumentAdapter(jobName)
                printManager?.print(
                    jobName,
                    printAdapter,
                    PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .build()
                )
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/HTML", "UTF-8", null)
    }

    private fun buildHtmlForPrint(
        subject: String,
        chapter: String,
        questions: List<QuestionEntity>
    ): String {
        val questionsHtml = StringBuilder()
        questions.forEach { q ->
            val numStr = q.questionNumber.toString().padStart(2, '0')
            questionsHtml.append("<div class='q-block'>")
            questionsHtml.append(
                "<div class='q-title'>प्रश्न संख्या [$numStr]: ${q.questionText} " +
                    "<span class='tag'>${q.examTag}</span></div>"
            )
            if (q.hasMatchTable && q.tableRowsJson.isNotBlank()) {
                questionsHtml.append("<table><thead><tr><th>${q.tableHeaderLeft}</th><th>${q.tableHeaderRight}</th></tr></thead><tbody>")
                q.tableRowsJson.split(";;").forEach { row ->
                    val cols = row.split("|")
                    if (cols.size >= 2) {
                        questionsHtml.append("<tr><td>${cols[0]}</td><td>${cols[1]}</td></tr>")
                    }
                }
                questionsHtml.append("</tbody></table><div class='koot'>कूट:</div>")
            }
            questionsHtml.append("<div class='opt'>(a) ${q.optionA}</div>")
            questionsHtml.append("<div class='opt'>(b) ${q.optionB}</div>")
            questionsHtml.append("<div class='opt'>(c) ${q.optionC}</div>")
            questionsHtml.append("<div class='opt'>(d) ${q.optionD}</div>")
            questionsHtml.append("<div class='ans'>उत्तर: (${q.correctOption})</div>")
            questionsHtml.append(
                "<div class='exp'><strong>विस्तृत व्याख्या:</strong> ${formatBoldHtml(q.detailedExplanation)}</div>"
            )
            if (q.summaryTableRowsJson.isNotBlank()) {
                questionsHtml.append("<table><thead><tr><th>${q.summaryTableHeaderLeft}</th><th>${q.summaryTableHeaderRight}</th></tr></thead><tbody>")
                q.summaryTableRowsJson.split(";;").forEach { row ->
                    val cols = row.split("|")
                    if (cols.size >= 2) {
                        questionsHtml.append("<tr><td>${cols[0]}</td><td>${cols[1]}</td></tr>")
                    }
                }
                questionsHtml.append("</tbody></table>")
            }
            questionsHtml.append("<hr class='divider'/></div>")
        }

        return """
            <!DOCTYPE html>
            <html lang="hi">
            <head>
                <meta charset="UTF-8">
                <style>
                    @page { size: A4; margin: 10mm; }
                    body {
                        font-family: 'Noto Sans Devanagari', 'Kokila', sans-serif;
                        font-size: 10.5pt;
                        line-height: 1.35;
                        color: #0F172A;
                        margin: 0;
                        padding: 0;
                    }
                    .header {
                        text-align: center;
                        border-bottom: 2px solid #1E3A8A;
                        padding-bottom: 6px;
                        margin-bottom: 10px;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 14pt;
                        color: #1E3A8A;
                    }
                    .header p {
                        margin: 2px 0 0 0;
                        font-size: 9.5pt;
                        color: #475569;
                    }
                    .two-col-container {
                        column-count: 2;
                        column-gap: 16px;
                        column-rule: 1px solid #CBD5E1;
                    }
                    .q-block {
                        break-inside: avoid;
                        margin-bottom: 8px;
                    }
                    .q-title {
                        color: #B91C1C;
                        font-weight: bold;
                        margin-bottom: 2px;
                    }
                    .tag {
                        color: #1E3A8A;
                        font-size: 9pt;
                        font-weight: bold;
                    }
                    .opt {
                        margin-left: 4px;
                    }
                    .ans {
                        color: #047857;
                        font-weight: bold;
                        margin-top: 2px;
                    }
                    .exp {
                        background: #F8FAFC;
                        border-left: 2.5px solid #4F46E5;
                        padding: 4px 6px;
                        margin-top: 3px;
                        text-align: justify;
                    }
                    table {
                        width: 100%;
                        border-collapse: collapse;
                        margin: 4px 0;
                        font-size: 9pt;
                    }
                    th, td {
                        border: 1px solid #94A3B8;
                        padding: 2px 4px;
                        text-align: left;
                    }
                    th {
                        background-color: #E2E8F0;
                        font-weight: bold;
                    }
                    .divider {
                        border: 0;
                        border-top: 1px dashed #CBD5E1;
                        margin: 6px 0;
                    }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>RO/ARO लक्ष्य 2026 - सिविल वर्ल्ड (CW EXAM) पूर्वावलोकन प्रश्न-बैंक</h1>
                    <p><strong>विषय:</strong> $subject | <strong>अध्याय:</strong> $chapter</p>
                </div>
                <div class="two-col-container">
                    $questionsHtml
                </div>
            </body>
            </html>
        """.trimIndent()
    }
}
