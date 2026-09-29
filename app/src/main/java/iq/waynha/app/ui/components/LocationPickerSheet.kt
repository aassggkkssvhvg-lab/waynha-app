package iq.waynha.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.waynha.app.data.IraqLocations
import iq.waynha.app.model.Governorate
import iq.waynha.app.ui.theme.SkyLight
import iq.waynha.app.ui.theme.SkyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerSheet(
    selectedGov: Governorate,
    selectedDistrict: String,
    onSelectLocation: (Governorate, String) -> Unit,
    onDismiss: () -> Unit,
    isArabic: Boolean
) {
    var tempGov by remember { mutableStateOf(selectedGov) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = Color(0xFF0D1730)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = SkyPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isArabic) "اختر المحافظة والمنطقة في العراق" else "Select Governorate & District in Iraq",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF1F5FF)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step 1: Select Governorate
            Text(
                text = if (isArabic) "1. المحافظة" else "1. Governorate",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SkyPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(180.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(IraqLocations.governorates) { gov ->
                    val isChosen = gov.id == tempGov.id
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isChosen) SkyPrimary else Color(0xFF141F3D),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { tempGov = gov }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
                        ) {
                            Text(
                                text = if (isArabic) gov.nameAr else gov.nameEn,
                                fontSize = 12.sp,
                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                color = if (isChosen) Color.White else Color(0xFFC9D1E6)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step 2: Select District
            Text(
                text = if (isArabic) "2. المنطقة في (${tempGov.nameAr})" else "2. District in (${tempGov.nameEn})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SkyPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                modifier = Modifier.height(160.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(tempGov.districts) { district ->
                    val isSelected = tempGov.id == selectedGov.id && district == selectedDistrict
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) SkyLight else Color(0xFF060B1A),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectLocation(tempGov, district)
                                onDismiss()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = district,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) SkyPrimary else Color(0xFFE5E9F5)
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = SkyPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
