package com.example.coffeeshopapp.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ====== LIGHT THEME COLORS ======
val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2D4B73),        // Xanh đậm chính
    secondary = Color(0xFFD4A574),      // Nâu cà phê
    tertiary = Color(0xFF6F4E37),       // Nâu đậm
    background = Color(0xFFF8F4F0),     // Nền beige nhạt
    surface = Color(0xFFFFFFFF),        // Bề mặt trắng
    surfaceVariant = Color(0xFFF0F0F0), // Bề mặt biến thể
    onPrimary = Color.White,            // Trên màu chính
    onSecondary = Color.Black,          // Trên màu phụ
    onBackground = Color(0xFF1C1B1F),   // Trên nền
    onSurface = Color(0xFF1C1B1F),      // Trên bề mặt

    // Các màu bổ sung
    primaryContainer = Color(0xFF2D4B73).copy(alpha = 0.1f),
    secondaryContainer = Color(0xFFD4A574).copy(alpha = 0.1f),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFC4C7C5)
)

// ====== DARK THEME COLORS (IMPROVED) ======
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),        // Tím neon (giữ nguyên)
    secondary = Color(0xFF03DAC6),      // Xanh ngọc sáng
    tertiary = Color(0xFFD4A574),       // Nâu cà phê (giữ nguyên)

    // Nền tối nhưng không quá đen
    background = Color(0xFF121212),     // Nền đen nhẹ
    surface = Color(0xFF1E1E1E),        // Bề mặt xám đậm
    surfaceVariant = Color(0xFF2D2D2D), // Bề mặt biến thể

    // Màu chữ sáng nhưng không chói
    onPrimary = Color.Black,            // Trên màu chính
    onSecondary = Color.Black,          // Trên màu phụ
    onBackground = Color(0xFFE1E1E1),   // Trên nền (xám sáng)
    onSurface = Color(0xFFE1E1E1),      // Trên bề mặt (xám sáng)

    // Các màu bổ sung cho dark theme
    primaryContainer = Color(0xFF3700B3).copy(alpha = 0.3f),
    secondaryContainer = Color(0xFF03DAC6).copy(alpha = 0.2f),
    error = Color(0xFFCF6679),
    errorContainer = Color(0xFFB00020),
    outline = Color(0xFF8A8D8F),
    outlineVariant = Color(0xFF3A3A3C)
)

// ====== CUSTOM COLORS FOR COFFEE SHOP ======
object CoffeeColors {
    // Light theme
    val Light = object {
        val coffeeBrown = Color(0xFF6F4E37)
        val lightBrown = Color(0xFFD4A574)
        val cream = Color(0xFFFFF8E1)
        val darkBlue = Color(0xFF2D4B73)
        val successGreen = Color(0xFF4CAF50)
        val warningOrange = Color(0xFFFF9800)
    }

    // Dark theme
    val Dark = object {
        val coffeeBrown = Color(0xFFD4A574)  // Giữ nâu sáng cho dark theme
        val lightBrown = Color(0xFF8B4513)
        val cream = Color(0xFF2D2D2D)
        val accentPurple = Color(0xFFBB86FC)
        val accentCyan = Color(0xFF03DAC6)
        val successGreen = Color(0xFF66BB6A)
        val warningOrange = Color(0xFFFFB74D)
    }
}