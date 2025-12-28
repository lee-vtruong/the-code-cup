package com.example.coffeeshopapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Component tờ giấy hóa đơn tĩnh (Không có animation)
 */
@Composable
fun ReceiptCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF8F8F0), // Màu trắng ngà hơi cũ kỹ
    content: @Composable ColumnScope.() -> Unit
) {
    // Tạo một cái bóng nhẹ cho cảm giác 3D
    Box(
        modifier = modifier
            .shadow(4.dp, ReceiptShape()) // Bóng đổ theo hình răng cưa
            .clip(ReceiptShape())         // Cắt nội dung theo hình răng cưa
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Padding nội dung bên trong
                .padding(bottom = 8.dp) // Tránh nội dung đè lên răng cưa
        ) {
            content()
        }
    }
}

/**
 * Component "Máy in" có hiệu ứng chạy giấy ra
 */
@Composable
fun AnimatedReceiptPrinter(
    modifier: Modifier = Modifier,
    printDuration: Int = 1500, // Thời gian in (ms)
    content: @Composable ColumnScope.() -> Unit
) {
    // Biến trạng thái để kích hoạt animation khi mới mở màn hình
    var visible by remember { mutableStateOf(false) }

    // Kích hoạt ngay khi component được vẽ
    LaunchedEffect(Unit) {
        visible = true
        // Gợi ý: Nếu muốn thêm âm thanh "xẹt xẹt", hãy gọi SoundPool hoặc MediaPlayer ở đây.
    }

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        // --- KHE MÁY IN (Trang trí) ---
        // Một cái hộp màu tối ở trên cùng để giả lập khe giấy chui ra
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(12.dp)
                .background(Color(0xFF2C2C2C), shape = MaterialTheme.shapes.small)
        )

        // --- PHẦN GIẤY IN RA ---
        AnimatedVisibility(
            visible = visible,
            // Hiệu ứng mở rộng từ trên xuống (expandVertically + Top)
            // Kết hợp fadeIn để mượt hơn
            enter = expandVertically(
                animationSpec = tween(durationMillis = printDuration),
                expandFrom = Alignment.Top
            ) + fadeIn(animationSpec = tween(durationMillis = printDuration / 2)),
            modifier = Modifier.fillMaxWidth(0.9f) // Giấy nhỏ hơn khe máy một chút
        ) {
            // Nội dung chính là tờ hóa đơn
            ReceiptCard(
                modifier = Modifier.padding(top = 0.dp) // Dính sát vào khe máy in
            ) {
                content()
            }
        }
    }
}