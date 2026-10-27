package com.example.data.seed

import com.example.data.local.DoubtPostEntity
import com.example.data.local.LiveClassEntity
import com.example.data.local.PdfNoteEntity
import com.example.data.local.QuestionEntity
import com.example.data.local.TestAttemptEntity

data class SubjectSyllabus(
    val id: Int,
    val title: String,
    val shortTitle: String,
    val emoji: String,
    val chapters: List<String>,
    val targetQuestionsPerChapter: Int = 300
)

object MasterSyllabusSeedData {

    val masterSyllabus: List<SubjectSyllabus> = listOf(
        SubjectSyllabus(
            id = 1,
            title = "भारतीय इतिहास (प्राचीन, मध्यकालीन, आधुनिक)",
            shortTitle = "भारतीय इतिहास",
            emoji = "🏛️",
            chapters = listOf(
                "पाषाण काल (Stone Age)",
                "सैंधव सभ्यता (Indus Valley Civilization)",
                "वैदिक काल",
                "बौद्ध व जैन धर्म",
                "मौर्य एवं मौर्योत्तर काल",
                "गुप्त एवं गुप्तोत्तर काल",
                "दिल्ली सल्तनत एवं मुग़ल काल",
                "आधुनिक भारत एवं राष्ट्रीय आंदोलन"
            )
        ),
        SubjectSyllabus(
            id = 2,
            title = "भारतीय राजव्यवस्था एवं शासन",
            shortTitle = "राजव्यवस्था (Polity)",
            emoji = "⚖️",
            chapters = listOf(
                "संवैधानिक विकास एवं संविधान सभा",
                "उद्देशिका, मूल अधिकार एवं नीति निदेशक तत्व",
                "संघीय कार्यपालिका एवं संसद",
                "न्यायपालिका एवं पंचायती राज",
                "प्रमुख अनुच्छेद, अनुसूचियां एवं संशोधन"
            )
        ),
        SubjectSyllabus(
            id = 3,
            title = "भारत एवं विश्व का भूगोल",
            shortTitle = "भूगोल (Geography)",
            emoji = "🌍",
            chapters = listOf(
                "भारत के भू-आकृतिक प्रदेश एवं अपवाह तंत्र",
                "जलवायु, मृदा एवं प्राकृतिक वनस्पति",
                "खनिज संसाधन, कृषि एवं सिंचाई परियोजनाएं",
                "विश्व का भूगोल (पर्वत, पठार, जलधाराएं)"
            )
        ),
        SubjectSyllabus(
            id = 4,
            title = "सामान्य विज्ञान (Physics, Chem, Bio)",
            shortTitle = "सामान्य विज्ञान",
            emoji = "🔬",
            chapters = listOf(
                "भौतिक विज्ञान (प्रकाश, ध्वनि, ऊष्मा, विद्युत)",
                "रसायन विज्ञान (अम्ल-क्षार, धातु, बहुलक)",
                "जीव विज्ञान (पाचन, परिसंचरण, विटामिन एवं रोग)"
            )
        ),
        SubjectSyllabus(
            id = 5,
            title = "पर्यावरण एवं पारिस्थितिकी",
            shortTitle = "पर्यावरण",
            emoji = "🌿",
            chapters = listOf(
                "पारिस्थितिकी तंत्र एवं जैव विविधता संरक्षण",
                "जलवायु परिवर्तन, ओजोन क्षरण एवं अंतरराष्ट्रीय सम्मेलन"
            )
        ),
        SubjectSyllabus(
            id = 6,
            title = "भारतीय अर्थव्यवस्था",
            shortTitle = "अर्थव्यवस्था",
            emoji = "📈",
            chapters = listOf(
                "राष्ट्रीय आय, पंचवर्षीय योजनाएं एवं गरीबी",
                "बैंकिंग, मौद्रिक नीति (RBI), बजट एवं सरकारी योजनाएं"
            )
        ),
        SubjectSyllabus(
            id = 7,
            title = "उत्तर प्रदेश विशेष (UP GK)",
            shortTitle = "यूपी विशेष (UP GK)",
            emoji = "🕌",
            chapters = listOf(
                "उत्तर प्रदेश का इतिहास, संस्कृति, मेले एवं लोकनृत्य",
                "यूपी का भूगोल, नदियां, वन्यजीव, खनिज एवं उद्योग"
            )
        ),
        SubjectSyllabus(
            id = 8,
            title = "जनगणना एवं नगरीकरण",
            shortTitle = "जनगणना 2011",
            emoji = "📊",
            chapters = listOf(
                "भारत एवं उत्तर प्रदेश की जनगणना (साक्षरता, लिंगानुपात, नगरीकरण)"
            )
        ),
        SubjectSyllabus(
            id = 9,
            title = "सामान्य हिंदी (विलोम, वर्तनी, अनेक शब्द, तत्सम-तद्भव, विशेषण-विशेष्य, पर्यायवाची)",
            shortTitle = "सामान्य हिंदी (RO/ARO 60 अंक)",
            emoji = "✍️",
            chapters = listOf(
                "विलोम शब्द (Antonyms)",
                "वाक्य एवं वर्तनी शुद्धि",
                "अनेक शब्दों के लिए एक शब्द",
                "तत्सम एवं तद्भव शब्द",
                "विशेष्य और विशेषण",
                "पर्यायवाची शब्द"
            )
        )
    )

