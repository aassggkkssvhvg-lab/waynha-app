package iq.waynha.app.model

enum class CategoryType(val id: String, val nameAr: String, val nameEn: String) {
    ALL("all", "الكل", "All"),
    ELECTRICIANS("technicians", "كهربائي", "Electrician"),
    PLUMBERS("plumbers", "سباك وصحيات", "Plumber"),
    MECHANICS("mechanics", "ميكانيكي وفيتر", "Mechanic"),
    AUTO_PARTS("auto_parts", "قطع سيارات", "Auto Parts"),
    RESTAURANTS("restaurants", "مطاعم", "Restaurants"),
    JOBS("jobs", "وظائف وفرص عمل", "Jobs"),
    SHOPS("shops", "محلات وأسواق", "Shops"),
    SERVICES("services", "خدمات أخرى", "Services"),
    COMPANIES("companies", "شركات", "Companies"),
    HOSPITALS("hospitals", "مستشفيات وعيادات", "Hospitals & Clinics"),
    PHARMACY("pharmacy", "صيدليات", "Pharmacies"),
    SCHOOLS("schools", "مدارس وجامعات", "Schools"),
    TEACHERS("teachers", "معلمين ومدرسين", "Teachers"),
    OFFERS("offers", "عروض وخصومات", "Offers"),
    FUEL("fuel", "محطات وقود", "Fuel Stations"),
    PUBLIC_PLACES("public", "أماكن عامة", "Public Places")
}
