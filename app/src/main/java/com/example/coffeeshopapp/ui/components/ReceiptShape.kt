package com.example.coffeeshopapp.ui.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

// Class vẽ hình dạng tờ hóa đơn
class ReceiptShape(
    private val toothWidth: Dp = 16.dp, // Độ rộng 1 cái răng cưa
    private val toothHeight: Dp = 8.dp  // Độ sâu của răng cưa
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        // Đổi Dp sang dơn vị Pixel
        val tw = with(density) { toothWidth.toPx() }
        val th = with(density) { toothHeight.toPx() }

        val path = Path().apply {
            // 1. Bắt đầu từ góc trên trái (0,0)
            moveTo(0f, 0f)
            // 2. Vẽ sang góc trên phải
            lineTo(size.width, 0f)
            // 3. Vẽ xuống góc dưới phải (Điểm bắt đầu răng cưa)
            lineTo(size.width, size.height)

            // 4. Vẽ RĂNG CƯA (Từ phải qua trái)
            // Tính số lượng răng cưa dựa trên chiều rộng
            val numberOfTeeth = (size.width / tw).toInt()

            for (i in 0 until numberOfTeeth) {
                val currentX = size.width - (i * tw)
                // Vẽ đường chéo lên (đỉnh răng)
                lineTo(currentX - tw / 2, size.height - th)
                // Vẽ đường chéo xuống (đáy răng tiếp theo)
                lineTo(currentX - tw, size.height)
            }

            // 5. Đảm bảo nối về góc dưới trái và khép vòng
            lineTo(0f, size.height)
            close()
        }

        return Outline.Generic(path)
    }
}