    fun getOtherSubjectsQuestions(): List<QuestionEntity> {
        val historySubject = masterSyllabus[0].title
        val indusChapter = "सैंधव सभ्यता (Indus Valley Civilization)"
        val hindiSubject = masterSyllabus[8].title
        val vilomChapter = "विलोम शब्द (Antonyms)"
        val tatsamChapter = "तत्सम एवं तद्भव शब्द"
        val politySubject = masterSyllabus[1].title
        val upGkSubject = masterSyllabus[6].title

        return listOf(
            // 5+ High-Yield Questions for भारतीय इतिहास - सैंधव सभ्यता
            QuestionEntity(
                subject = historySubject,
                chapter = indusChapter,
                batchNumber = 1,
                questionNumber = 1,
                questionText = "सिंधु घाटी सभ्यता के किस पुरास्थल से 'तीन युगल समाधियां' (Three Double Burials) तथा 'गोदीबाड़ा' (Dockyard) के प्रामाणिक साक्ष्य प्राप्त हुए हैं?",
                examTag = "[UPPSC RO/ARO Pre 2021]",
                optionA = "लोथल (गुजरात)",
                optionB = "कालीबंगा (राजस्थान)",
                optionC = "धौलावीरा (गुजरात)",
                optionD = "राखीगढ़ी (हरियाणा)",
                correctOption = "a",
                detailedExplanation = "गुजरात के अहमदाबाद जिले में **भोगावा नदी** के तट पर स्थित **लोथल** की खोज **वर्ष 1954** में **एस.आर. राव** ने की थी। यहाँ से विश्व का प्राचीनतम ईंटों से निर्मित **गोदीबाड़ा (Dockyard)**, **तीन युगल शवाधान (Double Burials)**, फारस की मुहर, धान की भूसी और मनका बनाने का कारखाना प्राप्त हुआ है।"
            ),
            QuestionEntity(
                subject = historySubject,
                chapter = indusChapter,
                batchNumber = 1,
                questionNumber = 2,
                questionText = "सूची-I (हड़प्पा स्थल) को सूची-II (पुरातत्वविद/उत्खननकर्ता) से सुमेलित कीजिए:",
                examTag = "[UPPCS Pre 2020 / RO/ARO]",
                hasMatchTable = true,
                tableHeaderLeft = "सूची-I (हड़प्पा पुरास्थल)",
                tableHeaderRight = "सूची-II (उत्खननकर्ता)",
                tableRowsJson = "A. हड़प्पा|1. दयाराम साहनी (1921);;B. मोहनजोदड़ो|2. राखालदास बनर्जी (1922);;C. चन्हूदड़ो|3. एन.जी. मजूमदार / मैके;;D. धौलावीरा|4. आर.एस. बिष्ट (1990-91)",
                optionA = "A-1, B-2, C-3, D-4",
                optionB = "A-2, B-1, C-4, D-3",
                optionC = "A-1, B-3, C-2, D-4",
                optionD = "A-4, B-2, C-3, D-1",
                correctOption = "a",
                detailedExplanation = "रावी नदी के बाएं तट पर स्थित **हड़प्पा** का उत्खनन **वर्ष 1921** में **दयाराम साहनी** ने किया। सिंधु नदी के दाएं तट पर **मोहनजोदड़ो** की खोज **वर्ष 1922** में **राखालदास बनर्जी** ने की। **चन्हूदड़ो** की खोज **वर्ष 1931** में **एन.जी. मजूमदार** ने तथा कच्छ स्थित तीन भागों में विभाजित नगर **धौलावीरा** का उत्खनन **आर.एस. बिष्ट** ने किया।",
                summaryTableHeaderLeft = "सैंधव नगर",
                summaryTableHeaderRight = "नदी एवं उत्खननकर्ता",
                summaryTableRowsJson = "हड़प्पा|रावी नदी - दयाराम साहनी;;मोहनजोदड़ो|सिंधु नदी - आर.डी. बनर्जी;;चन्हूदड़ो|सिंधु नदी - एन.जी. मजूमदार;;धौलावीरा|लूनी/मानहर - आर.एस. बिष्ट"
            ),
            QuestionEntity(
                subject = historySubject,
                chapter = indusChapter,
                batchNumber = 1,
                questionNumber = 3,
                questionText = "निम्नलिखित में से किस सैंधव स्थल से 'जुते हुए खेत' (Ploughed Field) और एक साथ दो फसलें (चना एवं सरसों) उगाने के साक्ष्य मिले हैं?",
                examTag = "[UPPSC RO/ARO Mains 2016]",
                optionA = "कालीबंगा (राजस्थान)",
                optionB = "बनावली (हरियाणा)",
                optionC = "रोपड़ (पंजाब)",
                optionD = "आलमगीरपुर (उत्तर प्रदेश)",
                correctOption = "a",
                detailedExplanation = "राजस्थान के हनुमानगढ़ जिले में **घग्घर नदी** के तट पर स्थित **कालीबंगा** ('काले रंग की चूड़ियां') के प्राक्-हड़प्पा स्तर से विश्व के प्राचीनतम **जुते हुए खेत** के प्रमाण मिले हैं। यहाँ समकोण पर काटती हुई हल की रेखाओं से **चना और सरसों** की मिश्रित खेती की जाती थी। इसकी खोज **अमलानंद घोष (1951)** ने तथा उत्खनन **बी.बी. लाल व बी.के. थापर** ने किया था।"
            ),
            QuestionEntity(
                subject = historySubject,
                chapter = indusChapter,
                batchNumber = 1,
                questionNumber = 4,
                questionText = "उन्नत जल प्रबंधन प्रणाली (Water Management System), शैलकृत जलकुंड और 10 बड़े चिह्नों वाला सूचना-पट्ट (Signboard) किस हड़प्पा नगर की विशेषता है?",
                examTag = "[UPPCS Pre 2021]",
                optionA = "धौलावीरा",
                optionB = "सूरकोटदा",
                optionC = "रंगपुर",
                optionD = "देसलपुर",
                correctOption = "a",
                detailedExplanation = "गुजरात के कच्छ के रण में खादिर बेट द्वीप पर स्थित **धौलावीरा** एकमात्र ऐसा सैंधव नगर है जो तीन भागों—**दुर्ग, मध्य नगर और निचला नगर** में विभाजित था। यहाँ चट्टानों को काटकर बनाए गए **16 विशाल जलाशय** तथा उत्तरी द्वार से जिप्सम से लिखे **10 बड़े अक्षरों वाला साइनबोर्ड** मिला है। इसे **वर्ष 2021** में यूनेस्को की विश्व धरोहर सूची में शामिल किया गया।"
            ),
            QuestionEntity(
                subject = historySubject,
                chapter = indusChapter,
                batchNumber = 1,
                questionNumber = 5,
                questionText = "उत्तर प्रदेश के बागपत जिले में स्थित 'सनौली' (Sinauli) पुरास्थल किस सनसनीखेज पुरातात्विक खोज के कारण चर्चा में रहा है?",
                examTag = "[UPPSC RO/ARO Pre 2021]",
                optionA = "ताम्र-निर्मित योद्धा रथ (Chariots), ताबूत शवाधान और तांबे की एंटीना तलवारें",
                optionB = "बौद्ध विहार और अशोक के स्तंभ",
                optionC = "सोने के सिक्कों का ढेर",
                optionD = "जहाज निर्माण गोदीबाड़ा",
                correctOption = "a",
                detailedExplanation = "उत्तर प्रदेश के बागपत जिले की बड़ौत तहसील में स्थित **सनौली** में **डॉ. डी.वी. शर्मा (2005)** और **डॉ. संजय कुमार मंजुल (2018)** के नेतृत्व में हुए ASI उत्खनन से विशाल शवाधान केंद्र, **तांबे से मढ़े ठोस पहियों वाले शाही रथ**, लकड़ी के ताबूत (Coffin burials), तांबे के टोप और एंटीना तलवारें प्राप्त हुई हैं।"
            ),

            // 6+ High-Yield Questions for सामान्य हिंदी - विलोम शब्द & तत्सम-तद्भव
            QuestionEntity(
                subject = hindiSubject,
                chapter = vilomChapter,
                batchNumber = 1,
                questionNumber = 1,
                questionText = "'आविर्भाव' शब्द का सर्वाधिक उपयुक्त एवं प्रामाणिक विलोम शब्द निम्नलिखित में से कौन-सा है?",
                examTag = "[UPPSC RO/ARO Pre 2021]",
                optionA = "तिरोभाव",
                optionB = "अनाविर्भाव",
                optionC = "निराविर्भाव",
                optionD = "प्रादुर्भाव",
                correctOption = "a",
                detailedExplanation = "**डॉ. हरदेव बाहरी** एवं **डॉ. वासुदेवनंदन प्रसाद** के अनुसार **'आविर्भाव'** (प्रकट होना / उदय होना) का सटीक विलोम **'तिरोभाव'** (अदृश्य होना / लोप होना) होता है। 'प्रादुर्भाव' आविर्भाव का समानार्थी शब्द है। इसी प्रकार **'आविर्भूत'** का विलोम **'तिरोभूत'** होता है।"
            ),
            QuestionEntity(
                subject = hindiSubject,
                chapter = vilomChapter,
                batchNumber = 1,
                questionNumber = 2,
                questionText = "'उपत्यका' (Upatyaka) शब्द का सही विलोम शब्द चुनिए:",
                examTag = "[UPPSC RO/ARO Mains 2021]",
                optionA = "अधित्यका",
                optionB = "घाटी",
                optionC = "शिखर",
                optionD = "विसर्जन",
                correctOption = "a",
                detailedExplanation = "पर्वत के नीचे की तलहटी या पास की समतल भूमि को **'उपत्यका'** कहते हैं, जबकि पर्वत के ऊपर की समतल भूमि (पठार/शिखर भूमि) को **'अधित्यका'** कहा जाता है। अतः उपत्यका का प्रामाणिक विलोम **'अधित्यका'** है। यह प्रश्न अनेक शब्दों के लिए एक शब्द और विलोम दोनों में बार-बार पूछा जाता है।"
            ),
            QuestionEntity(
                subject = hindiSubject,
                chapter = vilomChapter,
                batchNumber = 1,
                questionNumber = 3,
                questionText = "सूची-I (शब्द) को सूची-II (विलोम शब्द) से सुमेलित कीजिए तथा सही कूट चुनिए:",
                examTag = "[UPPSC RO/ARO Pre 2016 Re-Exam]",
                hasMatchTable = true,
                tableHeaderLeft = "सूची-I (मूल शब्द)",
                tableHeaderRight = "सूची-II (विलोम शब्द)",
                tableRowsJson = "A. जंगम|1. स्थावर;;B. सृष्टि|2. प्रलय;;C. ईप्सित|3. अनीप्सित;;D. साहचर्य|4. अलगाव (पृथक्करण)",
                optionA = "A-1, B-2, C-3, D-4",
                optionB = "A-2, B-1, C-4, D-3",
                optionC = "A-3, B-4, C-1, D-2",
                optionD = "A-4, B-3, C-2, D-1",
                correctOption = "a",
                detailedExplanation = "आयोग की परीक्षाओं का यह सर्वाधिक प्रतिष्ठित प्रश्न है। **'जंगम'** (जो चल सके) का विलोम **'स्थावर'** (स्थिर) है। **'सृष्टि'** का विलोम **'प्रलय'**, **'ईप्सित'** (चाहा हुआ) का विलोम **'अनीप्सित'** तथा **'साहचर्य'** का विलोम **'अलगाव'** या **'वैमनस्य'** होता है।",
                summaryTableHeaderLeft = "मूल तत्सम शब्द",
                summaryTableHeaderRight = "प्रामाणिक विलोम",
                summaryTableRowsJson = "जंगम|स्थावर;;सृष्टि / व्यष्टि|प्रलय / समष्टि;;ईप्सित / अभिज्ञ|अनीप्सित / अनभिज्ञ;;साहचर्य / गौरव|अलगाव / लाघव"
            ),
            QuestionEntity(
                subject = hindiSubject,
                chapter = vilomChapter,
                batchNumber = 1,
                questionNumber = 4,
                questionText = "'मृसण' (Mrishan) शब्द का सटीक विलोम शब्द क्या है?",
                examTag = "[UPPSC RO/ARO Pre 2021]",
                optionA = "रुक्ष",
                optionB = "सुकुमार",
                optionC = "चिकना",
                optionD = "मसरूफ",
                correctOption = "a",
                detailedExplanation = "**'मृसण'** का शाब्दिक अर्थ कोमल, चिकना या स्निग्ध होता है। इसका प्रामाणिक तत्सम विलोम **'रुक्ष'** (खुरदुरा या रूखा) होता है। इसी प्रकार **'सुकुमार'** का विलोम **'कठोर'** तथा **'मसृणता'** का विलोम **'रुक्षता'** होता है।"
            ),
            QuestionEntity(
                subject = hindiSubject,
                chapter = vilomChapter,
                batchNumber = 1,
                questionNumber = 5,
                questionText = "निम्नलिखित विलोम युग्मों में से कौन-सा एक युग्म अशुद्ध (सुमेलित नहीं) है?",
                examTag = "[UPPSC RO/ARO Pre 2017]",
                optionA = "अभिज्ञ — अनभिज्ञ",
                optionB = "अथ — इति",
                optionC = "उद्धत — विनीत",
                optionD = "अनुग्रह — आग्रह",
                correctOption = "d",
                detailedExplanation = "**'अनुग्रह'** (कृपा करना) का सही विलोम **'विग्रह'** या **'कोप'** होता है, न कि 'आग्रह'। **'आग्रह'** का विलोम **'दुराग्रह'** या **'अनाग्रह'** होता है। अन्य तीनों विकल्प पूर्णतः शुद्ध हैं: **अभिज्ञ** (जानने वाला) का विलोम **अनभिज्ञ**, **अथ** (आरंभ) का **इति** (समाप्ति), तथा **उद्धत** का विलोम **विनीत** या **सौम्य** होता है।"
            ),
            QuestionEntity(
                subject = hindiSubject,
                chapter = tatsamChapter,
                batchNumber = 1,
                questionNumber = 1,
                questionText = "निम्नलिखित में से 'हल्दी' और 'गेहूं' शब्दों का सही तत्सम रूप युग्म कौन-सा है?",
                examTag = "[UPPSC RO/ARO Pre 2021]",
                optionA = "हरिद्रा एवं गोधूम",
                optionB = "हरदी एवं गोहुम",
                optionC = "हारिद्रिका एवं गोधन",
                optionD = "हल्दिका एवं गोधूली",
                correctOption = "a",
                detailedExplanation = "तद्भव शब्द **'हल्दी'** का शुद्ध तत्सम रूप **'हरिद्रा'** है, तथा **'गेहूं'** का शुद्ध तत्सम रूप **'गोधूम'** है। इसी प्रकार **'गोबर'** का तत्सम **'गोमय'**, **'गधा'** का **'गर्दभ'**, और **'हुलास'** का तत्सम **'उल्लास'** होता है।"
            ),

            // Polity & UP GK High-Yield Questions
            QuestionEntity(
                subject = politySubject,
                chapter = "उद्देशिका, मूल अधिकार एवं नीति निदेशक तत्व",
                batchNumber = 1,
                questionNumber = 1,
                questionText = "भारतीय संविधान के किस अनुच्छेद को डॉ. बी.आर. अम्बेडकर ने 'संविधान की आत्मा और हृदय' (Heart and Soul of the Constitution) कहा था?",
                examTag = "[UPPCS Pre 2019 / RO/ARO]",
                optionA = "अनुच्छेद 32 (संवैधानिक उपचारों का अधिकार)",
                optionB = "अनुच्छेद 19 (वाक् स्वातंत्र्य)",
                optionC = "अनुच्छेद 21 (प्राण एवं दैहिक स्वतंत्रता)",
                optionD = "अनुच्छेद 14 (विधि के समक्ष समता)",
                correctOption = "a",
                detailedExplanation = "संविधान सभा में चर्चा के दौरान प्रारूप समिति के अध्यक्ष **डॉ. भीमराव अम्बेडकर** ने **अनुच्छेद 32 (संवैधानिक उपचारों का अधिकार)** को संविधान की आत्मा और हृदय कहा था। इसके अंतर्गत सर्वोच्च न्यायालय मूल अधिकारों के प्रवर्तन हेतु **5 प्रकार की रिट** (बंदी प्रत्यक्षीकरण, परमादेश, प्रतिषेध, उत्प्रेषण और अधिकार-पृच्छा) जारी करता है।"
            ),
            QuestionEntity(
                subject = upGkSubject,
                chapter = "उत्तर प्रदेश का इतिहास, संस्कृति, मेले एवं लोकनृत्य",
                batchNumber = 1,
                questionNumber = 1,
                questionText = "हिंदू-मुस्लिम एकता का प्रतीक प्रसिद्ध 'सुलहकुल उत्सव' उत्तर प्रदेश के किस जिले में आयोजित किया जाता है तथा 'चरकुला' किस क्षेत्र का लोकनृत्य है?",
                examTag = "[UPPSC RO/ARO Pre 2021]",
                optionA = "आगरा (सुलहकुल) एवं ब्रजभूमि (चरकुला)",
                optionB = "लखनऊ एवं अवध",
                optionC = "वाराणसी एवं पूर्वांचल",
                optionD = "झांसी एवं बुंदेलखंड",
                correctOption = "a",
                detailedExplanation = "मुगल सम्राट अकबर द्वारा प्रतिपादित 'सुलह-ए-कुल' सिद्धांत की स्मृति में हिंदू-मुस्लिम एकता का प्रतीक **'सुलहकुल उत्सव'** प्रतिवर्ष **आगरा** में मनाया जाता है। वहीं सिर पर 108 जलते हुए दीपकों का रथ रखकर किया जाने वाला प्रसिद्ध **'चरकुला नृत्य'** उत्तर प्रदेश के **ब्रज क्षेत्र (मथुरा)** का प्रमुख घड़ा नृत्य है।"
            )
        )
    }

