package iq.waynha.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import iq.waynha.app.model.CategoryType
import iq.waynha.app.ui.components.IraqBottomBar
import iq.waynha.app.ui.screens.LandingScreen
import iq.waynha.app.ui.screens.MapScreen
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
    // التبويبات: 0 بحث، 1 خريطة، 2 رئيسية، 3 وظائف، 4 حسابي
    var currentTab by remember { mutableStateOf(2) }
    var showAdd by remember { mutableStateOf(false) }

    CompositionLocalProvider(
        LocalLayoutDirection provides if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr
    ) {
        if (selectedListing != null) {
            DetailScreen(
                listing = selectedListing!!,
                viewModel = viewModel,
                onBack = { viewModel.selectListing(null) }
            )
        } else if (showAdd) {
            BackHandler { showAdd = false }
            AddListingScreen(
                viewModel = viewModel,
                onListingAdded = { showAdd = false; currentTab = 0 }
            )
        } else {
            Scaffold(
                containerColor = Color(0xFF060B1A),
                bottomBar = {
                    IraqBottomBar(selected = currentTab, isArabic = isArabic) { tab ->
                        if (tab == 3) viewModel.selectCategory(CategoryType.JOBS)
                        if (tab == 0 && currentTab == 3) viewModel.selectCategory(CategoryType.ALL)
                        currentTab = tab
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    when (currentTab) {
                        0, 3 -> HomeScreen(
                            viewModel = viewModel,
                            onSelectListing = { viewModel.selectListing(it) }
                        )
                        1 -> MapScreen(
                            viewModel = viewModel,
                            onOpenDetails = { viewModel.selectListing(it) }
                        )
                        2 -> LandingScreen(
                            isArabic = isArabic,
                            onSearch = { viewModel.selectCategory(CategoryType.ALL); currentTab = 0 },
                            onNearMe = { currentTab = 1 },
                            onAiSearch = { viewModel.selectCategory(CategoryType.ALL); currentTab = 0 },
                            onCategory = { cat ->
                                viewModel.selectCategory(cat)
                                currentTab = if (cat == CategoryType.JOBS) 3 else 0
                            },
                            onMap = { currentTab = 1 },
                            onToggleLanguage = { viewModel.toggleLanguage() }
                        )
                        4 -> Box {
                            ProfileScreen(
                                viewModel = viewModel,
                                onSelectListing = { viewModel.selectListing(it) }
                            )
                            ExtendedFloatingActionButton(
                                onClick = { showAdd = true },
                                containerColor = Color(0xFFFFC21A),
                                contentColor = Color(0xFF060B1A),
                                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
                            ) {
                                Icon(Icons.Default.AddCircle, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text(if (isArabic) "أضف إعلانك" else "Add listing")
                            }
                        }
                    }
                }
            }
        }
    }
}
