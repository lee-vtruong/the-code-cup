package com.example.coffeeshopapp.model

data class UserData(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val points: Int = 0,         // Lưu điểm tích lũy
    val stamps: Int = 0          // Lưu số ly tích lũy (Loyalty)
)