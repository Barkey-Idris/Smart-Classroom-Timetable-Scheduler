package com.example.bat_project

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bat_project.domain.model.ClassItem
import com.example.bat_project.domain.model.RoomData
import com.example.bat_project.ui.components.AppToast
import com.example.bat_project.ui.components.BottomSheetModal
import com.example.bat_project.ui.components.sheets.*
import com.example.bat_project.ui.screens.*
import com.example.bat_project.ui.theme.Bat_ProjectTheme
import com.example.bat_project.utils.AppData
import com.example.bat_project.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        enableEdgeToEdge()
        setContent {
            Bat_ProjectTheme {
                AppRoot()
            }
        }
    }
}

@Composable
private fun AppRoot(mainViewModel: MainViewModel = viewModel()) {
    val uiState by mainViewModel.uiState.collectAsState()

    if (!uiState.isLoggedIn) {
        LoginScreen(onLoginSuccess = { mainViewModel.login() })
        return
    }

    // ── Overlay state (purely UI, no business logic) ───────────────
    var showSearch           by remember { mutableStateOf(false) }
    var showNotifications    by remember { mutableStateOf(false) }
    var showFacultyModal     by remember { mutableStateOf(false) }
    var showSubjectModal     by remember { mutableStateOf(false) }
    var selectedClassDetail  by remember { mutableStateOf<ClassItem?>(null) }
    var selectedRoomDetail   by remember { mutableStateOf<RoomData?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                AppBottomBar(
                    currentTab   = uiState.currentTab,
                    onTabSelected = { mainViewModel.navigateTo(it) }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (uiState.currentTab) {
                    0 -> HomeScreen(
                        onNavigate        = { mainViewModel.navigateTo(it) },
                        onSearch          = { showSearch = true },
                        onNotifications   = { showNotifications = true },
                        onFacultyWorkload  = { showFacultyModal = true },
                        onSubjectConfig    = { showSubjectModal = true },
                        onShowToast       = { mainViewModel.showToast(it) },
                        unreadCount       = uiState.unreadCount
                    )
                    1 -> TimetableScreen(
                        onClassClick = { selectedClassDetail = it },
                        onShowToast  = { mainViewModel.showToast(it) }
                    )
                    2 -> ClassroomsScreen(
                        onRoomClick = { selectedRoomDetail = it },
                        onShowToast = { mainViewModel.showToast(it) }
                    )
                    3 -> ChatScreen()
                    4 -> ProfileScreen(
                        onNotifications = { showNotifications = true },
                        onNavigate      = { mainViewModel.navigateTo(it) },
                        onSignOut       = { mainViewModel.signOut() },
                        onShowToast     = { mainViewModel.showToast(it) }
                    )
                }
            }
        }

        // ── Overlays ───────────────────────────────────────────────
        if (showSearch) {
            SearchOverlay(
                onClose     = { showSearch = false },
                onNavigate  = { showSearch = false; mainViewModel.navigateTo(it) },
                onShowToast = { mainViewModel.showToast(it) }
            )
        }

        if (showNotifications) {
            BottomSheetModal(onDismiss = { showNotifications = false }) {
                NotificationsSheet(
                    notifications = uiState.notifications,
                    onMarkRead    = { mainViewModel.markNotificationRead(it) },
                    onClearAll    = { mainViewModel.clearAllNotifications(); showNotifications = false }
                )
            }
        }

        if (showFacultyModal) {
            BottomSheetModal(onDismiss = { showFacultyModal = false }) {
                FacultyWorkloadSheet()
            }
        }

        if (showSubjectModal) {
            BottomSheetModal(onDismiss = { showSubjectModal = false }) {
                SubjectConfigSheet()
            }
        }

        selectedClassDetail?.let { cls ->
            BottomSheetModal(onDismiss = { selectedClassDetail = null }) {
                ClassDetailSheet(
                    cls    = cls,
                    onBook = {
                        selectedClassDetail = null
                        mainViewModel.showToast("Added to personal calendar ✓")
                    }
                )
            }
        }

        selectedRoomDetail?.let { room ->
            BottomSheetModal(onDismiss = { selectedRoomDetail = null }) {
                RoomDetailSheet(
                    room           = room,
                    onBook         = { selectedRoomDetail = null; mainViewModel.showToast("Room booking request sent ✓") },
                    onViewSchedule = { selectedRoomDetail = null; mainViewModel.showToast("Timetable view opened") }
                )
            }
        }

        // ── Toast ──────────────────────────────────────────────────
        AppToast(message = uiState.toastMessage)
    }
}

@Composable
private fun AppBottomBar(currentTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        listOf(
            Triple("Home",     Icons.Default.Home,          0),
            Triple("Schedule", Icons.Default.CalendarToday, 1),
            Triple("Rooms",    Icons.Default.Business,      2),
            Triple("Chat",     Icons.AutoMirrored.Filled.Chat, 3),
            Triple("Profile",  Icons.Default.Person,        4)
        ).forEach { (label, icon, index) ->
            NavigationBarItem(
                selected  = currentTab == index,
                onClick   = { onTabSelected(index) },
                icon      = { Icon(icon, contentDescription = label) },
                label     = { Text(label, fontSize = 10.sp) },
                colors    = NavigationBarItemDefaults.colors(
                    selectedIconColor   = AppData.PrimaryBlue,
                    unselectedIconColor = AppData.TextGray,
                    indicatorColor      = AppData.PrimaryBlue.copy(alpha = 0.1f)
                )
            )
        }
    }
}
