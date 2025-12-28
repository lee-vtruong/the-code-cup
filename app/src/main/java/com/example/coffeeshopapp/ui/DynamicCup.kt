package com.example.coffeeshopapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text // <--- Dùng để viết Emoji
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // <--- Sửa lỗi 'clip'
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DynamicCup(
    size: String,
    iceLevel: String,
    sugarLevel: String,
    coffeeColor: Color = Color(0xFF6F4E37)
) {
    val targetScale = when (size) {
        "S" -> 0.8f
        "M" -> 1.0f
        "L" -> 1.2f
        else -> 1.0f
    }

    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(200.dp)
            .scale(animatedScale),
        contentAlignment = Alignment.BottomCenter
    ) {
        // LAYER 1: VỎ LY
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .background(Color.White.copy(alpha = 0.5f))
                .border(2.dp, Color.Gray, RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
        )

        // LAYER 2: NƯỚC CÀ PHÊ
        Box(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .fillMaxHeight(0.85f)
                .padding(bottom = 4.dp)
                .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                .background(coffeeColor)
        )

        // LAYER 3: ĐÁ (Ice)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .fillMaxHeight(0.85f)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            AnimatedVisibility(
                visible = iceLevel != "None",
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn()
            ) {
                val iceCount = if (iceLevel == "Full") 6 else 3
                IceCubesVisual(count = iceCount)
            }
        }

        // LAYER 4: ĐƯỜNG (Sugar)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .fillMaxHeight(0.85f)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = sugarLevel != "None" && sugarLevel != "0%",
                enter = slideInVertically(initialOffsetY = { -it / 2 }) + fadeIn()
            ) {
                val sugarDensity = if (sugarLevel == "Normal") 20 else 10
                SugarVisual(density = sugarDensity)
            }
        }
    }
}

@Composable
fun IceCubesVisual(count: Int) {
    // Sắp xếp đá: Dùng Emoji bông tuyết thay cho icon bị lỗi
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(count / 2) {
                Text("❄️", fontSize = 20.sp)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(count - (count / 2)) {
                Text("❄️", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun SugarVisual(density: Int) {
    // Hạt đường: Vẽ các chấm tròn nhỏ màu trắng thay vì dùng icon Grain bị lỗi
    Box(modifier = Modifier.fillMaxSize()) {
        repeat(density) {
            Box(
                modifier = Modifier
                    .size((4..6).random().dp) // Kích thước hạt đường nhỏ
                    .offset(
                        x = (0..100).random().dp,
                        y = (0..120).random().dp
                    )
                    .background(Color.White.copy(alpha = 0.8f), CircleShape) // Vẽ hình tròn trắng
            )
        }
    }
}