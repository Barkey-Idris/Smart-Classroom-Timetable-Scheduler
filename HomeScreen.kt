package com.example.bat_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bat_project.utils.AppData

@Composable
fun HomeScreen(
    onNavigate:        (Int) -> Unit,
    onSearch:          () -> Unit,
    onNotifications:   () -> Unit,
    onFacultyWorkload: () -> Unit,
    onSubjectConfig:   () -> Unit,
    onShowToast:       (String) -> Unit,
    unreadCount:       Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppData.CardBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // ── Header ─────────────────────────────────────────────────
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column {
                Text("WELCOME BACK", color = AppData.TextGray, fontSize = 12.sp,
                    fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                Text("SmartSchedule", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
            }
            Row {
                IconButton(onClick = onSearch) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = onNotifications) {
                    BadgedBox(badge = {
                        if (unreadCount > 0) Badge { Text(unreadCount.toString()) }
                    }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Hero Banner ────────────────────────────────────────────
        Card(modifier = Modifier.fillMaxWidth().height(200.dp), shape = RoundedCornerShape(24.dp)) {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.linearGradient(listOf(Color(0xFF1565C0), Color(0xFF42A5F5)))
                )
            ) {
                Column(
                    modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = AppData.PrimaryYellow,
                            modifier = Modifier.size(14.dp))
                        Text("  AI Powered", color = AppData.PrimaryYellow,
                            fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Text("Smart Timetable Generator", fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Text("Optimized scheduling for NEP 2020",
                        color = Color.White.copy(0.8f), fontSize = 13.sp)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Stats ──────────────────────────────────────────────────
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatBox("24", "ROOMS",       AppData.PrimaryBlue,   Modifier.weight(1f))
            StatBox("48", "FACULTY",     AppData.PrimaryPink,   Modifier.weight(1f))
            StatBox("12", "DEPARTMENTS", AppData.PrimaryYellow, Modifier.weight(1f))
        }

        Spacer(Modifier.height(24.dp))

        Text("QUICK ACTIONS", color = AppData.TextGray, fontSize = 12.sp,
            fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
        Spacer(Modifier.height(10.dp))

        QuickActionItem("Generate Timetable",     Icons.Default.CalendarToday) {
            onNavigate(1); onShowToast("Opening Timetable Generator…")
        }
        QuickActionItem("Manage Classrooms",      Icons.Default.Domain)        { onNavigate(2) }
        QuickActionItem("Faculty Workload",        Icons.Default.Group)         { onFacultyWorkload() }
        QuickActionItem("Subject Configuration",  Icons.Default.MenuBook)      { onSubjectConfig() }
        QuickActionItem("View Active Schedules",  Icons.Default.AccessTime)    { onNavigate(1) }
    }
}

@Composable
private fun StatBox(value: String, label: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors   = CardDefaults.cardColors(containerColor = color),
        shape    = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier            = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text(label, fontSize = 10.sp, color = Color.White.copy(0.85f))
        }
    }
}

@Composable
private fun QuickActionItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable(onClick = onClick),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        shape    = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier          = Modifier.padding(14.dp, 14.dp, 16.dp, 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color    = AppData.PrimaryBlue.copy(alpha = 0.1f),
                shape    = RoundedCornerShape(12.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = AppData.PrimaryBlue, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(Modifier.width(14.dp))
            Text(title, fontWeight = FontWeight.Medium, fontSize = 15.sp)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = AppData.TextGray)
        }
    }
}
