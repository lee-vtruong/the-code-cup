package com.example.coffeeshopapp.ui

import com.example.coffeeshopapp.R
import androidx.compose.foundation.background
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val scale = remember { Animatable(0.8f) }
    val rotation = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        // Sequence of animations
        launch {
            alpha.animateTo(1f, tween(800))
        }
        launch {
            scale.animateTo(1.1f, tween(1000, easing = FastOutSlowInEasing))
            scale.animateTo(1f, tween(400))
        }
        launch {
            rotation.animateTo(360f, tween(1200, easing = FastOutSlowInEasing))
        }
        delay(3000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFF4EDE0),
                        Color(0xFFF4EEE0)
                    ),
                    center = Offset(0.5f, 0.5f),
                    radius = 1000f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // --- THAY THẾ LOGO Ở ĐÂY ---
        Image(
            // QUAN TRỌNG: Thay 'ten_file_logo_cua_ban' bằng tên file ảnh của bạn trong thư mục drawable
            painter = painterResource(id = R.drawable.img_2),
            contentDescription = "App Logo",
            // Điều chỉnh kích thước logo cho phù hợp
            modifier = Modifier.size(250.dp)
        )
    }
}