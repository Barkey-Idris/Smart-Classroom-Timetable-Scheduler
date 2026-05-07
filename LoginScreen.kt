package com.example.bat_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bat_project.utils.AppData

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email    by remember { mutableStateOf("admin@sih.gov.in") }
    var password by remember { mutableStateOf("password123") }
    var errorMsg by remember { mutableStateOf("") }   // ← FIX: empty initially

    fun tryLogin() {
        errorMsg = when {
            email.isBlank() || password.isBlank() -> "Please fill in both fields."
            !email.contains("@")                  -> "Enter a valid email."
            password.length < 6                   -> "Password must be at least 6 characters."
            else                                  -> { onLoginSuccess(); return }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppData.CardBackground)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Brand Row ──────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(36.dp),
                    color    = AppData.PrimaryBlue,
                    shape    = RoundedCornerShape(10.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.School, null, tint = Color.White,
                            modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(AppData.PrimaryBlue, fontWeight = FontWeight.Bold)) { append("Smart ") }
                        withStyle(SpanStyle(Color.Black,          fontWeight = FontWeight.Bold)) { append("Classroom") }
                    },
                    fontSize = 19.sp
                )
            }

            Spacer(Modifier.height(48.dp))

            // ── Hero Title ─────────────────────────────────────────
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(Color.Black,        fontWeight = FontWeight.ExtraBold, fontSize = 44.sp)) { append("Timetable\n") }
                    withStyle(SpanStyle(AppData.PrimaryBlue, fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic, fontSize = 44.sp)) { append("Scheduling App.") }
                },
                lineHeight = 50.sp
            )

            Spacer(Modifier.height(36.dp))

            // ── Login Card ─────────────────────────────────────────
            Card(
                modifier  = Modifier.fillMaxWidth(),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                shape     = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    InputLabel("INSTITUTE EMAIL")
                    Spacer(Modifier.height(6.dp))
                    StyledTextField(
                        value         = email,
                        onValueChange = { email = it; errorMsg = "" },
                        placeholder   = "admin@sih.gov.in",
                        imeAction     = ImeAction.Next
                    )

                    Spacer(Modifier.height(18.dp))
                    InputLabel("PASSWORD")
                    Spacer(Modifier.height(6.dp))
                    StyledTextField(
                        value                = password,
                        onValueChange        = { password = it; errorMsg = "" },
                        placeholder          = "••••••••",
                        imeAction            = ImeAction.Done,
                        isPassword           = true,
                        keyboardActions      = KeyboardActions(onDone = { tryLogin() })
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick  = { tryLogin() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = AppData.PrimaryBlue),
                        shape    = RoundedCornerShape(12.dp)
                    ) {
                        Text("Login to Dashboard", fontWeight = FontWeight.Bold,
                            fontSize = 16.sp, color = Color.White)
                    }

                    // Error — only shown after a failed attempt
                    if (errorMsg.isNotEmpty()) {
                        Spacer(Modifier.height(10.dp))
                        Text(errorMsg, color = AppData.PrimaryRed, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// ── Private helpers ────────────────────────────────────────────────
@Composable
private fun InputLabel(text: String) {
    Text(text, color = AppData.TextGray, fontSize = 11.sp,
        fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
}

@Composable
private fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    imeAction: ImeAction,
    isPassword: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    TextField(
        value                = value,
        onValueChange        = onValueChange,
        modifier             = Modifier.fillMaxWidth(),
        placeholder          = { Text(placeholder, color = AppData.TextGray) },
        visualTransformation = if (isPassword) PasswordVisualTransformation()
                               else androidx.compose.ui.text.input.VisualTransformation.None,
        colors               = TextFieldDefaults.colors(
            focusedContainerColor   = AppData.CardBackground,
            unfocusedContainerColor = AppData.CardBackground,
            focusedIndicatorColor   = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape           = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = keyboardActions,
        singleLine      = true
    )
}
