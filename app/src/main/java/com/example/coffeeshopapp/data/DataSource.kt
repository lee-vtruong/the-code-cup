package com.example.coffeeshopapp.data
import com.example.coffeeshopapp.model.Coffee
import com.example.coffeeshopapp.R

object DataSource {
    val coffeeMenu = listOf(
        Coffee(1, "Americano", "Đậm đà hương vị", 3.00, R.drawable.coffee1),
        Coffee(2, "Cappuccino", "Béo ngậy sữa tươi", 3.00, R.drawable.coffee2),
        Coffee(3, "Mocha", "Sự kết hợp Chocolate", 3.50, R.drawable.coffee3),
        Coffee(4, "Flat White", "Nhẹ nhàng tinh tế", 3.20, R.drawable.coffee4),

        // Espresso-based
        Coffee(5, "Espresso", "Đậm đặc nguyên bản", 2.50, R.drawable.coffee5),
        Coffee(6, "Latte", "Sữa mịn hòa quyện cà phê", 3.20, R.drawable.coffee6),
        Coffee(7, "Macchiato", "Espresso điểm sữa", 3.10, R.drawable.coffee7),

        // Special coffee
        Coffee(8, "Caramel Latte", "Ngọt ngào caramel", 3.80, R.drawable.coffee8),
        Coffee(9, "Hazelnut Latte", "Hương hạt dẻ thơm béo", 3.80, R.drawable.coffee9),
        Coffee(10, "Vanilla Latte", "Hương vanilla dịu nhẹ", 3.70, R.drawable.coffee10),

        // Iced & milk coffee
        Coffee(11, "Iced Coffee", "Mát lạnh sảng khoái", 2.80, R.drawable.coffee11),
        Coffee(12, "Vietnamese Milk Coffee", "Cà phê sữa truyền thống", 2.90, R.drawable.coffee12),

        // Non-caffeine / nhẹ
        Coffee(13, "Matcha Latte", "Trà xanh Nhật Bản", 3.50, R.drawable.coffee13),
        Coffee(14, "Chocolate", "Chocolate nóng ngọt ngào", 3.40, R.drawable.coffee14)
    )

}
