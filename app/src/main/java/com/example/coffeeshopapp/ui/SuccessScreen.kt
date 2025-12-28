package com.example.coffeeshopapp.ui

import androidx.compose.animation.core.*
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SuccessScreen(onGoHome: () -> Unit) {
    // Animation states
    val scale = remember { Animatable(0.8f) }
    val rotation = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    // Confetti particles
    val particles = remember { List(15) { ConfettiParticle() } }
    var showConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Start animations
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

        // Show confetti after delay
        delay(500)
        showConfetti = true

        // Button pulse animation
        while (true) {
            buttonScale.animateTo(1.05f, tween(800, easing = FastOutSlowInEasing))
            buttonScale.animateTo(1f, tween(800, easing = FastOutSlowInEasing))
            delay(2000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF2D4B73),
                        Color(0xFF1A1A2E)
                    ),
                    center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
                    radius = 1000f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Confetti effect
        if (showConfetti) {
            Box(modifier = Modifier.fillMaxSize()) {
                particles.forEachIndexed { index, particle ->
                    val anim = remember { Animatable(0f) }

                    LaunchedEffect(key1 = showConfetti) {
                        delay(index * 100L) // Stagger animation
                        anim.animateTo(1f, tween(1000, easing = LinearEasing))
                    }

                    val pos = androidx.compose.ui.geometry.Offset(
                        particle.velocity.x * anim.value,
                        particle.velocity.y * anim.value
                    )

                    Box(
                        modifier = Modifier
                            .offset(pos.x.dp, pos.y.dp)
                            .size(particle.size.dp)
                            .rotate(particle.rotation * anim.value)
                            .alpha(1f - anim.value)
                            .background(particle.color, CircleShape)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success icon with animation
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .scale(scale.value)
                    .rotate(rotation.value)
                    .alpha(alpha.value)
                    .shadow(32.dp, shape = CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF4CAF50),
                                Color(0xFF2E7D32)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color.White,
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Success message
            Text(
                text = "Order Confirmed!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your coffee is being prepared with love",
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Estimated ready in: 5-10 minutes",
                fontSize = 14.sp,
                color = Color(0xFFD4A574),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Order details card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Order #",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "#${(1000..9999).random()}",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pickup Time",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "10-15 min",
                            color = Color(0xFFD4A574),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rewards Earned",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "+25 pts",
                            color = Color(0xFF4CAF50),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Action button with pulse animation
            Button(
                onClick = onGoHome,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD4A574)
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .scale(buttonScale.value),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 16.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalCafe,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        text = "Track My Order",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Secondary button
            TextButton(
                onClick = onGoHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Back to Home",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // Coffee beans floating animation
        FloatingCoffeeBeans()
    }
}

@Composable
private fun FloatingCoffeeBeans() {
    val infiniteTransition = rememberInfiniteTransition()

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Floating coffee beans
        repeat(8) { i ->
            val offset = when (i % 4) {
                0 -> float1
                1 -> float2
                2 -> -float1
                else -> -float2
            }

            Box(
                modifier = Modifier
                    .offset(
                        x = (i * 50).dp + (offset * 0.5f).dp,
                        y = (i * 40).dp + (offset * 0.3f).dp
                    )
                    .size((20 + i * 5).dp)
                    .background(Color(0xFF6F4E37).copy(alpha = 0.1f), CircleShape)
            )
        }
    }
}

private data class ConfettiParticle(
    val velocity: androidx.compose.ui.geometry.Offset = androidx.compose.ui.geometry.Offset(
        kotlin.random.Random.nextFloat() * 300 - 150,
        kotlin.random.Random.nextFloat() * -200
    ),
    val size: Float = kotlin.random.Random.nextFloat() * 10 + 5,
    val color: Color = listOf(
        Color(0xFFFF9800), // Orange
        Color(0xFF4CAF50), // Green
        Color(0xFF2196F3), // Blue
        Color(0xFF9C27B0), // Purple
        Color(0xFFFFEB3B)  // Yellow
    ).random(),
    val rotation: Float = kotlin.random.Random.nextFloat() * 360
)