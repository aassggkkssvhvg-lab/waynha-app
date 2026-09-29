package iq.waynha.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iq.waynha.app.data.IraqLocations
import iq.waynha.app.data.ListingRepository
import iq.waynha.app.model.CategoryType
import iq.waynha.app.model.Governorate
import iq.waynha.app.model.Listing
import iq.waynha.app.model.Review
import kotlinx.coroutines.flow.*
import kotlin.math.*

class WaynhaViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Default location: Baghdad, Iraq
    private val _selectedGovernorate = MutableStateFlow(IraqLocations.getDefaultGovernorate())
    val selectedGovernorate: StateFlow<Governorate> = _selectedGovernorate.asStateFlow()

    private val _selectedDistrict = MutableStateFlow("الكل")
    val selectedDistrict: StateFlow<String> = _selectedDistrict.asStateFlow()

    private val _selectedCategory = MutableStateFlow(CategoryType.ALL)
    val selectedCategory: StateFlow<CategoryType> = _selectedCategory.asStateFlow()

    private val _userCoordinates = MutableStateFlow<Pair<Double, Double>?>(null)
    val userCoordinates: StateFlow<Pair<Double, Double>?> = _userCoordinates.asStateFlow()

    private val _isGpsActive = MutableStateFlow(false)
    val isGpsActive: StateFlow<Boolean> = _isGpsActive.asStateFlow()

    // Language state: true for Arabic (default), false for English
    private val _isArabic = MutableStateFlow(true)
    val isArabic: StateFlow<Boolean> = _isArabic.asStateFlow()

    private val _selectedListing = MutableStateFlow<Listing?>(null)
    val selectedListing: StateFlow<Listing?> = _selectedListing.asStateFlow()

    val allListings = ListingRepository.listings

    private data class FilterInputs(
        val listings: List<Listing>,
        val query: String,
        val gov: Governorate,
        val district: String,
        val category: CategoryType
    )

    val filteredListings: StateFlow<List<Listing>> = combine(
        allListings,
        _searchQuery,
        _selectedGovernorate,
        _selectedDistrict,
        _selectedCategory
    ) { listings, query, gov, district, category ->
        FilterInputs(listings, query, gov, district, category)
    }.combine(_userCoordinates) { inputs, coords ->
        val (listings, query, gov, district, category) = inputs
        val q = query.trim().lowercase()
        val isBaghdad = gov.id == "baghdad"

        val filtered = listings.filter { item ->
            // 1. Governorate match: if user selected Baghdad (default), show all unless specific governorate chosen
            val matchesGov = if (isBaghdad) true else item.governorate == gov.nameAr

            // 2. District match
            val matchesDistrict = district == "الكل" || item.district == district

            // 3. Category match
            val matchesCategory = category == CategoryType.ALL || item.category == category

            // 4. Query & Iraqi Dialect Intent matching
            val matchesQuery = if (q.isBlank()) {
                true
            } else {
                val cleanQ = q.replace(Regex("^(اريد|أريد|محتاج|ابحث عن)\\s+"), "")
                    .replace(Regex("\\s+(قريب|قريبة|هسه)$"), "")
                    .trim()

                val titleMatch = item.title.lowercase().contains(q) || (cleanQ.isNotEmpty() && item.title.lowercase().contains(cleanQ))
                val descMatch = item.description.lowercase().contains(q) || (cleanQ.isNotEmpty() && item.description.lowercase().contains(cleanQ))
                val tagMatch = item.tags.any { it.lowercase().contains(q) || (cleanQ.isNotEmpty() && it.lowercase().contains(cleanQ)) }
                val districtMatch = item.district.lowercase().contains(q)

                // Common Iraqi intents
                val intentElectrician = (q.contains("كهربائي") || q.contains("تأسيس")) && item.category == CategoryType.ELECTRICIANS
                val intentPlumber = (q.contains("سباك") || q.contains("صحيات") || q.contains("بوري")) && item.category == CategoryType.PLUMBERS
                val intentMechanic = (q.contains("فيتر") || q.contains("ميكانيكي") || q.contains("تصليح سيارات")) && item.category == CategoryType.MECHANICS
                val intentParts = (q.contains("قطعة") || q.contains("ادوات") || q.contains("تويوتا") || q.contains("سيار")) && item.category == CategoryType.AUTO_PARTS
                val intentRestaurant = (q.contains("مطعم") || q.contains("كباب") || q.contains("اكل")) && item.category == CategoryType.RESTAURANTS
                val intentAc = (q.contains("سبلت") || q.contains("تبريد")) && (item.category == CategoryType.SERVICES || item.category == CategoryType.ELECTRICIANS)
                val intentJob = (q.contains("عمل") || q.contains("وظيفة") || q.contains("كاشير")) && item.category == CategoryType.JOBS

                titleMatch || descMatch || tagMatch || districtMatch ||
                        intentElectrician || intentPlumber || intentMechanic || intentParts || intentRestaurant || intentAc || intentJob
            }

            matchesGov && matchesDistrict && matchesCategory && matchesQuery
        }

        // If GPS is active and we have coordinates, sort by distance to user
        if (coords != null) {
            filtered.sortedBy { calculateDistanceKm(coords.first, coords.second, it.lat, it.lng) }
        } else {
            filtered
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectGovernorate(governorate: Governorate) {
        _selectedGovernorate.value = governorate
        _selectedDistrict.value = "الكل"
    }

    fun selectDistrict(district: String) {
        _selectedDistrict.value = district
    }

    fun selectCategory(category: CategoryType) {
        _selectedCategory.value = category
    }

    fun toggleLanguage() {
        _isArabic.value = !_isArabic.value
    }

    fun setGpsCoordinates(lat: Double, lng: Double) {
        _userCoordinates.value = Pair(lat, lng)
        _isGpsActive.value = true
    }

    fun disableGps() {
        _isGpsActive.value = false
        _userCoordinates.value = null
    }

    fun selectListing(listing: Listing?) {
        _selectedListing.value = listing
    }

    fun toggleFavorite(listingId: String) {
        ListingRepository.toggleFavorite(listingId)
        if (_selectedListing.value?.id == listingId) {
            _selectedListing.value = _selectedListing.value?.copy(
                isFavorite = !(_selectedListing.value?.isFavorite ?: false)
            )
        }
    }

    fun addListing(newListing: Listing) {
        ListingRepository.addListing(newListing)
        _selectedListing.value = newListing
    }

    fun addReview(listingId: String, rating: Int, comment: String, userName: String) {
        val review = Review(
            id = "rev-${System.currentTimeMillis()}",
            userName = userName.ifBlank { if (_isArabic.value) "مستخدم وينها" else "Waynha User" },
            rating = rating,
            comment = comment,
            date = if (_isArabic.value) "الآن" else "Just now"
        )
        ListingRepository.addReview(listingId, review)
        if (_selectedListing.value?.id == listingId) {
            val current = _selectedListing.value!!
            val updated = listOf(review) + current.reviews
            val newAvg = updated.map { it.rating }.average().toFloat()
            _selectedListing.value = current.copy(
                reviews = updated,
                reviewsCount = updated.size,
                rating = String.format("%.1f", newAvg).toFloat()
            )
        }
    }

    // Haversine distance in KM
    fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Radius of earth in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}
