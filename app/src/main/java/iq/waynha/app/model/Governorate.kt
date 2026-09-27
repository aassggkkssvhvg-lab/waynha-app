package iq.waynha.app.model

data class Governorate(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val centerLat: Double,
    val centerLng: Double,
    val districts: List<String>
)
