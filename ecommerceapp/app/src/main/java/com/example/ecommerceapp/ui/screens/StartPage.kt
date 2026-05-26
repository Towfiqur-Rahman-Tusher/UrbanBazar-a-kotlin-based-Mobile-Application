package com.example.ecommerceapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.R

@Composable
fun StartPage(
    onGuest: () -> Unit,
    onMember: () -> Unit,
    onLogin: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Gradient Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.6f)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFCDFFD8), Color(0xFF94B9FF)),
                        start = Offset(0f, 0f),
                        end = Offset(1f, 0f)
                    )
                )
        )

        // Background Logo
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.1f)
                .align(Alignment.Center)
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Urban Bazaar",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00695C)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onGuest,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00a37a),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Browse as Guest", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onMember,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00a37a),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Become a Member", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Already a Member? Login",
                color = Color(0xFF00695C),
                modifier = Modifier
                    .clickable(onClick = onLogin)
                    .padding(top = 8.dp),
                textDecoration = TextDecoration.Underline,
                fontSize = 14.sp
            )
        }
    }
}
