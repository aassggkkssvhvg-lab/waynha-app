package iq.waynha.app.model

data class Listing(
    val id: String,
    val title: String,
    val titleEn: String = "",
    val category: CategoryType,
    val governorate: String, // e.g. "بغداد"
    val district: String,    // e.g. "الكرادة"
    val addressDetails: String,
    val description: String,
    val phone: String,       // e.g. "07701234567"
    val whatsapp: String = "",
    val priceNote: String,
    val workingHours: String,
    val rating: Float = 5.0f,
    val reviewsCount: Int = 1,
    val reviews: List<Review> = emptyList(),
    val isVerified: Boolean = true,
    val isFeatured: Boolean = false,
    val lat: Double = 33.3152,
    val lng: Double = 44.3661,
    val tags: List<String> = emptyList(),
    val imageUrl: String = "",
    val isFavorite: Boolean = false
)
