package iq.waynha.app.data

import iq.waynha.app.model.CategoryType
import iq.waynha.app.model.Listing
import iq.waynha.app.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object ListingRepository {
    private val initialListings = listOf(
        Listing(
            id = "w-1",
            title = "الأستاذ حيدر - كهربائي منازل وتأسيسات ذكية",
            titleEn = "Haider - Master Electrician & Wiring",
            category = CategoryType.ELECTRICIANS,
            governorate = "بغداد",
            district = "الكرادة",
            addressDetails = "الكرادة داخل - قرب ساحة كهرمانة",
            description = "كهربائي منازل متمرس 15 سنة خبرة. فحص شورت وتأسيس كامل 3 فيز، صيانة بوردات المولدات والتحويل الأوتوماتيكي ATS، تركيب سبالت، وإنارة مخفية وديكورات. متواجد لخدمة الطوارئ السريعة بالكرادة والمناطق المجاورة.",
            phone = "07701234567",
            whatsapp = "9647701234567",
            priceNote = "كشفية 15,000 د.ع وتحديد السعر حسب العمل",
            workingHours = "8:00 ص - 11:00 م (خدمة طوارئ)",
            rating = 4.9f,
            reviewsCount = 38,
            reviews = listOf(
                Review("r1", "علي المفرجي", 5, "عاشت إيدك أستاذ حيدر، إجاني بربع ساعة وحل مشكلة الشورت بالبورد.", "منذ يومين"),
                Review("r2", "م. سيف السعدي", 5, "شغل دقيق ومضبوط وأخلاق عالية جداً وأسعاره معقولة.", "منذ أسبوع")
            ),
            isVerified = true,
            isFeatured = true,
            lat = 33.3089,
            lng = 44.4265,
            tags = listOf("كهربائي", "اريد كهربائي قريب", "تأسيسات", "شورت", "مولدة", "ats", "سبلت"),
            imageUrl = "https://images.unsplash.com/photo-1621905251189-08b45d6a269e?w=800&auto=format&fit=crop&q=80"
        ),
        Listing(
            id = "w-2",
            title = "شركة الفرات لقطع غيار تويوتا وهونداي الأصلية",
            titleEn = "Al-Furat Auto Parts (Toyota & Hyundai)",
            category = CategoryType.AUTO_PARTS,
            governorate = "بغداد",
            district = "السنك",
            addressDetails = "شارع الرشيد - سوق السنك للأدوات الاحتياطية",
            description = "أكبر مركز متخصص بقطع الغيار الأصلية واليابانية والكورية مع ضمان الفحص. متوفر لدينا سفايف بريك، دبلات، فلاتر، بلكات دينمو، ومضخات وقود لكافة موديلات التويوتا والهيونداي.",
            phone = "07804445566",
            whatsapp = "9647804445566",
            priceNote = "أسعار جملة ومفرد مع وصل فحص",
            workingHours = "8:30 ص - 6:00 م (الجمعة عطلة)",
            rating = 4.8f,
            reviewsCount = 52,
            reviews = listOf(
                Review("r3", "كرار الزيدي", 5, "لقيت عدهم حساس كير كامري أصلي بعد ما فريت كل بغداد عليه.", "منذ 3 أيام")
            ),
            isVerified = true,
            isFeatured = true,
            lat = 33.3320,
            lng = 44.4025,
            tags = listOf("قطع سيارات", "اريد قطعة سيارة", "ادوات احتياطية", "تويوتا", "هيونداي", "السنك"),
            imageUrl = "https://images.unsplash.com/photo-1486006920555-c77dce18193b?w=800&auto=format&fit=crop&q=80"
        ),
        Listing(
            id = "w-3",
            title = "المهندس مصطفى - صيانة سبالت وتبريد وتكييف مركزي",
            titleEn = "Eng. Mustafa - HVAC & AC Repair Service",
            category = CategoryType.SERVICES,
            governorate = "بغداد",
            district = "اليرموك",
            addressDetails = "اليرموك - شارع 4 بنك - خدمة منزلية متنقلة",
            description = "فني محترف لصيانة وغسيل أجهزة التبريد (سبالت كنتوري وجداري، بكج، دكت)، فحص وشحن غاز أمريكي أصلي R22 و R410، تبديل كابستر، وكشف تسريب الغاز بالنيتروجين.",
            phone = "07730001122",
            whatsapp = "9647730001122",
            priceNote = "غسيل سبلت 20,000 د.ع / شحن غاز 35,000 د.ع",
            workingHours = "7:30 ص - 10:00 م طيلة أيام الأسبوع",
            rating = 4.9f,
            reviewsCount = 46,
            reviews = listOf(
                Review("r4", "أم مصطفى", 5, "غسل سبلت الصالة بجهاز ضغط خاص وتبريده صار ثلج.", "منذ يوم")
            ),
            isVerified = true,
            isFeatured = true,
            lat = 33.3010,
            lng = 44.3320,
            tags = listOf("سبلت", "تبريد", "اريد مصلح سبالت", "غاز", "غسيل سبلت", "اليرموك"),
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&auto=format&fit=crop&q=80"
        ),
        Listing(
            id = "w-4",
            title = "مطعم كباب بغداد القديم - المنصور",
            titleEn = "Old Baghdad Kebab Restaurant - Mansour",
            category = CategoryType.RESTAURANTS,
            governorate = "بغداد",
            district = "المنصور",
            addressDetails = "شارع 14 رمضان - مقابل تقاطع الرواد",
            description = "أصيل المطبخ العراقي منذ 1978. كباب لحم غنم طازج عراقي مشوي على الفحم الحجري، كص لحم ودجاج، مقبلات بغدادية حارة وطازجة، وخبز تنور حار على مدار الساعة.",
            phone = "07718889900",
            whatsapp = "9647718889900",
            priceNote = "نفر كباب خاص 14,000 د.ع مع المقبلات",
            workingHours = "11:30 ص - 1:00 بعد منتصف الليل",
            rating = 4.9f,
            reviewsCount = 120,
            reviews = listOf(
                Review("r5", "د. يوسف التميمي", 5, "أطيب كباب ببغداد بدون منازع ولحم عراقي أصلي.", "أمس")
            ),
            isVerified = true,
            isFeatured = true,
            lat = 33.3128,
            lng = 44.3546,
            tags = listOf("مطعم", "اريد مطعم", "كباب", "مشويات", "المنصور", "عشاء"),
            imageUrl = "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800&auto=format&fit=crop&q=80"
        ),
        Listing(
            id = "w-5",
            title = "الأسطة أبو كرار - فيتر وميكانيك عام سيارات حديثة",
            titleEn = "Master Abu Karrar - Engine & Transmission Mechanic",
            category = CategoryType.MECHANICS,
            governorate = "بغداد",
            district = "الشيخ عمر",
            addressDetails = "الشيخ عمر - الفرع المقابل لساحة الطيران",
            description = "ورشة فحص كمبيوتر وتصليح محركات وكيرات أوتوماتيك وسكانر حديث. تبديل قوايش صدر، تصليح هيد، وفحص دبلات ومنظومة التوجيه والهيدروليك لكافة السيارات.",
            phone = "07801112233",
            whatsapp = "9647801112233",
            priceNote = "فحص كمبيوتر 15,000 د.ع",
            workingHours = "8:00 ص - 6:30 م",
            rating = 4.8f,
            reviewsCount = 27,
            reviews = listOf(
                Review("r6", "عمر البغدادي", 5, "فيتر أمين وفاهم شغله وما يبدل قطعة إلا وهو متأكد منها.", "منذ 4 أيام")
            ),
            isVerified = true,
            lat = 33.3350,
            lng = 44.4080,
            tags = listOf("ميكانيكي", "فيتر", "اريد فيتر", "تصليح سيارات", "كمبيوتر", "كير"),
            imageUrl = "https://images.unsplash.com/photo-1619642751034-765dfdf7c58e?w=800&auto=format&fit=crop&q=80"
        ),
        Listing(
            id = "w-6",
            title = "أبو سجاد البغدادي - سباك صحيات وكشف النضح بالأجهزة",
            titleEn = "Abu Sajjad - Plumbing & Electronic Leak Detection",
            category = CategoryType.PLUMBERS,
            governorate = "بغداد",
            district = "الأعظمية",
            addressDetails = "الأعظمية - شارع عمر بن عبد العزيز",
            description = "سباك صحيات متقدم، جهاز إلكتروني لكشف تسريب الماء تحت الكاشي بدون تكسير عشوائي. تركيب مضخات ماء، صيانة سخانات، تأسيس بوري حراري ألماني وتركي للحمامات والمطابخ.",
            phone = "07705554433",
            whatsapp = "9647705554433",
            priceNote = "كشف النضح وتحديد المكان 25,000 د.ع",
            workingHours = "7:30 ص - 9:00 م",
            rating = 4.9f,
            reviewsCount = 33,
            reviews = listOf(
                Review("r7", "حسين القيسي", 5, "حدد مكان البوري المكسور بالسنتيمتر وخلصنا من مشكلة رطوبة قديمة.", "منذ أسبوع")
            ),
            isVerified = true,
            lat = 33.3640,
            lng = 44.3680,
            tags = listOf("سباك", "صحيات", "اريد سباك", "كشف نضح", "بوري", "مضخة"),
            imageUrl = "https://images.unsplash.com/photo-1504148455328-c376907d081c?w=800&auto=format&fit=crop&q=80"
        ),
        Listing(
            id = "w-7",
            title = "فرصة عمل: مطلوب كاشير ومسؤول مبيعات في معرض تجاري",
            titleEn = "Job: Showroom Cashier & Sales Representative",
            category = CategoryType.JOBS,
            governorate = "بغداد",
            district = "زيونة",
            addressDetails = "شارع الربيعي - مجمع النخبة التجاري",
            description = "تعلن شركة تجارية عن توفر شاغر كاشير ومبيعات براتب شهري مجزي وحوافز مبيعات أسبوعية. الشروط: حسن المظهر واللباقة، التفرغ التام للدوام الصباحي أو المسائي.",
            phone = "07722221199",
            whatsapp = "9647722221199",
            priceNote = "الراتب: 800,000 - 1,000,000 د.ع",
            workingHours = "شفت صباحي: 9:00 ص - 4:00 م",
            rating = 4.7f,
            reviewsCount = 12,
            reviews = listOf(
                Review("r8", "أحمد العبيدي", 5, "إدارة محترمة وموقع مريح بالربيعي.", "منذ 5 أيام")
            ),
            isVerified = true,
            lat = 33.3280,
            lng = 44.4450,
            tags = listOf("عمل", "اريد عمل", "وظيفة", "كاشير", "مبيعات", "زيونة"),
            imageUrl = "https://images.unsplash.com/photo-1521737604893-d14cc237f11d?w=800&auto=format&fit=crop&q=80"
        ),
        Listing(
            id = "w-8",
            title = "سوق دجلة للمواد المنزلية والأجهزة الكهربائية",
            titleEn = "Tigris Home Electronics & Appliances Store",
            category = CategoryType.SHOPS,
            governorate = "بغداد",
            district = "شارع فلسطين",
            addressDetails = "شارع فلسطين - قرب ماكولات الصخرة",
            description = "متجر متكامل لكافة الأجهزة المنزلية الأصلية بضمان سنة كاملة. شاشات ذكية 4K، غسالات، طباخات ليزرية، أفران، وخلاطات مع خدمة توصيل ونصب لجميع مناطق بغداد.",
            phone = "07709991122",
            whatsapp = "9647709991122",
            priceNote = "أسعار تنافسية مع خدمة التوصيل",
            workingHours = "10:00 ص - 10:00 م",
            rating = 4.8f,
            reviewsCount = 41,
            reviews = listOf(
                Review("r9", "مروة طارق", 5, "شريت منهم شاشة وغسالة وصلوها بنفس اليوم والضمان رسمي.", "منذ 6 أيام")
            ),
            isVerified = true,
            lat = 33.3400,
            lng = 44.4300,
            tags = listOf("محل", "اريد محل", "اجهزة كهربائية", "شاشات", "غسالات", "شارع فلسطين"),
            imageUrl = "https://images.unsplash.com/photo-1550009158-9ebf69173e03?w=800&auto=format&fit=crop&q=80"
        )
    )

    private val _listingsState = MutableStateFlow(initialListings)
    val listings: StateFlow<List<Listing>> = _listingsState.asStateFlow()

    fun addListing(newListing: Listing) {
        _listingsState.update { current ->
            listOf(newListing) + current
        }
    }

    fun addReview(listingId: String, review: Review) {
        _listingsState.update { current ->
            current.map { listing ->
                if (listing.id == listingId) {
                    val updatedReviews = listOf(review) + listing.reviews
                    val newAvg = updatedReviews.map { it.rating }.average().toFloat()
                    listing.copy(
                        reviews = updatedReviews,
                        reviewsCount = updatedReviews.size,
                        rating = String.format("%.1f", newAvg).toFloat()
                    )
                } else {
                    listing
                }
            }
        }
    }

    fun toggleFavorite(listingId: String) {
        _listingsState.update { current ->
            current.map { listing ->
                if (listing.id == listingId) {
                    listing.copy(isFavorite = !listing.isFavorite)
                } else {
                    listing
                }
            }
        }
    }
}
