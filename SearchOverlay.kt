package com.example.bat_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bat_project.domain.model.SearchResult
import com.example.bat_project.utils.AppData

@Composable
fun SearchOverlay(
    onClose:     () -> Unit,
    onNavigate:  (Int) -> Unit,
    onShowToast: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    // Build a flat searchable index from all data sources
    val allResults: List<SearchResult> = remember {
        buildList {
            AppData.roomList.forEach    { add(SearchResult(it.name, "${it.status} · ${it.type}",                     "Room",    2)) }
            AppData.subjectList.forEach { add(SearchResult(it.name, "${it.code} · ${it.faculty}",                    "Subject", 1)) }
            AppData.facultyList.forEach { add(SearchResult(it.name, "${it.dept} · ${it.subjects.joinToString(", ")}","Faculty", 0)) }
        }
    }

    val filtered = if (query.isBlank()) emptyList()
    else allResults.filter { it.label.contains(query, ignoreCase = true) }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.ChevronLeft, null)
                }
                TextField(
                    value         = query,
                    onValueChange = { query = it },
                    modifier      = Modifier.weight(1f).focusRequester(focusRequester),
                    placeholder   = { Text("Search rooms, subjects, faculty…") },
                    shape         = RoundedCornerShape(12.dp),
                    colors        = TextFieldDefaults.colors(
                        focusedContainerColor   = AppData.CardBackground,
                        unfocusedContainerColor = AppData.CardBackground,
                        focusedIndicatorColor   = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            Spacer(Modifier.height(20.dp))

            if (query.isNotBlank() && filtered.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No results for \"$query\"", color = AppData.TextGray,
                        modifier = Modifier.padding(top = 32.dp))
                }
            }

            LazyColumn {
                items(filtered) { result ->
                    SearchResultRow(
                        result  = result,
                        onClick = {
                            onClose()
                            onNavigate(result.tab)
                            onShowToast("Viewing ${result.label}")
                        }
                    )
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                }
            }
        }
    }
}

@Composable
private fun SearchResultRow(result: SearchResult, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(result.label, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Spacer(Modifier.width(8.dp))
            Surface(color = Color(0xFFF0F0F0), shape = RoundedCornerShape(50)) {
                Text(result.type, fontSize = 11.sp, color = AppData.TextGray,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp))
            }
        }
        Text(result.subLabel, fontSize = 12.sp, color = AppData.TextGray,
            modifier = Modifier.padding(top = 2.dp))
    }
}