    fun getSeedLiveClasses(): List<LiveClassEntity> = listOf(
        LiveClassEntity(
            title = "🔴 मैराथन: प्राचीन इतिहास - पाषाण काल व सैंधव सभ्यता (2-Column Purvavlokan)",
            subject = "भारतीय इतिहास (प्राचीन, मध्यकालीन, आधुनिक)",
            chapter = "पाषाण काल (Stone Age)",
            educator = "डॉ. आलोक पाण्डेय (Ex-RO Selected & वरिष्ठ इतिहास विशेषज्ञ)",
            videoIdOrLink = "https://www.youtube.com/watch?v=uppsc_ro_aro_live_2026",
            startTime = "LIVE NOW • शाम 07:00 बजे से",
            durationText = "02:15:00",
            isLiveNow = true,
            timestampsText = "00:00 पाठ्यक्रम व रणनीति;;14:20 रॉबर्ट ब्रूस फूट व पुरापाषाण काल;;38:45 मध्यपाषाण: महदहा, दमदमा व सराय नाहर राय;;01:12:10 नवपाषाण: लहुरादेवा बनाम मेहरगढ़ विवाद;;01:45:00 50 अति-संभावित PYQ अभ्यास",
            notesAttachmentTitle = "पाषाण_काल_2_Column_Print_Notes.pdf"
        ),
        LiveClassEntity(
            title = "सामान्य हिंदी (60/60 लक्ष्य): विलोम एवं तत्सम-तद्भव के 200 रामबाण शब्द",
            subject = "सामान्य हिंदी (विलोम, वर्तनी, अनेक शब्द, तत्सम-तद्भव, विशेषण-विशेष्य, पर्यायवाची)",
            chapter = "विलोम शब्द (Antonyms)",
            educator = "आचार्य विनय त्रिपाठी (हिंदी विशेषज्ञ - हरदेव बाहरी विशेष)",
            videoIdOrLink = "https://www.youtube.com/watch?v=ro_aro_hindi_masterclass",
            startTime = "रिकॉर्डेड सत्र • कल रात 08:30 बजे",
            durationText = "01:48:20",
            isLiveNow = false,
            timestampsText = "00:00 उपसर्ग आधारित विलोम नियम;;22:15 डॉ. हरदेव बाहरी के क्लिष्ट शब्द;;54:30 RO/ARO 2016 व 2021 पेपर हल;;01:25:00 रैपिड फायर 100 प्रश्न",
            notesAttachmentTitle = "सामान्य_हिंदी_विलोम_तत्सम_सारणी.pdf"
        ),
        LiveClassEntity(
            title = "उत्तर प्रदेश बजट 2025-26 एवं यूपी जनगणना विशेषांक",
            subject = "उत्तर प्रदेश विशेष (UP GK)",
            chapter = "यूपी का भूगोल, नदियां, वन्यजीव, खनिज एवं उद्योग",
            educator = "इं. विकास वर्मा (UPPCS Mentor)",
            videoIdOrLink = "https://www.youtube.com/watch?v=up_special_gk_2026",
            startTime = "रिकॉर्डेड सत्र • 3 दिन पूर्व",
            durationText = "01:32:10",
            isLiveNow = false,
            timestampsText = "00:00 यूपी की प्रमुख योजनाएं;;29:40 एक जिला एक उत्पाद (ODOP);;58:10 उत्तर प्रदेश के वन्यजीव विहार व रामसर स्थल",
            notesAttachmentTitle = "UP_GK_Pariksha_Vani_Gist.pdf"
        )
    )

