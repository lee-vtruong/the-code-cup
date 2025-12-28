package com.example.coffeeshopapp.ui

import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.getValue
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeshopapp.model.Coffee
import com.example.coffeeshopapp.ui.components.SlideToPayButton

@Composable
fun CartScreen(
    cartItems: List<Pair<Coffee, Int>>,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onCheckout: () -> Unit,
    onDeleteItem: (Coffee) -> Unit
) {
    val totalAmount = cartItems.sumOf { it.first.price * it.second }

    val bgColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF121212) else Color(0xFFF8F4F0),
        animationSpec = tween(500)
    )

    val textColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color.White else Color(0xFF2D4B73),
        animationSpec = tween(500)
    )

    val cardColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White,
        animationSpec = tween(500)
    )

    val primaryColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFFBB86FC) else Color(0xFF2D4B73),
        animationSpec = tween(500)
    )

    Scaffold(
        topBar = {
            // FIXED HEADER
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .shadow(elevation = 4.dp),
                color = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                if (isDarkTheme) Color(0xFF2D4B73) else Color(0xFF2D4B73).copy(alpha = 0.1f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isDarkTheme) Color.White else Color(0xFF2D4B73)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "My Cart",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.weight(1f)
                    )

                    // Cart item count badge
                    if (cartItems.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(primaryColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${cartItems.size}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            // STICKY FOOTER
            if (cartItems.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 16.dp),
                    color = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Total price
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Total Items",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "${cartItems.sumOf { it.second }} items",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Total Price",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "$${String.format("%.2f", totalAmount)}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Slide to pay button
                        SlideToPayButton(
                            onPaid = onCheckout,
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = primaryColor,
                            thumbColor = if (isDarkTheme) Color(0xFF3E3E3E) else Color(0xFFD4A574)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Add more items button
                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = primaryColor
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = primaryColor.copy(alpha = 0.5f)
                            )
                        ) {
                            Text(
                                text = "Add more items",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(paddingValues)
        ) {
            if (cartItems.isEmpty()) {
                // Empty cart state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(
                                if (isDarkTheme) Color(0xFF2D4B73).copy(alpha = 0.2f)
                                else Color(0xFF2D4B73).copy(alpha = 0.1f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Empty Cart",
                            tint = primaryColor.copy(alpha = 0.5f),
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Your cart is empty",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Add some delicious coffee to get started!",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(200.dp)
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Browse Menu",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            } else {
                // Cart items list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(cartItems, key = { it.first.id }) { (coffee, qty) ->
                        CartItemRow(
                            coffee = coffee,
                            qty = qty,
                            cardColor = cardColor,
                            textColor = textColor,
                            primaryColor = primaryColor,
                            onDelete = { onDeleteItem(coffee) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    coffee: Coffee,
    qty: Int,
    cardColor: Color,
    textColor: Color,
    primaryColor: Color,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Coffee image
            Image(
                painter = painterResource(id = coffee.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Coffee details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = coffee.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = textColor,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .height(28.dp)
                            .background(primaryColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "x $qty",
                            fontWeight = FontWeight.Bold,
                            color = primaryColor,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "$${coffee.price} each",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$${String.format("%.2f", coffee.price * qty)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (textColor == Color.White) Color(0xFF00E5FF) else primaryColor
                )
            }

            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Red.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}