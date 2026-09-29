package iq.waynha.app.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import iq.waynha.app.model.CategoryType
import iq.waynha.app.model.Listing
import iq.waynha.app.viewmodel.WaynhaViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.math.roundToInt

private val Navy = Color(0xFF060B1A)
private val Panel = Color(0xFF0D1730)
private val Gold = Color(0xFFFFC21A)

@Composable
fun MapScreen(viewModel: WaynhaViewModel, onOpenDetails: (Listing) -> Unit) {
    val ctx = LocalContext.current
    val isArabic by viewModel.isArabic.collectAsState()
    val listings by viewModel.allListings.collectAsState()
    val coords by viewModel.userCoordinates.collectAsState()
    var category by remember { mutableStateOf<CategoryType?>(null) }
    var selected by remember { mutableStateOf<Listing?>(null) }
    var mapRef by remember { mutableStateOf<MapView?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    fun t(ar: String, en: String) = if (isArabic) ar else en

    val cats = remember(listings) { listings.map { it.category }.distinct() }
    val visible = remember(listings, category, coords) {
        val l = listings.filter { category == null || it.category == category }
        coords?.let { c -> l.sortedBy { viewModel.calculateDistanceKm(c.first, c.second, it.lat, it.lng) } } ?: l
    }
    fun distText(p: Listing): String? = coords?.let {
        val km = viewModel.calculateDistanceKm(it.first, it.second, p.lat, p.lng)
        if (km < 1) "${(km * 1000).roundToInt()} " + t("م", "m") else "%.1f ".format(km) + t("كم", "km")
    }

    fun goNearest() {
        val n = visible.firstOrNull() ?: return
        selected = n
        mapRef?.controller?.setZoom(15.0)
        mapRef?.controller?.animateTo(GeoPoint(n.lat, n.lng))
    }
    fun locate() {
        val loc = lastLocation(ctx)
        if (loc == null) error = t("فعّل الموقع (GPS) من الإعدادات", "Turn on location in settings")
        else { error = null; viewModel.setGpsCoordinates(loc.latitude, loc.longitude) }
    }
    var wantNearest by remember { mutableStateOf(false) }
    // بعد ما تتحدث الإحداثيات نروح لأقرب مكان
    LaunchedEffect(coords, wantNearest) { if (wantNearest && coords != null) { wantNearest = false; goNearest() } }

    // نطلب الموقع التقريبي فقط، وعند الضغط على الزر فقط
    val permLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ok ->
        if (ok) { wantNearest = true; locate() }
        else error = t("لا يمكن حساب المسافة بدون إذن الموقع", "Location permission is needed for distances")
    }
    fun requestNearest() {
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            wantNearest = true; locate()
        } else permLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    Box(Modifier.fillMaxSize().background(Navy)) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { c ->
                Configuration.getInstance().userAgentValue = c.packageName
                MapView(c).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(6.0)
                    controller.setCenter(GeoPoint(33.2, 43.7))
                    mapRef = this
                }
            },
            update = { map ->
                map.overlays.clear()
                visible.forEach { p ->
                    map.overlays.add(Marker(map).apply {
                        position = GeoPoint(p.lat, p.lng); title = p.title
                        setOnMarkerClickListener { _, _ -> selected = p; true }
                    })
                }
                map.invalidate()
            },
            onRelease = { it.onDetach() }
        )

        Column(Modifier.align(Alignment.TopCenter).statusBarsPadding().padding(12.dp)) {
            Text(t("خريطة العراق", "Iraq Map"), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Panel).padding(horizontal = 14.dp, vertical = 6.dp))
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { MapChip(t("الكل", "All"), category == null) { category = null } }
                items(cats) { c -> MapChip(if (isArabic) c.nameAr else c.nameEn, category == c) { category = c } }
            }
            error?.let { Text(it, color = Gold, fontSize = 12.sp, modifier = Modifier.padding(top = 6.dp)) }
        }

        ExtendedFloatingActionButton(
            onClick = { requestNearest() },
            containerColor = Gold, contentColor = Navy,
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
                .padding(bottom = if (selected != null) 200.dp else 0.dp)
        ) {
            Icon(Icons.Filled.MyLocation, null); Spacer(Modifier.width(8.dp))
            Text(t("الأقرب إليّ", "Nearest to me"), fontWeight = FontWeight.Bold)
        }

        selected?.let { p ->
            Column(
                Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)).background(Panel).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f).clickable { onOpenDetails(p) }) {
                        Text(if (isArabic || p.titleEn.isBlank()) p.title else p.titleEn, color = Color.White,
                            fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, maxLines = 2)
                        Text(listOf(if (isArabic) p.category.nameAr else p.category.nameEn, p.governorate, p.district)
                            .filter { it.isNotBlank() }.joinToString(" • "), color = Gold, fontSize = 13.sp)
                    }
                    distText(p)?.let { Text(it, color = Color.White, fontWeight = FontWeight.Bold) }
                    IconButton(onClick = { selected = null }) { Icon(Icons.Filled.Close, null, tint = Color.White) }
                }
                if (p.addressDetails.isNotBlank()) Text(p.addressDetails, color = Color(0xFFB0B8D0), fontSize = 13.sp, maxLines = 2)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MapAction(t("اتصال", "Call"), Icons.Filled.Call, Color(0xFF2E9E3F), Modifier.weight(1f)) {
                        if (p.phone.isNotBlank()) ctx.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${p.phone}")))
                    }
                    MapAction(t("الاتجاهات", "Directions"), Icons.Filled.Directions, Color(0xFF1E6FE0), Modifier.weight(1f)) {
                        val nav = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${p.lat},${p.lng}"))
                        runCatching { ctx.startActivity(nav) }.onFailure {
                            runCatching { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:${p.lat},${p.lng}?q=${p.lat},${p.lng}"))) }
                        }
                    }
                    MapAction(t("مشاركة", "Share"), Icons.Filled.Share, Color(0xFF7C3AED), Modifier.weight(1f)) {
                        val text = "${p.title}\n${p.addressDetails}\n${p.phone}\nhttps://maps.google.com/?q=${p.lat},${p.lng}"
                        ctx.startActivity(Intent.createChooser(
                            Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, text), null))
                    }
                }
            }
        }
    }
}

@Composable
private fun MapChip(text: String, on: Boolean, click: () -> Unit) {
    Box(Modifier.clip(RoundedCornerShape(20.dp)).background(if (on) Gold else Panel)
        .clickable(onClick = click).padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(text, color = if (on) Navy else Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
private fun MapAction(label: String, icon: ImageVector, c: Color, m: Modifier, click: () -> Unit) {
    Row(m.height(50.dp).clip(RoundedCornerShape(14.dp)).background(c).clickable(onClick = click),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp)); Spacer(Modifier.width(6.dp))
        Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@SuppressLint("MissingPermission")
private fun lastLocation(c: Context): Location? {
    val lm = c.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return listOf(LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER, LocationManager.PASSIVE_PROVIDER)
        .mapNotNull { runCatching { lm.getLastKnownLocation(it) }.getOrNull() }
        .maxByOrNull { it.time }
}
