package iq.waynha.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.waynha.app.ui.theme.SkyPrimary
import iq.waynha.app.ui.theme.SkyPrimaryDark

@Composable
fun SearchBarCard(
    query: String,
    onQueryChange: (String) -> Unit,
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(SkyPrimary, SkyPrimaryDark)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = if (isArabic) "شنو تحتاج هسه؟" else "What do you need right now?",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isArabic)
                        "كهربائي، قطعة سيارة، مطعم، وظيفة أو فني قريب منك"
                    else
                        "Electrician, auto parts, restaurants, jobs or repairman near you in Iraq",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = {
                        Text(
                            text = if (isArabic)
                                "اكتب طلبك... مثلاً: أريد كهربائي قريب"
                            else
                                "Search e.g. Electrician nearby, Toyota parts...",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = SkyPrimary
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = Color.Gray
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF0D1730),
                        unfocusedContainerColor = Color(0xFF0D1730),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color(0xFFF1F5FF),
                        unfocusedTextColor = Color(0xFFF1F5FF)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
