package com.example.bat_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bat_project.domain.model.RoomData
import com.example.bat_project.utils.AppData

private val ROOM_FILTERS = listOf("All", "Available", "Occupied", "Labs")

@Composable
fun ClassroomsScreen(
    onRoomClick: (RoomData) -> Unit,
    onShowToast: (String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }

    val occupiedCount  = AppData.roomList.count { it.status == "Occupied" }
    val availableCount = AppData.roomList.count { it.status == "Available" }
    val maintCount     = AppData.roomList.count { it.status == "Maintenance" }
    val utilizationPct = (occupiedCount.toFloat() / AppData.roomList.size * 100).toInt()

    val filteredRooms = when (selectedFilter) {
        "Available" -> AppData.roomList.filter { it.status == "Available" }
        "Occupied"  -> AppData.roomList.filter { it.status == "Occupied" }
        "Labs"      -> AppData.roomList.filter { it.name.startsWith("Lab") }
        else        -> AppData.roomList
    }

    Column(modifier = Modifier.fillMaxSize().background(AppData.CardBackground)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Classrooms", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
            Text("Real-time room availability", fontSize = 13.sp, color = AppData.TextGray)
            Spacer(Modifier.height(16.dp))

            // Utilization Card
            UtilizationCard(
                utilizationPct = utilizationPct,
                availableCount = availableCount,
                occupiedCount  = occupiedCount,
                maintCount     = maintCount
            )

            Spacer(Modifier.height(12.dp))

            // Filter Chips
            Row(
                modifier              = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ROOM_FILTERS.forEach { filter ->
                    FilterChip(
                        label      = filter,
                        isSelected = filter == selectedFilter,
                        onClick    = { selectedFilter = filter }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            filteredRooms.forEach { room ->
                RoomItem(room = room, onClick = { onRoomClick(room) })
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun UtilizationCard(
    utilizationPct: Int,
    availableCount: Int,
    occupiedCount:  Int,
    maintCount:     Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        shape    = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("UTILIZATION", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = AppData.TextGray, letterSpacing = 0.5.sp)
                Text("$utilizationPct%", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress   = { utilizationPct / 100f },
                modifier   = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color      = AppData.PrimaryBlue,
                trackColor = Color(0xFFEEEEEE)
            )
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("🟢 $availableCount Available", fontSize = 12.sp, color = AppData.TextGray)
                Text("🔴 $occupiedCount Occupied",   fontSize = 12.sp, color = AppData.TextGray)
                Text("🟡 $maintCount Maintenance",   fontSize = 12.sp, color = AppData.TextGray)
            }
        }
    }
}

@Composable
private fun FilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick        = onClick,
        colors         = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) AppData.PrimaryBlue else Color.White,
            contentColor   = if (isSelected) Color.White else AppData.TextGray
        ),
        shape          = RoundedCornerShape(50),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(label, fontSize = 13.sp)
    }
}

@Composable
private fun RoomItem(room: RoomData, onClick: () -> Unit) {
    val statusColor = when (room.status) {
        "Occupied"    -> AppData.PrimaryPink
        "Available"   -> AppData.PrimaryBlue
        "Maintenance" -> AppData.PrimaryYellow
        else          -> AppData.TextGray
    }
    val iconColor: Color = when {
        room.type == "Physics Lab"   -> AppData.PrimaryPink
        room.type == "Computer Lab"  -> AppData.PrimaryYellow
        room.status == "Available"   -> AppData.PrimaryGreen
        else                         -> AppData.PrimaryBlue
    }
    val icon: ImageVector = when (room.type) {
        "Physics Lab"   -> Icons.Default.Science
        "Computer Lab"  -> Icons.Default.Computer
        "Seminar"       -> Icons.Default.Group
        else            -> Icons.Default.DesktopWindows
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable(onClick = onClick),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        shape    = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier          = Modifier.padding(14.dp, 14.dp, 16.dp, 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(modifier = Modifier.size(48.dp), color = iconColor, shape = RoundedCornerShape(12.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = Color.White, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(room.name, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                    Spacer(Modifier.width(8.dp))
                    Surface(color = statusColor.copy(alpha = 0.13f), shape = RoundedCornerShape(50)) {
                        Text(
                            room.status.uppercase(),
                            color    = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Group,      null, tint = AppData.TextGray, modifier = Modifier.size(13.dp))
                    Text(" ${room.capacity}  ",   color = AppData.TextGray, fontSize = 12.sp)
                    Icon(Icons.Default.LocationOn, null, tint = AppData.TextGray, modifier = Modifier.size(13.dp))
                    Text(" ${room.type}",          color = AppData.TextGray, fontSize = 12.sp)
                }
                if (room.currentSubject.isNotEmpty()) {
                    Spacer(Modifier.height(2.dp))
                    Text(room.currentSubject, color = AppData.PrimaryBlue, fontSize = 12.sp,
                        fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
