package iq.waynha.app.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import iq.waynha.app.model.CategoryType
import iq.waynha.app.model.Governorate
import iq.waynha.app.model.Listing
import iq.waynha.app.ui.components.*
import iq.waynha.app.ui.theme.EmeraldSuccess
import iq.waynha.app.ui.theme.SkyLight
import iq.waynha.app.ui.theme.SkyPrimary
import iq.waynha.app.viewmodel.WaynhaViewModel

@Composable
fun HomeScreen(
    viewModel: WaynhaViewModel,
    onSelectListing: (Listing) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isArabic by viewModel.isArabic.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedGov by viewModel.selectedGovernorate.collectAsState()
    val selectedDistrict by viewModel.selectedDistrict.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredListings by viewModel.filteredListings.collectAsState()
    val isGpsActive by viewModel.isGpsActive.collectAsState()
    val userCoords by viewModel.userCoordinates.collectAsState()

    var showLocationSheet by remember { mutableStateOf(false) }

    // GPS Permission Launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            try {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    if (loc != null) {
                        viewModel.setGpsCoordinates(loc.latitude, loc.longitude)
                    } else {
                        // Default near Baghdad center if GPS coordinates are null in simulator
                        viewModel.setGpsCoordinates(33.3152, 44.3661)
                    }
                }
            } catch (_: SecurityException) {}
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = Color(0xFF0D1730),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Brand: «وينها؟»
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SkyPrimary,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "و",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isArabic) "وينها؟" else "Waynha?",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFF1F5FF)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SkyLight
                                ) {
                                    Text(
                                        text = if (isArabic) "العراق" else "Iraq",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SkyPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = if (isArabic) "دليل الخدمات والمحلات الذكي" else "Iraq Smart Services Directory",
                                fontSize = 10.sp,
                                color = Color(0xFFA0A8C0)
                            )
                        }
                    }

                    // Language Switcher Button (Arabic / English)
                    FilledTonalButton(
                        onClick = { viewModel.toggleLanguage() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = SkyLight,
                            contentColor = SkyPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isArabic) "English" else "العربية",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFF060B1A)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(2.dp)) }

            // Location Bar with Baghdad default & GPS trigger button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pick Governorate Button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0D1730),
                        border = ButtonDefaults.outlinedButtonBorder,
                        modifier = Modifier.clickable { showLocationSheet = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = SkyPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isArabic)
                                    "${selectedGov.nameAr} • $selectedDistrict"
                                else
                                    "${selectedGov.nameEn} • $selectedDistrict",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE5E9F5)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // GPS Button ("استخدام الموقع الحالي")
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isGpsActive) Color(0xFF0F3A2A) else Color(0xFF0D1730),
                        border = ButtonDefaults.outlinedButtonBorder,
                        modifier = Modifier.clickable {
                            if (isGpsActive) {
                                viewModel.disableGps()
                            } else {
                                val hasFine = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED
                                if (hasFine) {
                                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                                    try {
                                        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                                            if (loc != null) {
                                                viewModel.setGpsCoordinates(loc.latitude, loc.longitude)
                                            } else {
                                                viewModel.setGpsCoordinates(33.3152, 44.3661)
                                            }
                                        }
                                    } catch (_: SecurityException) {}
                                } else {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            }
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "GPS",
                                tint = if (isGpsActive) EmeraldSuccess else SkyPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isGpsActive)
                                    (if (isArabic) "GPS مفعل" else "GPS Active")
                                else
                                    (if (isArabic) "موقعي الآن" else "My GPS"),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isGpsActive) EmeraldSuccess else Color(0xFFE5E9F5)
                            )
                        }
                    }
                }
            }

            // Big Search Box: "شنو تحتاج هسه؟"
            item {
                SearchBarCard(
                    query = searchQuery,
                    onQueryChange = { viewModel.setSearchQuery(it) },
                    isArabic = isArabic
                )
            }

            // Quick Search Chips
            item {
                QuickSearchChips(
                    isArabic = isArabic,
                    onChipClick = { viewModel.setSearchQuery(it) }
                )
            }

            // Service Categories
            item {
                Column {
                    Text(
                        text = if (isArabic) "تصنيفات الخدمات والمحلات" else "Categories",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF1F5FF),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )
                    CategoryChips(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { viewModel.selectCategory(it) },
                        isArabic = isArabic
                    )
                }
            }

            // Results count bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic)
                            "النتائج المتاحة (${filteredListings.size})"
                        else
                            "Available Results (${filteredListings.size})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF1F5FF)
                    )

                    if (isGpsActive) {
                        Text(
                            text = if (isArabic) "مرتبة حسب الأقرب لك" else "Sorted by Nearest",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EmeraldSuccess
                        )
                    }
                }
            }

            // Listings Cards
            if (filteredListings.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1730)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isArabic) "لم نجد نتائج مطابقة للبحث" else "No matching results found",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFFE5E9F5)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isArabic)
                                    "جرب البحث بكلمات أخرى أو اختر محافظة بغداد لعرض جميع الفنيين"
                                else
                                    "Try different keywords or select Baghdad to view all options",
                                fontSize = 12.sp,
                                color = Color(0xFFA0A8C0)
                            )
                        }
                    }
                }
            } else {
                items(filteredListings, key = { it.id }) { listing ->
                    val distance = userCoords?.let {
                        viewModel.calculateDistanceKm(it.first, it.second, listing.lat, listing.lng)
                    }
                    ListingCard(
                        listing = listing,
                        distanceKm = distance,
                        isArabic = isArabic,
                        onClick = { onSelectListing(listing) },
                        onToggleFavorite = { viewModel.toggleFavorite(listing.id) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    if (showLocationSheet) {
        LocationPickerSheet(
            selectedGov = selectedGov,
            selectedDistrict = selectedDistrict,
            onSelectLocation = { gov, district ->
                viewModel.selectGovernorate(gov)
                viewModel.selectDistrict(district)
            },
            onDismiss = { showLocationSheet = false },
            isArabic = isArabic
        )
    }
}
