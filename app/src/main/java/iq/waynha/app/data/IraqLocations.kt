package iq.waynha.app.data

import iq.waynha.app.model.Governorate

object IraqLocations {
    val governorates = listOf(
        Governorate(
            id = "baghdad",
            nameAr = "بغداد",
            nameEn = "Baghdad",
            centerLat = 33.3152,
            centerLng = 44.3661,
            districts = listOf(
                "الكل", "الكرادة", "المنصور", "اليرموك", "زيونة", "حي الجامعة",
                "الأعظمية", "الكاظمية", "الحارثية", "شارع فلسطين", "الدورة",
                "السيدية", "العامرية", "الغزالية", "البنوك", "الشعب", "بغداد الجديدة",
                "الزعفرانية", "الشورجة", "السنك", "الشيخ عمر", "الصالحية"
            )
        ),
        Governorate(
            id = "basra",
            nameAr = "البصرة",
            nameEn = "Basra",
            centerLat = 30.5085,
            centerLng = 47.8340,
            districts = listOf("الكل", "العشار", "الجزائر", "المعقل", "الطويسة", "الجنينة", "البريهة", "القبلة", "الزبير", "أبي الخصيب", "القرنة")
        ),
        Governorate(
            id = "erbil",
            nameAr = "أربيل",
            nameEn = "Erbil",
            centerLat = 36.2167,
            centerLng = 43.9961,
            districts = listOf("الكل", "عينكاوة", "بختياري", "شارع 100 متري", "شارع 60 متري", "الإسكان", "شورش", "القرية الإنجليزية")
        ),
        Governorate(
            id = "najaf",
            nameAr = "النجف الأشرف",
            nameEn = "Najaf",
            centerLat = 32.0259,
            centerLng = 44.3462,
            districts = listOf("الكل", "المدينة القديمة", "حي الأمير", "الكوفة", "حي الحنانة", "حي السعد", "حي الغري", "حي الزهراء")
        ),
        Governorate(
            id = "karbala",
            nameAr = "كربلاء المقدسة",
            nameEn = "Karbala",
            centerLat = 32.6160,
            centerLng = 44.0249,
            districts = listOf("الكل", "باب بغداد", "العباسية", "حي الحسين", "حي المعلمين", "الإسكان", "حي النقيب", "الهندية")
        ),
        Governorate(
            id = "sulaymaniyah",
            nameAr = "السليمانية",
            nameEn = "Sulaymaniyah",
            centerLat = 35.5669,
            centerLng = 45.4167,
            districts = listOf("الكل", "سرشنار", "بختياري", "شارع سالم", "رابرين", "كوردسات")
        ),
        Governorate(
            id = "ninawa",
            nameAr = "نينوى (الموصل)",
            nameEn = "Nineveh (Mosul)",
            centerLat = 36.3400,
            centerLng = 43.1300,
            districts = listOf("الكل", "الجانب الأيسر", "الجانب الأيمن", "حي الجامعة", "النبي يونس", "الزهور", "الغابات", "الدواسة")
        ),
        Governorate(
            id = "kirkuk",
            nameAr = "كركوك",
            nameEn = "Kirkuk",
            centerLat = 35.4667,
            centerLng = 44.3833,
            districts = listOf("الكل", "شارع القدس", "طريق بغداد", "رحيماوا", "المصلى", "الشورجة", "حي الواسطي")
        ),
        Governorate(
            id = "babil",
            nameAr = "بابل (الحلة)",
            nameEn = "Babil (Hillah)",
            centerLat = 32.4833,
            centerLng = 44.4333,
            districts = listOf("الكل", "شارع 40", "حي الجمعية", "نادر", "حي بابل", "حي المعلمين", "المحاويل")
        ),
        Governorate(
            id = "dhi_qar",
            nameAr = "ذي قار (الناصرية)",
            nameEn = "Dhi Qar (Nasiriyah)",
            centerLat = 31.0500,
            centerLng = 46.2667,
            districts = listOf("الكل", "حي الحسين", "حي أور", "الشامية", "شارع الحبوبي", "الإدارة المحلية")
        ),
        Governorate(
            id = "anbar",
            nameAr = "الأنبار",
            nameEn = "Anbar",
            centerLat = 33.4167,
            centerLng = 43.3000,
            districts = listOf("الكل", "الرمادي - شارع المستودع", "الرمادي - التأميم", "الفلوجة - العام", "الفلوجة - الضباط", "هيت")
        ),
        Governorate(
            id = "diyala",
            nameAr = "ديالى",
            nameEn = "Diyala",
            centerLat = 33.7500,
            centerLng = 44.6500,
            districts = listOf("الكل", "بعقوبة - حي المعلمين", "بعقوبة - شفتة", "بعقوبة - التحرير", "المقدادية", "بلدروز")
        ),
        Governorate(
            id = "wasit",
            nameAr = "واسط (الكوت)",
            nameEn = "Wasit (Kut)",
            centerLat = 32.5167,
            centerLng = 45.8333,
            districts = listOf("الكل", "الكوت - داموك", "الكوت - حي الزهراء", "الكوت - الهورة", "الحي", "الصويرة")
        ),
        Governorate(
            id = "maysan",
            nameAr = "ميسان (العمارة)",
            nameEn = "Maysan (Amara)",
            centerLat = 31.8333,
            centerLng = 47.1500,
            districts = listOf("الكل", "العمارة - قطاع 28", "حي المعلمين", "حي الحسين", "الدبيسات")
        ),
        Governorate(
            id = "qadisiyyah",
            nameAr = "الديوانية (القادسية)",
            nameEn = "Diwaniyah",
            centerLat = 31.9833,
            centerLng = 44.9167,
            districts = listOf("الكل", "الديوانية - الصوب الكبير", "حي الفرات", "حي الجزائر", "حي الضباط")
        ),
        Governorate(
            id = "saladin",
            nameAr = "صلاح الدين",
            nameEn = "Saladin",
            centerLat = 34.6000,
            centerLng = 43.6833,
            districts = listOf("الكل", "تكريت - حي الزهور", "تكريت - شارع الأربعين", "سامراء - القاطول", "بلد", "الدجيل")
        ),
        Governorate(
            id = "duhok",
            nameAr = "دهوك",
            nameEn = "Duhok",
            centerLat = 36.8667,
            centerLng = 42.9833,
            districts = listOf("الكل", "مركز دهوك", "حي مالطا", "شارع كلي", "ماسيك", "زاخو")
        ),
        Governorate(
            id = "muthanna",
            nameAr = "المثنى (السماوة)",
            nameEn = "Muthanna (Samawah)",
            centerLat = 31.3167,
            centerLng = 45.2833,
            districts = listOf("الكل", "السماوة - الغربي", "حي المعلمين", "حي الجمهوري", "الرميثة")
        )
    )

    fun getDefaultGovernorate(): Governorate = governorates.first() // Baghdad is first
}
