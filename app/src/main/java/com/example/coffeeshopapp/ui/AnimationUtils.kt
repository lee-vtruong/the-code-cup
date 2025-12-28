package com.example.coffeeshopapp.ui

import androidx.compose.ui.geometry.Offset

// Hàm tính toán đường cong Bezier bậc 2 (Parabol)
// t: Tiến độ (0.0 -> 1.0)
// p0: Điểm bắt đầu (Vị trí món hàng)
// p1: Điểm điều khiển (Đỉnh của đường cong, thường là ở giữa và cao hơn p0, p2)
// p2: Điểm kết thúc (Vị trí giỏ hàng)
fun calculateBezierPoint(t: Float, p0: Offset, p1: Offset, p2: Offset): Offset {
    val u = 1 - t
    val tt = t * t
    val uu = u * u

    // Công thức Bezier bậc 2: B(t) = (1-t)^2 * P0 + 2(1-t)t * P1 + t^2 * P2
    val x = uu * p0.x + 2 * u * t * p1.x + tt * p2.x
    val y = uu * p0.y + 2 * u * t * p1.y + tt * p2.y

    return Offset(x, y)
}