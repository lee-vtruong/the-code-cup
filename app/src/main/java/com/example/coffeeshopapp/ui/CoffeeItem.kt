package com.example.coffeeshopapp.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeshopapp.model.Coffee

@Composable
fun CoffeeItem(
    coffee: Coffee,
    isDarkTheme: Boolean,
    onAddClick: (Offset) -> Unit
) {
    var itemCenterPosition by remember { mutableStateOf(Offset.Zero) }
    var isFavorite by remember { mutableStateOf(false) }

    // Theme colors with animation
    val cardBgColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White,
        animationSpec = tween(300)
    )

    val textColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color.White else Color(0xFF2D4B73),
        animationSpec = tween(300)
    )

    val priceColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF006400),
        animationSpec = tween(300)
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(250.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBgColor
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            // Favorite button
            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier
                    .padding(8.dp)
                    .size(36.dp)
                    .background(
                        if (isFavorite) Color.Red.copy(alpha = 0.1f)
                        else Color.Gray.copy(alpha = 0.1f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Image with gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                ) {
                    Image(
                        painter = painterResource(id = coffee.imageRes),
                        contentDescription = coffee.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                            .onGloballyPositioned { coordinates ->
                                val pos = coordinates.positionInRoot()
                                val size = coordinates.size
                                itemCenterPosition = Offset(
                                    pos.x + size.width / 2f,
                                    pos.y + size.height / 2f
                                )
                            }
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.2f)
                                    ),
                                    startY = 0f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Coffee name
                Text(
                    text = coffee.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Price
                Text(
                    text = "$${coffee.price}",
                    fontWeight = FontWeight.ExtraBold,
                    color = priceColor,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Add to cart button
                Button(
                    onClick = { onAddClick(itemCenterPosition) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkTheme)
                            Color(0xFFBB86FC)
                        else
                            Color(0xFF2D4B73)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(36.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Text(
                        text = "Add to Cart",
                        color = if (isDarkTheme) Color.Black else Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}