    fun getSeedPdfNotes(): List<PdfNoteEntity> = listOf(
        PdfNoteEntity(
            title = "प्राचीन इतिहास: पाषाण काल एवं सैंधव सभ्यता (2-Column Print Ready)",
            category = "Handwritten Topper Notes",
            subject = "भारतीय इतिहास",
            pageCount = 28,
            fileSizeText = "4.2 MB",
            isDownloadedOffline = true,
            contentMarkdown = """
                # 🏛️ अध्याय 1: पाषाण काल (Stone Age) - टॉपर शॉर्ट नोट्स
                
                ### 1. प्रमुख खोजकर्ता एवं तिथियां
                - **रॉबर्ट ब्रूस फूट (13 मई 1863):** मद्रास के **पल्लवरम** से प्रथम हस्तकुठार (Handaxe) खोजा। इन्हें **'भारतीय प्रागैतिहास का जनक'** कहा जाता है।
                - **सी.जे. थॉमसन (1818/1836):** कोपेनहेगन संग्रहालय की सामग्री के आधार पर पाषाण, कांस्य व लौह युग का त्रियुगीन विभाजन किया।
                - **डॉ. अरुण सोनकिया (5 दिसंबर 1982):** सीहोर (म.प्र.) की नर्मदा घाटी के **हथनौरा** से प्रथम मानव कपाल जीवाश्म (**नर्मदा मानव**) खोजा।
                - **डॉ. विष्णुधर वाकणकर (1957-58):** रायसेन (म.प्र.) में **भीमबेटका** की प्रागैतिहासिक चित्रित गुफाओं की खोज की।
                
                ### 2. मध्य गंगा घाटी (प्रतापगढ़, उ.प्र.) के तीन रत्न
                | पुरास्थल | प्रमुख पुरातात्विक साक्ष्य |
                | :--- | :--- |
                | **सराय नाहर राय** | 11 समाधियां, 8 गर्त-चूल्हे, एक कब्र में 4 कंकाल, खोपड़ी में धंसा बाणाग्र (युद्ध का प्रथम साक्ष्य) |
                | **महदहा** | बूचड़खाना संकुल, हड्डी व मृगशृंग के आभूषण (कंठहार, कर्णफूल), युगल समाधि |
                | **दमदमा** | कुल 41 मानव शवाधान, 5 युगल समाधियां, **1 त्रि-मानव शवाधान (3 कंकाल एक साथ)** |
                
                ### 3. कृषि एवं पशुपालन के प्राचीनतम साक्ष्य
                - **पशुपालन (5000 ई.पू.):** **बागोर** (भीलवाड़ा, राजस्थान - कोठारी नदी) एवं **आदमगढ़** (नर्मदापुरम, म.प्र.)।
                - **कृषि (नवीनतम शोध):** **लहुरादेवा** (संत कबीर नगर, उ.प्र. - **9000 से 7000 ई.पू.** चावल के साक्ष्य)।
                - **मेहरगढ़ (बलूचिस्तान):** बोलन नदी के तट पर **7000 ई.पू.** गेहूं व जौ, भैंस पालन, और जीवित दांतों में ड्रिलिंग (Proto-dentistry) के प्रमाण।
            """.trimIndent()
        ),
        PdfNoteEntity(
            title = "सामान्य हिंदी: डॉ. हरदेव बाहरी एवं वासुदेवनंदन प्रसाद सार-संग्रह (60 अंक)",
            category = "Official NCERT Gist",
            subject = "सामान्य हिंदी",
            pageCount = 42,
            fileSizeText = "5.8 MB",
            isDownloadedOffline = true,
            contentMarkdown = """
                # ✍️ RO/ARO सामान्य हिंदी (60/60 स्कोरर गाइड)
                
                ### 1. अति-महत्वपूर्ण विलोम शब्द (आयोग द्वारा बार-बार पूछे गए)
                - **आविर्भाव** ⟷ तिरोभाव
                - **जंगम** ⟷ स्थावर
                - **उपत्यका** ⟷ अधित्यका
                - **मृसण** ⟷ रुक्ष
                - **ईप्सित** ⟷ अनीप्सित
                - **साहचर्य** ⟷ अलगाव / पृथक्करण
                - **उद्धत** ⟷ विनीत
                - **प्रसारण** ⟷ आकुंचन
                - **ऋजु** ⟷ वक्र
                - **अज्ञ** ⟷ विज्ञ
                
                ### 2. शुद्ध-अशुद्ध वर्तनी के स्वर्णिम नियम
                - **शुद्ध वर्तनी:** कवयित्री, उज्ज्वल, हिरण्यकशिपु, सांकृत्यायन, अनुषंगिक, जाज्वल्यमान, संन्यासी, वाल्मीकि, द्वारका, अहल्या।
            """.trimIndent()
        ),
        PdfNoteEntity(
            title = "UPPSC RO/ARO फुल लेंथ मॉडल प्रश्न-पत्र 01 (140 GS + 60 हिंदी)",
            category = "Model Question Papers",
            subject = "टेस्ट सीरीज़ मॉडल पेपर",
            pageCount = 36,
            fileSizeText = "6.1 MB",
            isDownloadedOffline = false,
            contentMarkdown = """
                # 📝 UPPSC RO/ARO प्रारंभिक परीक्षा मॉडल प्रश्न-पत्र 2026
                - **पूर्णांक:** 200 अंक (GS: 140 अंक + सामान्य हिंदी: 60 अंक)
                - **ऋणात्मक अंकन (Negative Marking):** एक-तिहाई (-0.33)
                - **लक्ष्य कट-ऑफ:** 132+ अंक
                
                इस मॉडल पेपर में घटनाचक्र पूर्वावलोकन और परीक्षा वाणी के नवीनतम आंकड़ों पर आधारित कूट एवं कथन-कारण वाले प्रश्न शामिल हैं।
            """.trimIndent()
        ),
        PdfNoteEntity(
            title = "भारतीय राजव्यवस्था: एम. लक्ष्मीकांत एवं घटनाचक्र अनुच्छेद सारणी",
            category = "Purvavlokan 2-Column Notes",
            subject = "भारतीय राजव्यवस्था",
            pageCount = 54,
            fileSizeText = "7.4 MB",
            isDownloadedOffline = true,
            contentMarkdown = """
                # ⚖️ भारतीय संविधान एवं राजव्यवस्था (2-कॉलम रिवीजन नोट्स)
                
                ### 1. प्रमुख संवैधानिक अनुच्छेद एवं संशोधन
                - **अनुच्छेद 14 से 18:** समता का अधिकार (विधि के समक्ष समता एवं अस्पृश्यता का अंत - अनुच्छेद 17)।
                - **अनुच्छेद 21:** प्राण एवं दैहिक स्वतंत्रता का संरक्षण (मेनका गांधी वाद 1978)।
                - **अनुच्छेद 32:** संवैधानिक उपचारों का अधिकार — **डॉ. बी.आर. अम्बेडकर** के अनुसार संविधान की आत्मा।
                - **42वां संविधान संशोधन (1976):** स्वर्ण सिंह समिति की सिफारिश पर भाग 4(क) व अनुच्छेद 51(क) में **मूल कर्तव्य** जोड़े गए।
                - **73वां एवं 74वां संशोधन (1992):** पंचायती राज (11वीं अनुसूची - 29 विषय) एवं नगरपालिकाएं (12वीं अनुसूची - 18 विषय)।
            """.trimIndent()
        ),
        PdfNoteEntity(
            title = "उत्तर प्रदेश विशेष (UP GK): परीक्षा वाणी संपूर्ण सार एवं बजट 2025-26",
            category = "Purvavlokan 2-Column Notes",
            subject = "उत्तर प्रदेश विशेष",
            pageCount = 48,
            fileSizeText = "6.5 MB",
            isDownloadedOffline = false,
            contentMarkdown = """
                # 🕌 उत्तर प्रदेश विशेष (UP Special GK) - परीक्षा वाणी 2-कॉलम नोट्स
                
                ### 1. यूपी के प्रमुख सांस्कृतिक केंद्र, मेले एवं लोकनृत्य
                - **सुलहकुल उत्सव:** आगरा (हिंदू-मुस्लिम एकता का प्रतीक)।
                - **चरकुला नृत्य:** ब्रजभूमि (मथुरा) - 108 दीपकों का घड़ा नृत्य।
                - **कर्मा एवं शैला नृत्य:** सोनभद्र एवं मिर्जापुर (खरवार व गोंड जनजाति)।
                - **भातखंडे संगीत संस्थान:** लखनऊ (स्थापना 1926 - मैरिस कॉलेज ऑफ म्यूजिक)।
                
                ### 2. उत्तर प्रदेश के प्रमुख वन्यजीव विहार एवं रामसर स्थल
                - **दुधवा राष्ट्रीय उद्यान:** लखीमपुर खीरी (1977 में राष्ट्रीय उद्यान, 1987 में टाइगर रिजर्व)।
                - **चंद्रप्रभा वन्यजीव विहार:** चंदौली (1957 - उत्तर प्रदेश का प्रथम वन्यजीव विहार)।
            """.trimIndent()
        ),
        PdfNoteEntity(
            title = "भारत एवं विश्व का भूगोल: NCERT (कक्षा 6-12) मानचित्र आधारित नोट्स",
            category = "Official NCERT Gist",
            subject = "भूगोल एवं पर्यावरण",
            pageCount = 64,
            fileSizeText = "8.9 MB",
            isDownloadedOffline = false,
            contentMarkdown = """
                # 🌍 भारत एवं विश्व का भूगोल - NCERT व माजिद हुसैन सार
                
                ### 1. हिमालय के प्रमुख दर्रे एवं अपवाह तंत्र
                - **काराकोरम, जोजिला, बनिहाल:** जम्मू-कश्मीर एवं लद्दाख क्षेत्र।
                - **शिपकी ला, रोहतांग, बाड़ालाचा ला:** हिमाचल प्रदेश (सतलज नदी शिपकी ला से भारत में प्रवेश करती है)।
                - **माना, नीति, लिपुलेख:** उत्तराखंड।
                - **नाथू ला एवं जेलेप ला:** सिक्किम।
                
                ### 2. पर्यावरण संरक्षण अधिनियम एवं संधियां
                - **वन्यजीव संरक्षण अधिनियम:** 1972 | **प्रोजेक्ट टाइगर:** 1973 | **पर्यावरण संरक्षण अधिनियम:** 1986
                - **मॉन्ट्रियल प्रोटोकॉल (16 सितंबर 1987):** ओजोन परत संरक्षण।
            """.trimIndent()
        ),
        PdfNoteEntity(
            title = "तत्सम-तद्भव, विशेषण-विशेष्य एवं पर्यायवाची: टॉपर हस्तलिखित ट्रिक्स",
            category = "Handwritten Topper Notes",
            subject = "सामान्य हिंदी",
            pageCount = 34,
            fileSizeText = "4.7 MB",
            isDownloadedOffline = true,
            contentMarkdown = """
                # ✍️ सामान्य हिंदी: तत्सम-तद्भव एवं विशेषण-विशेष्य मास्टर नोट्स
                
                ### 1. आयोग के सर्वाधिक पूछे जाने वाले तत्सम-तद्भव युग्म
                - **हरिद्रा** ⟷ हल्दी | **गोधूम** ⟷ गेहूं | **गोमय** ⟷ गोबर
                - **कर्पट** ⟷ कपड़ा | **कपूर** ⟷ कर्पूर | **मक्षिका** ⟷ मक्खी
                - **नारिकेल** ⟷ नारियल | **गृध्र** ⟷ गिद्ध | **श्यामल** ⟷ सांवला
                
                ### 2. प्रमुख पर्यायवाची शब्द समूह
                - **यमुना:** कालिंदी, अर्कजा, रवितनया, कृष्णा, सूर्यसुता, भानुजा।
                - **कमल:** इंदीवर, पुंडरीक, उत्पल, कोकनद, शतदल, सरसिज।
            """.trimIndent()
        ),
        PdfNoteEntity(
            title = "UPPSC PCS एवं RO/ARO 10-वर्षीय हल प्रश्न-पत्र (2014-2024 PYQ पेपर)",
            category = "Model Question Papers",
            subject = "टेस्ट सीरीज़ मॉडल पेपर",
            pageCount = 92,
            fileSizeText = "11.4 MB",
            isDownloadedOffline = false,
            contentMarkdown = """
                # 📝 विगत 10 वर्षों के प्रामाणिक हल प्रश्न-पत्र (UPPSC Official Key सहित)
                
                - **RO/ARO Pre 2021 (5 दिसंबर 2021):** GS 140 प्रश्न + सामान्य हिंदी 60 प्रश्न।
                - **RO/ARO Pre 2016 Re-Exam (20 सितंबर 2020):** आयोग की संशोधित उत्तर-कुंजी सहित विस्तृत व्याख्या।
                - **UPPCS Pre 2023 एवं 2022:** कूट मिलान व कालानुक्रमिक प्रश्नों का विशेष संकलन।
            """.trimIndent()
        )
    )

