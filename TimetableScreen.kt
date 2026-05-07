package com.example.bat_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bat_project.domain.model.ClassItem
import com.example.bat_project.utils.AppData

private val DAYS = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

@Composable
fun TimetableScreen(
    onClassClick: (ClassItem) -> Unit,
    onShowToast:  (String) -> Unit
) {
    var selectedDay by remember { mutableStateOf("Mon") }
    var currentWeek by remember { mutableIntStateOf(12) }
    val classes = AppData.timetableData[selectedDay] ?: emptyList()

    Column(modifier = Modifier.fillMaxSize().background(AppData.CardBackground)) {

        // ── Top Controls ───────────────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Timetable", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                WeekNavigator(
                    week       = currentWeek,
                    onPrevious = { if (currentWeek > 1) currentWeek-- },
                    onNext     = { currentWeek++ }
                )
            }
            Spacer(Modifier.height(16.dp))
            DaySelector(selectedDay = selectedDay, onDaySelected = { selectedDay = it })
        }

        // ── Classes List ───────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            if (classes.isEmpty()) {
                EmptyDayCard()
            } else {
                classes.forEach { cls ->
                    ScheduleCard(cls = cls, onClick = { onClassClick(cls) })
                }
            }

            Spacer(Modifier.height(16.dp))

            // Day Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors   = CardDefaults.cardColors(containerColor = Color.White),
                shape    = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("DAY SUMMARY", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = AppData.TextGray, letterSpacing = 0.5.sp)
                    Spacer(Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${classes.size} Class${if (classes.size != 1) "es" else ""}",
                            fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("${classes.size} hr${if (classes.size != 1) "s" else ""}",
                            fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = AppData.TextGray)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun WeekNavigator(week: Int, onPrevious: () -> Unit, onNext: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onPrevious) {
            Icon(Icons.Default.ChevronLeft, null, tint = AppData.TextGray)
        }
        Text("Week $week", fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.widthIn(min = 64.dp))
        IconButton(onClick = onNext) {
            Icon(Icons.Default.ChevronRight, null, tint = AppData.TextGray)
        }
    }
}

@Composable
private fun DaySelector(selectedDay: String, onDaySelected: (String) -> Unit) {
    Row(
        modifier              = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DAYS.forEach { day ->
            val isSelected = day == selectedDay
            Button(
                onClick        = { onDaySelected(day) },
                colors         = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) AppData.PrimaryBlue else Color.White,
                    contentColor   = if (isSelected) Color.White else AppData.TextGray
                ),
                shape          = CircleShape,
                modifier       = Modifier.size(52.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(day, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun EmptyDayCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        shape    = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier            = Modifier.padding(40.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No classes scheduled", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text("Enjoy your free day! 🎉", fontSize = 13.sp, color = AppData.TextGray)
        }
    }
}

@Composable
private fun ScheduleCard(cls: ClassItem, onClick: () -> Unit) {
    Row(
        modifier          = Modifier.padding(vertical = 6.dp).clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.width(52.dp)) {
            Text(cls.time,    fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(cls.endTime, color = AppData.TextGray,     fontSize = 11.sp)
        }
        Spacer(Modifier.width(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth().height(80.dp),
            colors   = CardDefaults.cardColors(containerColor = cls.color),
            shape    = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier            = Modifier.padding(horizontal = 16.dp).fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(cls.subject, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                Text(cls.room,    color = Color.White.copy(0.85f), fontSize = 13.sp)
            }
        }
    }
}
