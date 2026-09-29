package iq.waynha.app.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.waynha.app.model.CategoryType
import iq.waynha.app.ui.theme.SkyLight
import iq.waynha.app.ui.theme.SkyPrimary

@Composable
fun CategoryChips(
    selectedCategory: CategoryType,
    onCategorySelected: (CategoryType) -> Unit,
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CategoryType.values().forEach { cat ->
            val isSelected = cat == selectedCategory
            val icon = getCategoryIcon(cat)
            val name = if (isArabic) cat.nameAr else cat.nameEn

            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(cat) },
                label = {
                    Text(
                        text = name,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = name,
                        modifier = Modifier.size(16.dp)
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SkyPrimary,
                    selectedLabelColor = Color.White,
                    selectedLeadingIconColor = Color.White,
                    containerColor = Color(0xFF0D1730),
                    labelColor = Color(0xFFC9D1E6),
                    iconColor = SkyPrimary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = if (isSelected) SkyPrimary else Color(0xFF24305A),
                    selectedBorderColor = SkyPrimary,
                    borderWidth = 1.dp,
                    enabled = true,
                    selected = isSelected
                )
            )
        }
    }
}

private fun getCategoryIcon(category: CategoryType): ImageVector {
    return when (category) {
        CategoryType.ALL -> Icons.Default.Category
        CategoryType.ELECTRICIANS -> Icons.Default.ElectricalServices
        CategoryType.PLUMBERS -> Icons.Default.Plumbing
        CategoryType.MECHANICS -> Icons.Default.Build
        CategoryType.AUTO_PARTS -> Icons.Default.DirectionsCar
        CategoryType.RESTAURANTS -> Icons.Default.Restaurant
        CategoryType.JOBS -> Icons.Default.Work
        CategoryType.SHOPS -> Icons.Default.Storefront
        CategoryType.SERVICES -> Icons.Default.Handyman
        CategoryType.COMPANIES -> Icons.Default.Business
        CategoryType.HOSPITALS -> Icons.Default.LocalHospital
        CategoryType.PHARMACY -> Icons.Default.Medication
        CategoryType.SCHOOLS -> Icons.Default.School
        CategoryType.TEACHERS -> Icons.Default.Person
        CategoryType.OFFERS -> Icons.Default.LocalOffer
        CategoryType.FUEL -> Icons.Default.LocalGasStation
        CategoryType.PUBLIC_PLACES -> Icons.Default.Park
    }
}