    fun getSeedDoubtPosts(): List<DoubtPostEntity> = listOf(
        DoubtPostEntity(
            studentName = "अनुराग तिवारी (प्रयागराज)",
            subject = "भारतीय इतिहास",
            questionReference = "प्रश्न संख्या [06]: कृषि के प्राचीनतम साक्ष्य",
            doubtText = "सर, यदि परीक्षा में 'भारतीय उपमहाद्वीप में कृषि के प्राचीनतम साक्ष्य' पूछा जाए और विकल्प में लहुरादेवा तथा मेहरगढ़ दोनों हों, तो आयोग किसको सही मानता है?",
            mentorName = "डॉ. आलोक पाण्डेय (चयनित समीक्षा अधिकारी)",
            mentorReply = "बिल्कुल स्पष्ट रखें: यदि विकल्प में 'लहुरादेवा' (9000-7000 ई.पू.) दिया गया है, तो UPPSC अब सदैव लहुरादेवा को ही सही उत्तर मानता है। केवल लहुरादेवा के विकल्प में न होने पर ही 'मेहरगढ़' (7000 ई.पू.) चुनें।",
            isVerifiedByTeacher = true,
            upvotes = 48,
            timestampText = "2 घंटे पूर्व"
        ),
        DoubtPostEntity(
            studentName = "प्रिया सिंह (लखनऊ)",
            subject = "सामान्य हिंदी",
            questionReference = "विलोम शब्द - अज्ञ बनाम अभिज्ञ",
            doubtText = "सर, 'अभिज्ञ' का विलोम 'अज्ञ' होगा या 'अनभिज्ञ'? इसमें बहुत भ्रम होता है।",
            mentorName = "आचार्य विनय त्रिपाठी (हिंदी विशेषज्ञ)",
            mentorReply = "डॉ. हरदेव बाहरी के अनुसार 'अभिज्ञ' (जानकार) का प्राथमिक और सर्वमान्य विलोम 'अनभिज्ञ' है। वहीं 'अज्ञ' (मूर्ख/न जानने वाला) का सटीक विलोम 'विज्ञ' या 'प्रज्ञ' होता है। विकल्प में 'अनभिज्ञ' होने पर वही टिक करें।",
            isVerifiedByTeacher = true,
            upvotes = 35,
            timestampText = "5 घंटे पूर्व"
        )
    )

    fun getInitialSampleAttempt(): TestAttemptEntity = TestAttemptEntity(
        testTitle = "RO/ARO GS + हिंदी सेक्शनल मॉक टेस्ट #01",
        paperType = "GS Paper-1 & Hindi Combined",
        totalQuestions = 20,
        correctCount = 15,
        incorrectCount = 3,
        unattemptedCount = 2,
        netScore = 14.01,
        maxMarks = 20.0,
        accuracyPercent = 83.3,
        percentile = 94.6,
        weakAreaSubject = "उत्तर प्रदेश विशेष (UP GK)"
    )
}
