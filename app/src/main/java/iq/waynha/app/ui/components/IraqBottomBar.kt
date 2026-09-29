package iq.waynha.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Panel = Color(0xFF0D1730)
private val Gold = Color(0xFFFFC21A)
private val Navy = Color(0xFF060B1A)

/** التبويبات: 0 بحث، 1 خريطة، 2 رئيسية، 3 وظائف، 4 حسابي (تظهر من اليمين لليسار بالعربي) */
@Composable
fun IraqBottomBar(selected: Int, isArabic: Boolean, onSelect: (Int) -> Unit) {
    val items = listOf(
        Triple(if (isArabic) "البحث" else "Search", Icons.Filled.Search, 0),
        Triple(if (isArabic) "الخريطة" else "Map", Icons.Filled.Map, 1),
        Triple(if (isArabic) "الرئيسية" else "Home", Icons.Filled.Home, 2),
        Triple(if (isArabic) "الوظائف" else "Jobs", Icons.Filled.Work, 3),
        Triple(if (isArabic) "حسابي" else "Account", Icons.Filled.Person, 4),
    )
    Row(
        Modifier.fillMaxWidth().background(Panel).navigationBarsPadding().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { (label, icon, idx) ->
            val home = idx == 2
            val on = selected == idx
            Column(Modifier.clickable { onSelect(idx) }.padding(horizontal = 6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                if (home) {
                    Box(Modifier.size(54.dp).clip(CircleShape).background(Gold), contentAlignment = Alignment.Center) {
                        Icon(icon, null, tint = Navy, modifier = Modifier.size(28.dp))
                    }
                } else Icon(icon, null, tint = if (on) Gold else Color.White, modifier = Modifier.size(26.dp))
                Text(label, color = if (on || home) Gold else Color.White, fontSize = 11.sp)
            }
        }
    }
}
