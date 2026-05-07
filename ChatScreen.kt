package com.example.bat_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bat_project.ui.components.ChatBubble
import com.example.bat_project.utils.AppData
import com.example.bat_project.viewmodel.ChatViewModel

private val quickChips = listOf(
    "Show today's schedule",
    "Which rooms are free?",
    "Faculty workload",
    "List all subjects"
)

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = viewModel()
) {
    val uiState  by chatViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    var messageText by remember { mutableStateOf("") }

    // Auto-scroll when messages change
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.lastIndex)
        }
    }

    fun sendMessage() {
        val text = messageText.trim()
        if (text.isBlank()) return
        messageText = ""
        chatViewModel.sendMessage(text)
    }

    Column(modifier = Modifier.fillMaxSize().background(AppData.CardBackground)) {

        // ── Header ─────────────────────────────────────────────────
        ChatHeader()

        // ── Message List ───────────────────────────────────────────
        LazyColumn(
            state               = listState,
            modifier            = Modifier.weight(1f).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = uiState.messages,
                key   = { it.id }
            ) { message ->
                ChatBubble(message = message)
            }
        }

        // ── Error Banner ───────────────────────────────────────────
        uiState.errorMessage?.let { error ->
            RetryBanner(
                message = error,
                onDismiss = { chatViewModel.clearError() }
            )
        }

        // ── Quick Chips ────────────────────────────────────────────
        LazyRow(
            modifier              = Modifier.fillMaxWidth(),
            contentPadding        = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickChips) { chip ->
                QuickChip(label = chip, onClick = {
                    messageText = chip
                    sendMessage()
                })
            }
        }

        Spacer(Modifier.height(8.dp))

        // ── Input Row ──────────────────────────────────────────────
        Row(
            modifier          = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value         = messageText,
                onValueChange = { messageText = it },
                modifier      = Modifier.weight(1f),
                placeholder   = { Text("Ask about schedules…") },
                shape         = RoundedCornerShape(50),
                colors        = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor   = Color.White,
                    focusedIndicatorColor   = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { sendMessage() }),
                singleLine      = true
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick        = { sendMessage() },
                modifier       = Modifier.size(50.dp),
                shape          = CircleShape,
                colors         = ButtonDefaults.buttonColors(containerColor = AppData.PrimaryBlue),
                contentPadding = PaddingValues(0.dp),
                enabled        = messageText.isNotBlank() && !uiState.isLoading
            ) {
                Icon(Icons.Default.Send, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
private fun ChatHeader() {
    Surface(color = Color.White, shadowElevation = 2.dp) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(modifier = Modifier.size(44.dp), color = AppData.PrimaryBlue, shape = CircleShape) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Chat, null, tint = Color.White, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Schedule Assistant", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("● Online", color = AppData.PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun QuickChip(label: String, onClick: () -> Unit) {
    Surface(
        color    = Color.White,
        shape    = RoundedCornerShape(50),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(label, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            fontSize = 12.sp, color = AppData.TextGray)
    }
}

@Composable
private fun RetryBanner(message: String, onDismiss: () -> Unit) {
    Surface(
        color    = AppData.PrimaryRed.copy(alpha = 0.1f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text     = "Error: $message",
                color    = AppData.PrimaryRed,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = AppData.PrimaryRed, fontSize = 12.sp)
            }
        }
    }
}
