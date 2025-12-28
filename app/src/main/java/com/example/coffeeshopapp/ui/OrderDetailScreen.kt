package com.example.coffeeshopapp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeshopapp.model.Order
import com.example.coffeeshopapp.model.OrderStatus
import com.example.coffeeshopapp.ui.components.AnimatedReceiptPrinter
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

@Composable
fun OrderDetailScreen(
    order: Order,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onCompleteOrder: () -> Unit // Callback xử lý khi bấm nút hoàn tất
) {
    val bgColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFEEEEEE)
    val textColor = if (isDarkTheme) Color.White else Color.Black

    // Format ngày tháng
    val dateText = remember(order.date) {
        try {
            SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.getDefault()).format(order.date)
        } catch (e: Exception) { "" }
    }

    // Giả lập Order ID
    val displayOrderId = remember { "#ORD-${order.hashCode().toString().takeLast(6)}" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
    ) {
        // --- Header Toolbar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = textColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Receipt Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- Nội dung hóa đơn (Scrollable nếu cần, nhưng ở đây ta để cố định) ---
        // Lưu ý: Nếu hóa đơn quá dài, bạn có thể bọc AnimatedReceiptPrinter trong Column + verticalScroll

        AnimatedReceiptPrinter {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                // 1. Shop Header
                Text(
                    text = "COFFEE SHOP APP",
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    letterSpacing = 1.sp
                )
                Text(
                    text = displayOrderId,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))
                DashedDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // 2. Danh sách món (Items)
                val items = order.itemsDescription.split(", ")
                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = item,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                DashedDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // 3. Tổng tiền (Total)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("TOTAL", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(
                        text = "$${String.format("%.2f", order.totalPrice)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Payment", fontSize = 12.sp, color = Color.Gray)
                    Text("Cash / Card", fontSize = 12.sp, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 4. Footer & Barcode
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Thank you for your order!", fontSize = 14.sp, color = Color.Gray)
                    Text(dateText, fontSize = 12.sp, color = Color.LightGray)

                    Spacer(modifier = Modifier.height(16.dp))
                    FakeBarcode()
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // --- Nút Action Button ở dưới cùng ---
        // Spacer này có tác dụng đẩy nút xuống đáy màn hình
        Spacer(modifier = Modifier.weight(1f))

        // Logic: Chỉ hiện nút "Hoàn thành" nếu đơn hàng đang ở trạng thái PREPARING
        if (order.status.equals("Preparing", ignoreCase = true)) {
            Button(
                onClick = { onCompleteOrder() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp), // Chiều cao chuẩn cho nút bấm thao tác chính
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50) // Màu xanh lá (Success)
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Mark as Completed",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        } else if (order.status == OrderStatus.COMPLETED.name) {
            // (Tùy chọn) Hiển thị trạng thái đã xong nếu muốn
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Order Completed ✅",
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// --- Helper Components ---

@Composable
fun DashedDivider(color: Color = Color.Gray) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect,
            strokeWidth = 2f
        )
    }
}

@Composable
fun FakeBarcode() {
    Row(
        modifier = Modifier
            .height(40.dp)
            .width(160.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val random = Random(123)
        repeat(30) {
            val width = random.nextInt(1, 6).dp
            val isSpace = random.nextBoolean() && random.nextBoolean()
            Box(
                modifier = Modifier
                    .width(width)
                    .fillMaxHeight()
                    .background(if (isSpace) Color.Transparent else Color.Black)
            )
            Spacer(modifier = Modifier.width(1.dp))
        }
    }
}