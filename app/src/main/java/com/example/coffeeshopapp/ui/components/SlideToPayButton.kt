package com.example.coffeeshopapp.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun SlideToPayButton(
    onPaid: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF2D4B73),
    thumbColor: Color = Color(0xFFD4A574),
    textColor: Color = Color.White.copy(alpha = 0.8f)
) {
    val haptic = LocalHapticFeedback.current

    val buttonHeight = 64.dp
    val thumbSize = 56.dp
    val maxOffsetPx = 600f

    var dragOffset by remember { mutableFloatStateOf(0f) }
    var hasPaid by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }

    val animatedOffset by animateFloatAsState(
        targetValue = dragOffset,
        animationSpec = tween(300),
        label = "slide"
    )

    Box(
        modifier = modifier
            .height(buttonHeight)
            .clip(CircleShape)
            .background(if (hasPaid) Color(0xFF4CAF50) else backgroundColor)
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        val progress = (animatedOffset / maxOffsetPx).coerceIn(0f, 1f)

        Text(
            text = if (hasPaid) "Processing..." else "Slide to Pay  >>>",
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(1f - progress)
        )

        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (dragOffset > maxOffsetPx * 0.85f && !hasPaid) {
                                hasPaid = true
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                showConfetti = true
                                onPaid()
                            }
                            dragOffset = 0f
                        }
                    ) { _, dragAmount ->
                        dragOffset =
                            (dragOffset + dragAmount).coerceIn(0f, maxOffsetPx)
                    }
                }
                .shadow(4.dp, CircleShape)
                .background(thumbColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.LocalCafe,
                contentDescription = null,
                tint = backgroundColor,
                modifier = Modifier.size(28.dp)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = backgroundColor.copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(start = 32.dp)
                    .size(20.dp)
            )
        }

        if (showConfetti) {
            ConfettiExplosion(
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun ConfettiExplosion(modifier: Modifier = Modifier) {
    val particles = remember { List(24) { ConfettiParticle() } }

    Box(modifier = modifier) {
        particles.forEach { particle ->
            val anim = remember { Animatable(0f) }

            LaunchedEffect(Unit) {
                anim.animateTo(1f, tween(600, easing = LinearEasing))
            }

            val pos = Offset(
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

private data class ConfettiParticle(
    val velocity: Offset = Offset(
        Random.nextFloat() * 200 - 100,
        Random.nextFloat() * -200
    ),
    val size: Float = Random.nextFloat() * 8 + 4,
    val color: Color = listOf(
        Color.Red, Color.Yellow, Color.Blue, Color.Green, Color.Magenta
    ).random(),
    val rotation: Float = Random.nextFloat() * 360
)
