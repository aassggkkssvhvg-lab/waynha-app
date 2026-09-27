package iq.waynha.app.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import iq.waynha.app.model.Listing
import iq.waynha.app.model.Review
import iq.waynha.app.ui.components.dialPhone
import iq.waynha.app.ui.components.openMaps
import iq.waynha.app.ui.theme.AmberAccent
import iq.waynha.app.ui.theme.EmeraldSuccess
import iq.waynha.app.ui.theme.SkyLight
import iq.waynha.app.ui.theme.SkyPrimary
import iq.waynha.app.viewmodel.WaynhaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    listing: Listing,
    viewModel: WaynhaViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isArabic by viewModel.isArabic.collectAsState()
    var showReviewDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = listing.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(listing.id) }) {
                        Icon(
                            imageVector = if (listing.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (listing.isFavorite) Color.Red else Color.Gray
                        )
                    }
                    IconButton(onClick = { shareListing(context, listing) }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Call Button
                    Button(
                        onClick = { dialPhone(context, listing.phone) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArabic) "اتصال فوري" else "Call Now",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    // Maps Button
                    Button(
                        onClick = { openMaps(context, listing.lat, listing.lng, listing.title) },
                        colors = ButtonDefaults.buttonColors(containerColor = SkyPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArabic) "على الخريطة" else "View on Map",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    // WhatsApp Button (if available)
                    if (listing.whatsapp.isNotBlank()) {
                        IconButton(
                            onClick = { openWhatsApp(context, listing.whatsapp, listing.title, isArabic) },
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF25D366)),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "WhatsApp",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(2.dp)) }

            // Title & Badges Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = listing.title,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${listing.governorate} • ${listing.district} • ${listing.addressDetails}",
                                    fontSize = 13.sp,
                                    color = Color(0xFF64748B)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFFFBEB)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star",
                                        tint = AmberAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = String.format("%.1f", listing.rating),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Info row: Pricing and Hours
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SkyLight,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = if (isArabic) "الأسعار والكشفية" else "Pricing",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0369A1)
                                    )
                                    Text(
                                        text = listing.priceNote,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SkyPrimary
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF1F5F9),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = if (isArabic) "أوقات العمل" else "Working Hours",
                                        fontSize = 11.sp,
                                        color = Color(0xFF475569)
                                    )
                                    Text(
                                        text = listing.workingHours,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Description Section
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = if (isArabic) "عن الخدمة والمحل" else "About Service",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = listing.description,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            color = Color(0xFF334155)
                        )
                    }
                }
            }

            // Reviews & Ratings Section
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isArabic)
                                    "آراء وتقييمات العملاء (${listing.reviewsCount})"
                                else
                                    "Customer Reviews (${listing.reviewsCount})",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            TextButton(onClick = { showReviewDialog = true }) {
                                Text(
                                    text = if (isArabic) "+ إضافة تقييم" else "+ Add Review",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SkyPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (listing.reviews.isEmpty()) {
                            Text(
                                text = if (isArabic) "لا توجد تقييمات بعد. كن أول من يقيّم!" else "No reviews yet.",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        } else {
                            listing.reviews.forEach { rev ->
                                ReviewItemRow(review = rev)
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

    if (showReviewDialog) {
        AddReviewDialog(
            isArabic = isArabic,
            onDismiss = { showReviewDialog = false },
            onSubmit = { rating, comment, name ->
                viewModel.addReview(listing.id, rating, comment, name)
                showReviewDialog = false
            }
        )
    }
}

@Composable
fun ReviewItemRow(review: Review) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.userName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Row {
                    repeat(review.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AmberAccent,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = review.comment,
                fontSize = 12.sp,
                color = Color(0xFF475569)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = review.date,
                fontSize = 10.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
fun AddReviewDialog(
    isArabic: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (rating: Int, comment: String, name: String) -> Unit
) {
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isArabic) "إضافة تقييم وتجربة" else "Write a Review",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Star picker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    (1..5).forEach { star ->
                        IconButton(onClick = { rating = star }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$star stars",
                                tint = if (star <= rating) AmberAccent else Color(0xFFCBD5E1),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text(if (isArabic) "اسمك الكريم" else "Your Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text(if (isArabic) "اكتب رأيك وتجربتك بالخدمة" else "Review details") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (comment.isNotBlank()) {
                        onSubmit(rating, comment, userName)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SkyPrimary)
            ) {
                Text(if (isArabic) "نشر التقييم" else "Post Review")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isArabic) "إلغاء" else "Cancel")
            }
        }
    )
}

fun shareListing(context: Context, listing: Listing) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "${listing.title}\n${listing.governorate} - ${listing.district}\nهاتف: ${listing.phone}\nتطبيق «وينها؟» العراق"
        )
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, "مشاركة الخدمة"))
}

fun openWhatsApp(context: Context, phone: String, title: String, isArabic: Boolean) {
    try {
        val cleanNumber = phone.replace("+", "").replace(" ", "")
        val msg = if (isArabic)
            "مرحباً، شفت إعلانك ($title) على تطبيق «وينها؟» وأريد استفسر."
        else
            "Hello, I saw your listing ($title) on Waynha app."
        val url = "https://api.whatsapp.com/send?phone=$cleanNumber&text=${Uri.encode(msg)}"
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (_: Exception) {}
}
