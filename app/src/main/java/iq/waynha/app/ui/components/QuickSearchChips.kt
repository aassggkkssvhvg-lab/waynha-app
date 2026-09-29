package iq.waynha.app.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.waynha.app.ui.theme.SkyLight
import iq.waynha.app.ui.theme.SkyPrimary

@Composable
fun QuickSearchChips(
    isArabic: Boolean,
    onChipClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val prompts = if (isArabic) {
        listOf(
            "أريد كهربائي قريب",
            "أريد مصلح سبالت",
            "أريد قطعة سيارة",
            "أريد فيتر سيارات",
            "أريد سباك صحيات",
            "أريد كباب بغدادي",
            "أريد فرصة عمل"
        )
    } else {
        listOf(
            "Need electrician nearby",
            "Need AC repair",
            "Need auto parts",
            "Need car mechanic",
            "Need plumber",
            "Need Baghdad kebab",
            "Need job opening"
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = if (isArabic) "عمليات بحث شائعة في العراق" else "Popular Searches in Iraq",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFC9D1E6),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            prompts.forEach { prompt ->
                SuggestionChip(
                    onClick = { onChipClick(prompt) },
                    label = {
                        Text(
                            text = prompt,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = Color(0xFF0D1730),
                        labelColor = Color(0xFFE5E9F5)
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        borderColor = Color(0xFF3A4670),
                        borderWidth = 1.dp,
                        enabled = true
                    )
                )
            }
        }
    }
}
