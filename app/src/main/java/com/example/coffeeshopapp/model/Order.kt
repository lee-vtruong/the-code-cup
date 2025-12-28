package com.example.coffeeshopapp.model

import java.util.Date

enum class OrderStatus {
    PREPARING, // Đang chuẩn bị (Ongoing)
    COMPLETED, // Đã hoàn thành (History)
    CANCELLED  // Đã hủy
}

data class Order(
    val id: String = "",
    val userId: String = "",
    val date: Date = Date(),
    val itemsDescription: String = "",
    val totalPrice: Double = 0.0,
    // Chuyển thành var để có thể thay đổi trạng thái
    // Mặc định là tên của Enum PREPARING
    var status: String = OrderStatus.PREPARING.name
)