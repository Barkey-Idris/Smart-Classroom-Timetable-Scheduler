package com.example.bat_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
fun ProfileScreen(
    onNotifications: () -> Unit,
    onNavigate:      (Int) -> Unit,
    onSignOut:       () -> Unit,
    onShowToast:     (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppData.CardBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Header gradient
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))))
                .padding(24.dp)
        ) {
            Text("Profile", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
        }

        Column(modifier = Modifier.padding(20.dp)) {
            // ── Profile Card ───────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors   = CardDefaults.cardColors(containerColor = Color.White),
                shape    = RoundedCornerShape(20.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(modifier = Modifier.size(56.dp), color = AppData.PrimaryBlue,
                        shape = RoundedCornerShape(16.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, null, tint = Color.White,
                                modifier = Modifier.size(32.dp))
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text("Dr. Sharma", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Text("HOD, Computer Science", color = AppData.TextGray, fontSize = 13.sp)
                        Spacer(Modifier.height(4.dp))
                        Surface(color = AppData.PrimaryBlue.copy(alpha = 0.1f), shape = RoundedCornerShape(50)) {
                            Text("ADMIN", color = AppData.PrimaryBlue, fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier   = Modifier.padding(horizontal = 10.dp, vertical = 3.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Stats ──────────────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("8" to "SCHEDULES CREATED", "3" to "PENDING APPROVALS", "5" to "YEARS ACTIVE")
                    .forEach { (value, label) ->
                        Card(modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape  = RoundedCornerShape(16.dp)) {
                            Column(modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(value, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                                Text(label, fontSize = 9.sp,  color = AppData.TextGray, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
            }

            Spacer(Modifier.height(20.dp))

            // ── Recent Activity ────────────────────────────────────
            SectionLabel("RECENT ACTIVITY")
            Spacer(Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape  = RoundedCornerShape(16.dp)) {
                Column {
                    ActivityRow("CS Dept Timetable Updated",  "2 hours ago", isRead = true)  { onShowToast("CS Dept schedule opened") }
                    HorizontalDivider(color = Color(0xFFF5F5F5))
                    ActivityRow("Pending Approval: Room 303", "Yesterday",   isRead = false) { onShowToast("Approval details opened") }
                    HorizontalDivider(color = Color(0xFFF5F5F5))
                    ActivityRow("Schedule Exported to PDF",   "3 days ago",  isRead = true)  { onShowToast("Schedule exported") }
                }
            }

            Spacer(Modifier.height(20.dp))
            SectionLabel("SETTINGS")
            Spacer(Modifier.height(8.dp))

            ProfileActionRow("Account Settings",  Icons.Default.Settings,      onClick = { onShowToast("Account Settings") })
            ProfileActionRow("Notifications",     Icons.Default.Notifications,  onClick = onNotifications)
            ProfileActionRow("Approval Workflow", Icons.Default.Shield,         onClick = { onShowToast("Approval Workflow opened") })
            ProfileActionRow("Help & Support",    Icons.Default.HelpOutline,    onClick = { onNavigate(3) })
            Spacer(Modifier.height(8.dp))
            ProfileActionRow("Sign Out", Icons.Default.ExitToApp, isDestructive = true, onClick = onSignOut)
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, color = AppData.TextGray, fontSize = 12.sp,
        fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
}

@Composable
private fun ActivityRow(title: String, time: String, isRead: Boolean, onClick: () -> Unit) {
    Row(
        modifier          = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.size(8.dp).offset(y = 5.dp)
                .background(if (isRead) Color(0xFFDDDDDD) else AppData.PrimaryBlue, CircleShape)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(time,  color = AppData.TextGray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ProfileActionRow(
    title:         String,
    icon:          ImageVector,
    isDestructive: Boolean = false,
    onClick:       () -> Unit
) {
    val color = if (isDestructive) AppData.PrimaryRed else AppData.PrimaryBlue
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable(onClick = onClick),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        shape    = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = color)
            Spacer(Modifier.width(16.dp))
            Text(title, fontWeight = FontWeight.Medium,
                color = if (isDestructive) color else Color.Black)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = AppData.TextGray)
        }
    }
}
