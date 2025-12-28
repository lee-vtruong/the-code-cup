package com.example.coffeeshopapp.ui.components

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun ParallaxBackground(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sensorManager =
        remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    var roll by remember { mutableFloatStateOf(0f) }
    var pitch by remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                roll = -event.values[0]
                pitch = event.values[1]
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    val animatedRoll by animateFloatAsState(roll, tween(120), label = "roll")
    val animatedPitch by animateFloatAsState(pitch, tween(120), label = "pitch")

    Box(modifier = modifier.fillMaxSize()) {

        /* ========= LAYER XA (nhiều – mờ – chậm) ========= */
        repeat(20) { i ->
            FloatingBean(
                xBase = Random.nextInt(0, 1080),
                yBase = Random.nextInt(0, 2200),
                size = Random.nextInt(20, 40),
                alpha = 0.08f,
                rotation = Random.nextFloat() * 360,
                xOffset = animatedRoll * 4f,
                yOffset = animatedPitch * 4f
            )
        }

        /* ========= LAYER TRUNG (vừa) ========= */
        repeat(15) {
            FloatingBean(
                xBase = Random.nextInt(0, 1080),
                yBase = Random.nextInt(0, 2200),
                size = Random.nextInt(40, 70),
                alpha = 0.12f,
                rotation = Random.nextFloat() * 360,
                xOffset = animatedRoll * 8f,
                yOffset = animatedPitch * 8f
            )
        }

        /* ========= LAYER GẦN (ít – to – nhanh) ========= */
        repeat(8) {
            FloatingBean(
                xBase = Random.nextInt(0, 1080),
                yBase = Random.nextInt(0, 2200),
                size = Random.nextInt(70, 110),
                alpha = 0.18f,
                rotation = Random.nextFloat() * 360,
                xOffset = animatedRoll * 14f,
                yOffset = animatedPitch * 14f
            )
        }
    }
}

@Composable
private fun FloatingBean(
    xBase: Int,
    yBase: Int,
    size: Int,
    alpha: Float,
    rotation: Float,
    xOffset: Float,
    yOffset: Float
) {
    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    (xBase + xOffset).roundToInt(),
                    (yBase + yOffset).roundToInt()
                )
            }
            .size(size.dp)
            .alpha(alpha)
            .rotate(rotation)
            .background(
                color = Color(0xFF6F4E37),
                shape = CircleShape
            )
    )
}
