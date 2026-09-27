package iq.waynha.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iq.waynha.app.data.IraqLocations
import iq.waynha.app.data.ListingRepository
import iq.waynha.app.model.CategoryType
import iq.waynha.app.model.Governorate
import iq.waynha.app.model.Listing
import iq.waynha.app.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class WaynhaViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGovernorate =
        MutableStateFlow(IraqLocations.getDefaultGovernorate())
    val selectedGovernorate: StateFlow<Governorate> =
        _selectedGovernorate.asStateFlow()

    private val _selectedDistrict = MutableStateFlow("الكل")
    val selectedDistrict: StateFlow<String> =
        _selectedDistrict.asStateFlow()

    private val _selectedCategory =
        MutableStateFlow(CategoryType.ALL)
    val selectedCategory: StateFlow<CategoryType> =
        _selectedCategory.asStateFlow()

    private val _userCoordinates =
        MutableStateFlow<Pair<Double, Double>?>(null)
    val userCoordinates: StateFlow<Pair<Double, Double>?> =
        _userCoordinates.asStateFlow()

    private val _isGpsActive = MutableStateFlow(false)
    val isGpsActive: StateFlow<Boolean> =
        _isGpsActive.asStateFlow()

    private val _isArabic = MutableStateFlow(true)
    val isArabic: StateFlow<Boolean> =
        _isArabic.asStateFlow()

    private val _selectedListing =
        MutableStateFlow<Listing?>(null)
    val selectedListing: StateFlow<Listing?> =
        _selectedListing.asStateFlow()

    val allListings = ListingRepository.listings

    /**
     * بيانات الفلترة
     */
    private data class FilterData(
        val listings: List<Listing>,
        val query: String,
        val governorate: Governorate,
        val district: String,
        val category: CategoryType
    )

    /**
     * القوائم بعد البحث والفلترة
     */
    val filteredListings: StateFlow<List<Listing>> =
        combine(
            allListings,
            _searchQuery,
            _selectedGovernorate,
            _selectedDistrict,
            _selectedCategory
        ) { listings, query, governorate, district, category ->

            FilterData(
                listings = listings,
                query = query,
                governorate = governorate,
                district = district,
                category = category
            )

        }.combine(_userCoordinates) { data, coordinates ->

            val q = data.query.trim().lowercase()

            val filtered = data.listings.filter { item ->

                // المحافظة
                val matchesGovernorate =
                    item.governorate == data.governorate.nameAr

                // القضاء / المنطقة
                val matchesDistrict =
                    data.district == "الكل" ||
                            item.district == data.district

                // التصنيف
                val matchesCategory =
                    data.category == CategoryType.ALL ||
                            item.category == data.category

                // البحث
                val matchesQuery = if (q.isBlank()) {

                    true

                } else {

                    val cleanQ = q
                        .replace(
                            Regex("^(اريد|أريد|محتاج|ابحث عن)\\s+"),
                            ""
                        )
                        .replace(
                            Regex("\\s+(قريب|قريبة|هسه)$"),
                            ""
                        )
                        .trim()

                    val title =
                        item.title.lowercase()

                    val description =
                        item.description.lowercase()

                    val districtText =
                        item.district.lowercase()

                    val titleMatch =
                        title.contains(q) ||
                                (
                                        cleanQ.isNotEmpty() &&
                                                title.contains(cleanQ)
                                        )

                    val descriptionMatch =
                        description.contains(q) ||
                                (
                                        cleanQ.isNotEmpty() &&
                                                description.contains(cleanQ)
                                        )

                    val tagMatch =
                        item.tags.any { tag ->
                            tag.lowercase().contains(q) ||
                                    (
                                            cleanQ.isNotEmpty() &&
                                                    tag.lowercase()
                                                        .contains(cleanQ)
                                            )
                        }

                    val districtMatch =
                        districtText.contains(q)

                    // البحث باللهجة العراقية
                    val intentElectrician =
                        (
                                q.contains("كهربائي") ||
                                        q.contains("تأسيس")
                                ) &&
                                item.category == CategoryType.ELECTRICIANS

                    val intentPlumber =
                        (
                                q.contains("سباك") ||
                                        q.contains("صحيات") ||
                                        q.contains("بوري")
                                ) &&
                                item.category == CategoryType.PLUMBERS

                    val intentMechanic =
                        (
                                q.contains("فيتر") ||
                                        q.contains("ميكانيكي") ||
                                        q.contains("تصليح سيارات")
                                ) &&
                                item.category == CategoryType.MECHANICS

                    val intentParts =
                        (
                                q.contains("قطعة") ||
                                        q.contains("ادوات") ||
                                        q.contains("أدوات") ||
                                        q.contains("تويوتا") ||
                                        q.contains("سيار")
                                ) &&
                                item.category == CategoryType.AUTO_PARTS

                    val intentRestaurant =
                        (
                                q.contains("مطعم") ||
                                        q.contains("كباب") ||
                                        q.contains("اكل") ||
                                        q.contains("أكل")
                                ) &&
                                item.category == CategoryType.RESTAURANTS

                    val intentAc =
                        (
                                q.contains("سبلت") ||
                                        q.contains("تبريد") ||
                                        q.contains("تكييف")
                                ) &&
                                (
                                        item.category == CategoryType.SERVICES ||
                                                item.category == CategoryType.ELECTRICIANS
                                        )

                    val intentJob =
                        (
                                q.contains("عمل") ||
                                        q.contains("وظيفة") ||
                                        q.contains("شغل") ||
                                        q.contains("كاشير")
                                ) &&
                                item.category == CategoryType.JOBS

                    titleMatch ||
                            descriptionMatch ||
                            tagMatch ||
                            districtMatch ||
                            intentElectrician ||
                            intentPlumber ||
                            intentMechanic ||
                            intentParts ||
                            intentRestaurant ||
                            intentAc ||
                            intentJob
                }

                matchesGovernorate &&
                        matchesDistrict &&
                        matchesCategory &&
                        matchesQuery
            }

            // ترتيب النتائج حسب أقرب مكان للمستخدم
            if (coordinates != null) {

                filtered.sortedBy { item ->

                    calculateDistanceKm(
                        coordinates.first,
                        coordinates.second,
                        item.lat,
                        item.lng
                    )
                }

            } else {

                filtered
            }

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    /**
     * تغيير البحث
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * اختيار المحافظة
     */
    fun selectGovernorate(governorate: Governorate) {
        _selectedGovernorate.value = governorate
        _selectedDistrict.value = "الكل"
    }

    /**
     * اختيار المنطقة
     */
    fun selectDistrict(district: String) {
        _selectedDistrict.value = district
    }

    /**
     * اختيار التصنيف
     */
    fun selectCategory(category: CategoryType) {
        _selectedCategory.value = category
    }

    /**
     * تبديل اللغة
     */
    fun toggleLanguage() {
        _isArabic.value = !_isArabic.value
    }

    /**
     * تشغيل GPS
     */
    fun setGpsCoordinates(
        lat: Double,
        lng: Double
    ) {
        _userCoordinates.value = Pair(lat, lng)
        _isGpsActive.value = true
    }

    /**
     * إيقاف GPS
     */
    fun disableGps() {
        _isGpsActive.value = false
        _userCoordinates.value = null
    }

    /**
     * اختيار إعلان
     */
    fun selectListing(listing: Listing?) {
        _selectedListing.value = listing
    }

    /**
     * إضافة / إزالة المفضلة
     */
    fun toggleFavorite(listingId: String) {

        ListingRepository.toggleFavorite(listingId)

        if (_selectedListing.value?.id == listingId) {

            val current =
                _selectedListing.value

            if (current != null) {

                _selectedListing.value =
                    current.copy(
                        isFavorite = !current.isFavorite
                    )
            }
        }
    }

    /**
     * إضافة إعلان
     */
    fun addListing(newListing: Listing) {

        ListingRepository.addListing(newListing)

        _selectedListing.value = newListing
    }

    /**
     * إضافة تقييم
     */
    fun addReview(
        listingId: String,
        rating: Int,
        comment: String,
        userName: String
    ) {

        val safeRating =
            rating.coerceIn(1, 5)

        val review = Review(
            id = "rev-${System.currentTimeMillis()}",
            userName =
                userName.ifBlank {
                    if (_isArabic.value) {
                        "مستخدم وينها"
                    } else {
                        "Waynha User"
                    }
                },
            rating = safeRating,
            comment = comment,
            date =
                if (_isArabic.value) {
                    "الآن"
                } else {
                    "Just now"
                }
        )

        ListingRepository.addReview(
            listingId,
            review
        )

        if (_selectedListing.value?.id == listingId) {

            val current =
                _selectedListing.value ?: return

            val updatedReviews =
                listOf(review) + current.reviews

            val newAverage =
                updatedReviews
                    .map { it.rating }
                    .average()
                    .toFloat()

            _selectedListing.value =
                current.copy(
                    reviews = updatedReviews,
                    reviewsCount = updatedReviews.size,
                    rating =
                        String.format(
                            "%.1f",
                            newAverage
                        ).toFloat()
                )
        }
    }

    /**
     * حساب المسافة بين نقطتين بالكيلومتر
     * Haversine Formula
     */
    fun calculateDistanceKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {

        val earthRadiusKm = 6371.0

        val dLat =
            Math.toRadians(lat2 - lat1)

        val dLon =
            Math.toRadians(lon2 - lon1)

        val a =
            sin(dLat / 2).pow(2) +
                    cos(Math.toRadians(lat1)) *
                    cos(Math.toRadians(lat2)) *
                    sin(dLon / 2).pow(2)

        val c =
            2 * atan2(
                sqrt(a),
                sqrt(1 - a)
            )

        return earthRadiusKm * c
    }
}
