package iq.waynha.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.waynha.app.model.Listing
import iq.waynha.app.ui.theme.EmeraldSuccess
import iq.waynha.app.ui.theme.SkyLight
import iq.waynha.app.ui.theme.SkyPrimary
import iq.waynha.app.viewmodel.WaynhaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: WaynhaViewModel,
    onSelectListing: (Listing) -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic by viewModel.isArabic.collectAsState()
    val allListings by viewModel.allListings.collectAsState()
    val favorites = allListings.filter { it.isFavorite }

    var userName by remember { mutableStateOf("مستخدم وينها") }
    var userPhone by remember { mutableStateOf("07701234567") }
    var isEditing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isArabic) "حساب المستخدم" else "User Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // User card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = SkyLight,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = SkyPrimary,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = userPhone,
                                fontSize = 13.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        IconButton(onClick = { isEditing = !isEditing }) {
                            Icon(
                                imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = SkyPrimary
                            )
                        }
                    }

                    if (isEditing) {
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = userName,
                            onValueChange = { userName = it },
                            label = { Text(if (isArabic) "الاسم" else "Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = userPhone,
                            onValueChange = { userPhone = it },
                            label = { Text(if (isArabic) "رقم الهاتف" else "Phone") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Quick Settings: Language
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isArabic) "الإعدادات العامة" else "Settings",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleLanguage() }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = SkyPrimary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isArabic) "لغة التطبيق" else "App Language",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = if (isArabic) "العربية (تغيير إلى English)" else "English (Switch to العربية)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPrimary
                        )
                    }
                }
            }

            // Favorites / Saved Section
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isArabic) "الخدمات والمحلات المحفوظة (${favorites.size})" else "Saved Items (${favorites.size})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = Color.Red, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (favorites.isEmpty()) {
                        Text(
                            text = if (isArabic) "لم تقم بحفظ أي خدمة بعد. اضغط على رمز القلب في الإعلان لحفظه." else "No saved items yet.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        favorites.forEach { item ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF8FAFC),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectListing(item) }
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text(text = "${item.governorate} • ${item.district}", fontSize = 11.sp, color = Color.Gray)
                                    }
                                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }

            // About "وينها؟"
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isArabic) "عن تطبيق «وينها؟» العراقي" else "About Waynha App",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isArabic)
                            "تطبيق عراقي أصلي 100% مبني بأحدث تقنيات Jetpack Compose + Kotlin لربط المواطن العراقي بالفنيين والمحلات والخدمات وفرص العمل في جميع المحافظات بسرعة وبدون وسيط."
                        else
                            "Native Android app built with Jetpack Compose & Kotlin for Iraq services and stores.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "الإصدار: 1.0.0 (النسخة الرسمية)",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}
