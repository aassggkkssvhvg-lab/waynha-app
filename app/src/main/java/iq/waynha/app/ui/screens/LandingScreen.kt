package iq.waynha.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.waynha.app.model.CategoryType

private val Navy = Color(0xFF060B1A)
private val Panel = Color(0xFF0D1730)
private val Gold = Color(0xFFFFC21A)
private val Purple = Color(0xFF7C4DFF)
private val Blue = Color(0xFF1E88E5)

private data class Tile(val cat: CategoryType, val ar: String, val en: String, val icon: ImageVector, val c1: Color, val c2: Color)

// الترتيب من اليمين إلى اليسار في الواجهة العربية
private val tiles = listOf(
    Tile(CategoryType.COMPANIES, "الشركات", "Companies", Icons.Filled.Business, Color(0xFFE53935), Color(0xFF8E1B1B)),
    Tile(CategoryType.SERVICES, "الخدمات", "Services", Icons.Filled.Engineering, Color(0xFF2E9E3F), Color(0xFF14602A)),
    Tile(CategoryType.SHOPS, "المحلات", "Shops", Icons.Filled.Storefront, Color(0xFF1E6FE0), Color(0xFF0E3F91)),
    Tile(CategoryType.JOBS, "الوظائف", "Jobs", Icons.Filled.Work, Color(0xFFFFA000), Color(0xFFC25E00)),
    Tile(CategoryType.OFFERS, "العروض", "Offers", Icons.Filled.LocalOffer, Color(0xFFFF8F1F), Color(0xFFC0490B)),
    Tile(CategoryType.TEACHERS, "المعلمين", "Teachers", Icons.Filled.Person, Color(0xFF12A6A6), Color(0xFF0A6670)),
    Tile(CategoryType.HOSPITALS, "المستشفيات", "Hospitals", Icons.Filled.LocalHospital, Color(0xFFE91E8C), Color(0xFF8A1160)),
    Tile(CategoryType.SCHOOLS, "المدارس", "Schools", Icons.Filled.School, Color(0xFF7C3AED), Color(0xFF4520A0)),
)

@Composable
fun LandingScreen(
    isArabic: Boolean,
    onSearch: () -> Unit,
    onNearMe: () -> Unit,
    onAiSearch: () -> Unit,
    onCategory: (CategoryType) -> Unit,
    onMap: () -> Unit,
    onToggleLanguage: () -> Unit,
) {
    fun t(ar: String, en: String) = if (isArabic) ar else en
    LazyColumn(
        Modifier.fillMaxSize().background(Navy),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Box(
                Modifier.fillMaxWidth().height(290.dp)
                    .background(Brush.verticalGradient(listOf(Color(0xFF2A1A3A), Navy)))
            ) {
                // TODO: صورة الأفق: Image(painterResource(R.drawable.header_bg), ...)
                Row(
                    Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp).align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.AccountCircle, null, tint = Color.White, modifier = Modifier.size(40.dp))
                    Box(
                        Modifier.clip(RoundedCornerShape(12.dp)).background(Panel)
                            .clickable(onClick = onToggleLanguage).padding(horizontal = 12.dp, vertical = 6.dp)
                    ) { Text(t("English", "العربية"), color = Gold, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                    Icon(Icons.Filled.Notifications, null, tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Column(
                    Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // TODO: شعار خريطة العراق: Image(painterResource(R.drawable.iraq_logo), ...)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(t("خدمات", "Iraq"), color = Gold, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold)
                        Text(t("العراق", "Mix"), color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Text(t("كل ما تحتاجه .. في مكان واحد", "Everything you need .. in one place"),
                        color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
        item {
            Row(
                Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(58.dp)
                    .clip(RoundedCornerShape(30.dp)).background(Panel)
                    .border(2.dp, Gold, RoundedCornerShape(30.dp))
                    .clickable(onClick = onSearch).padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Search, null, tint = Color.White)
                Spacer(Modifier.width(12.dp))
                Text(t("ابحث عن خدمة، وظيفة، محل، أو أي شيء ...", "Search a service, job, shop..."),
                    color = Color(0xFFA0A8C0), fontSize = 14.sp)
            }
        }
        item {
            Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickCard(t("البحث الذكي", "Smart search"), t("بمساعدة الذكاء الاصطناعي", "AI assisted"),
                    Icons.Filled.AutoAwesome, Purple, Modifier.weight(1f), onAiSearch)
                QuickCard(t("قريب مني", "Near me"), t("اعثر على الأقرب لك", "Find the closest"),
                    Icons.Filled.LocationOn, Blue, Modifier.weight(1f), onNearMe)
            }
        }
        item {
            Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                tiles.chunked(4).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        row.forEach { tile ->
                            Column(
                                Modifier.weight(1f).height(96.dp).clip(RoundedCornerShape(18.dp))
                                    .background(Brush.verticalGradient(listOf(tile.c1, tile.c2)))
                                    .clickable { onCategory(tile.cat) },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(tile.icon, null, tint = Color.White, modifier = Modifier.size(36.dp))
                                Spacer(Modifier.height(6.dp))
                                Text(t(tile.ar, tile.en), color = Color.White, fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp, textAlign = TextAlign.Center, maxLines = 1)
                            }
                        }
                    }
                }
            }
        }
        item {
            Box(
                Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(110.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFF0A2A5E), Color(0xFF0B1F44))))
                    .border(1.dp, Blue, RoundedCornerShape(20.dp)).clickable(onClick = onMap)
            ) {
                Column(Modifier.align(Alignment.CenterStart).padding(16.dp)) {
                    Text(t("خريطة العراق", "Iraq Map"), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text(t("اكتشف جميع المحافظات", "Explore all governorates"), color = Gold, fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    Box(Modifier.clip(RoundedCornerShape(14.dp)).background(Gold).padding(horizontal = 14.dp, vertical = 5.dp)) {
                        Text(t("استكشف الآن", "Explore now"), color = Navy, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
        item {
            val items = listOf(
                Icons.Filled.Security to t("أمان وموثوقية", "Safe & trusted"),
                Icons.Filled.SupportAgent to t("دعم 24 ساعة", "24h support"),
                Icons.Filled.Star to t("تقييمات حقيقية", "Real reviews"),
                Icons.Filled.Favorite to t("خدمات متنوعة", "Diverse services"),
            )
            Row(
                Modifier.padding(horizontal = 16.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp))
                    .background(Panel).padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items.forEach { (ic, tx) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(ic, null, tint = Gold, modifier = Modifier.size(22.dp))
                        Text(tx, color = Color.White, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickCard(title: String, sub: String, icon: ImageVector, accent: Color, m: Modifier, onClick: () -> Unit) {
    Row(
        m.height(76.dp).clip(RoundedCornerShape(18.dp)).background(Panel)
            .border(1.5.dp, accent, RoundedCornerShape(18.dp)).clickable(onClick = onClick).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(44.dp).clip(CircleShape).background(accent), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Color.White)
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(sub, color = Color(0xFFB0B8D0), fontSize = 10.sp)
        }
    }
}
