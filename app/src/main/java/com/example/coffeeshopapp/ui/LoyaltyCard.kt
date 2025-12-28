package com.example.coffeeshopapp.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LoyaltyCard(earnedCount: Int) {
    // Đảm bảo số lượng luôn nằm trong khoảng 0 đến 8
    val safeCount = earnedCount.coerceIn(0, 8)
    val progress = safeCount / 8f
    val infiniteTransition = rememberInfiniteTransition()

    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D4B73)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2D4B73).copy(alpha = 0.9f),
                            Color(0xFF4A6588)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
        ) {
            // Shimmer effect
            Canvas(modifier = Modifier.fillMaxSize()) {
                val shimmerWidth = size.width * 0.3f
                val shimmerX = size.width * shimmerOffset - shimmerWidth
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    ),
                    topLeft = Offset(shimmerX, 0f),
                    size = Size(shimmerWidth, size.height)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Loyalty Card",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "$safeCount / 8",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Animated coffee cups with progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(8) { index ->
                        CoffeeCupWithProgress(
                            isCollected = index < safeCount,
                            isNext = index == safeCount,
                            index = index + 1
                        )
                    }
                }

                // Progress bar
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = Color(0xFFD4A574),
                    trackColor = Color.White.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
private fun CoffeeCupWithProgress(
    isCollected: Boolean,
    isNext: Boolean,
    index: Int
) {
    val scale by animateFloatAsState(
        targetValue = if (isNext) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
    )

    val color by androidx.compose.animation.animateColorAsState(
        targetValue = if (isCollected) Color(0xFFD4A574) else Color.White.copy(alpha = 0.3f),
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier.size(30.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow for collected cups
        if (isCollected) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .scale(scale)
                    .background(
                        color = Color(0xFFD4A574).copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            )
        }

        // Coffee cup icon
        Text(
            text = if (isCollected) "☕" else "○",
            fontSize = if (isNext) 20.sp else 16.sp,
            color = color,
            modifier = Modifier.align(Alignment.Center)
        )

        // Number indicator for empty cups
        if (!isCollected) {
            Text(
                text = "$index",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp)
            )
        }
    }
}