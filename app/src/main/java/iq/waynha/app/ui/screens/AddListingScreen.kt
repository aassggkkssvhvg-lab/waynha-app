package iq.waynha.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.waynha.app.data.IraqLocations
import iq.waynha.app.model.CategoryType
import iq.waynha.app.model.Governorate
import iq.waynha.app.model.Listing
import iq.waynha.app.ui.theme.EmeraldSuccess
import iq.waynha.app.ui.theme.SkyLight
import iq.waynha.app.ui.theme.SkyPrimary
import iq.waynha.app.viewmodel.WaynhaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen(
    viewModel: WaynhaViewModel,
    onListingAdded: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic by viewModel.isArabic.collectAsState()

    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(CategoryType.ELECTRICIANS) }
    var selectedGov by remember { mutableStateOf(IraqLocations.getDefaultGovernorate()) }
    var selectedDistrict by remember { mutableStateOf(selectedGov.districts.getOrElse(1) { "الكرادة" }) }
    var addressDetails by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("07") }
    var whatsapp by remember { mutableStateOf("") }
    var priceNote by remember { mutableStateOf("") }
    var workingHours by remember { mutableStateOf("8:00 ص - 9:00 م") }
    var description by remember { mutableStateOf("") }

    var isGovMenuOpen by remember { mutableStateOf(false) }
    var isDistrictMenuOpen by remember { mutableStateOf(false) }
    var isCategoryMenuOpen by remember { mutableStateOf(false) }

    var showSuccessMessage by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isArabic) "إضافة إعلان جديد" else "Add New Listing",
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

            if (showSuccessMessage) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = EmeraldSuccess)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isArabic) "تم نشر إعلانك بنجاح في تطبيق «وينها؟»!" else "Your listing has been published successfully!",
                            color = EmeraldSuccess,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(if (isArabic) "عنوان الإعلان أو اسم المحل / الفني *" else "Title or Business Name *") },
                placeholder = { Text(if (isArabic) "مثال: الأسطة حيدر لتأسيس الكهرباء" else "e.g. Master Haider Electrician") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Category Picker
            ExposedDropdownMenuBox(
                expanded = isCategoryMenuOpen,
                onExpandedChange = { isCategoryMenuOpen = it }
            ) {
                OutlinedTextField(
                    value = if (isArabic) selectedCategory.nameAr else selectedCategory.nameEn,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(if (isArabic) "تصنيف الخدمة" else "Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryMenuOpen) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isCategoryMenuOpen,
                    onDismissRequest = { isCategoryMenuOpen = false }
                ) {
                    CategoryType.values().filter { it != CategoryType.ALL }.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(if (isArabic) cat.nameAr else cat.nameEn) },
                            onClick = {
                                selectedCategory = cat
                                isCategoryMenuOpen = false
                            }
                        )
                    }
                }
            }

            // Governorate & District Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Governorate
                ExposedDropdownMenuBox(
                    expanded = isGovMenuOpen,
                    onExpandedChange = { isGovMenuOpen = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = if (isArabic) selectedGov.nameAr else selectedGov.nameEn,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (isArabic) "المحافظة" else "Governorate") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGovMenuOpen) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isGovMenuOpen,
                        onDismissRequest = { isGovMenuOpen = false }
                    ) {
                        IraqLocations.governorates.forEach { gov ->
                            DropdownMenuItem(
                                text = { Text(if (isArabic) gov.nameAr else gov.nameEn) },
                                onClick = {
                                    selectedGov = gov
                                    selectedDistrict = gov.districts.getOrElse(1) { "المركز" }
                                    isGovMenuOpen = false
                                }
                            )
                        }
                    }
                }

                // District
                ExposedDropdownMenuBox(
                    expanded = isDistrictMenuOpen,
                    onExpandedChange = { isDistrictMenuOpen = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedDistrict,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (isArabic) "المنطقة" else "District") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDistrictMenuOpen) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isDistrictMenuOpen,
                        onDismissRequest = { isDistrictMenuOpen = false }
                    ) {
                        selectedGov.districts.filter { it != "الكل" }.forEach { dist ->
                            DropdownMenuItem(
                                text = { Text(dist) },
                                onClick = {
                                    selectedDistrict = dist
                                    isDistrictMenuOpen = false
                                }
                            )
                        }
                    }
                }
            }

            // Detailed Address
            OutlinedTextField(
                value = addressDetails,
                onValueChange = { addressDetails = it },
                label = { Text(if (isArabic) "العنوان التفصيلي أو أقرب نقطة دالة" else "Detailed Address / Landmark") },
                placeholder = { Text(if (isArabic) "مثال: قرب ساحة كهرمانة، فرع 4" else "e.g. Near Kahramana Square") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Phone & WhatsApp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(if (isArabic) "رقم الهاتف العراقي *" else "Phone *") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = whatsapp,
                    onValueChange = { whatsapp = it },
                    label = { Text(if (isArabic) "رقم الواتساب" else "WhatsApp") },
                    placeholder = { Text("0780...") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            // Price Note & Hours
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = priceNote,
                    onValueChange = { priceNote = it },
                    label = { Text(if (isArabic) "ملاحظة الأسعار أو الكشفية" else "Price Note") },
                    placeholder = { Text(if (isArabic) "مثال: كشفية 15 ألف" else "e.g. 15k IQD inspection") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = workingHours,
                    onValueChange = { workingHours = it },
                    label = { Text(if (isArabic) "ساعات العمل" else "Working Hours") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(if (isArabic) "تفاصيل الإعلان وما تقدمه للزبائن *" else "Details & Description *") },
                placeholder = { Text(if (isArabic) "اشرح نوع الخدمات أو القطع المتوفرة بدقة..." else "Describe your service...") },
                minLines = 3,
                maxLines = 6,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Submit Button
            Button(
                onClick = {
                    if (title.isNotBlank() && phone.isNotBlank() && description.isNotBlank()) {
                        val newListing = Listing(
                            id = "custom-${System.currentTimeMillis()}",
                            title = title.trim(),
                            category = selectedCategory,
                            governorate = selectedGov.nameAr,
                            district = selectedDistrict,
                            addressDetails = addressDetails.ifBlank { "العراق" },
                            description = description.trim(),
                            phone = phone.trim(),
                            whatsapp = whatsapp.trim().ifBlank { phone.trim().replace("^0".toRegex(), "964") },
                            priceNote = priceNote.ifBlank { if (isArabic) "حسب الاتفاق والمعاينة" else "Negotiable" },
                            workingHours = workingHours.ifBlank { "8:00 ص - 10:00 م" },
                            rating = 5.0f,
                            reviewsCount = 1,
                            lat = selectedGov.centerLat,
                            lng = selectedGov.centerLng,
                            isVerified = true,
                            isFeatured = true
                        )
                        viewModel.addListing(newListing)
                        showSuccessMessage = true
                        onListingAdded()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SkyPrimary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isArabic) "نشر الإعلان في «وينها؟»" else "Publish Listing",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}
