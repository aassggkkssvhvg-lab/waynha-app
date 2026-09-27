package iq.waynha.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import iq.waynha.app.model.Listing
import iq.waynha.app.ui.screens.AddListingScreen
import iq.waynha.app.ui.screens.DetailScreen
import iq.waynha.app.ui.screens.HomeScreen
import iq.waynha.app.ui.screens.ProfileScreen
import iq.waynha.app.ui.theme.SkyLight
import iq.waynha.app.ui.theme.SkyPrimary
import iq.waynha.app.ui.theme.WaynhaTheme
import iq.waynha.app.viewmodel.WaynhaViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: WaynhaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaynhaTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: WaynhaViewModel) {
    val isArabic by viewModel.isArabic.collectAsState()
    val selectedListing by viewModel.selectedListing.collectAsState()
    var currentTab by remember { mutableStateOf(0) }

    if (selectedListing != null) {
        DetailScreen(
            listing = selectedListing!!,
            viewModel = viewModel,
            onBack = { viewModel.selectListing(null) }
        )
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 6.dp
                ) {
                    NavigationBarItem(
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text(if (isArabic) "الرئيسية" else "Home") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SkyPrimary,
                            selectedTextColor = SkyPrimary,
                            indicatorColor = SkyLight
                        )
                    )

                    NavigationBarItem(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        icon = { Icon(Icons.Default.AddCircle, contentDescription = null) },
                        label = { Text(if (isArabic) "أضف إعلان" else "Add Ad") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SkyPrimary,
                            selectedTextColor = SkyPrimary,
                            indicatorColor = SkyLight
                        )
                    )

                    NavigationBarItem(
                        selected = currentTab == 2,
                        onClick = { currentTab = 2 },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text(if (isArabic) "حسابي" else "Profile") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SkyPrimary,
                            selectedTextColor = SkyPrimary,
                            indicatorColor = SkyLight
                        )
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (currentTab) {
                    0 -> HomeScreen(
                        viewModel = viewModel,
                        onSelectListing = { viewModel.selectListing(it) }
                    )
                    1 -> AddListingScreen(
                        viewModel = viewModel,
                        onListingAdded = { currentTab = 0 }
                    )
                    2 -> ProfileScreen(
                        viewModel = viewModel,
                        onSelectListing = { viewModel.selectListing(it) }
                    )
                }
            }
        }
    }
}
