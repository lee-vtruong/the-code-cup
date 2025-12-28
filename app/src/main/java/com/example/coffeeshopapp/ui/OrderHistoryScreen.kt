package com.example.coffeeshopapp.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // Đã bao gồm Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// XÓA: import androidx.compose.material3.OutlinedButtonDefaults (Class này không tồn tại)
// XÓA: import androidx.compose.foundation.layout.Arrangement (Đã import ở dòng layout.*)
import com.example.coffeeshopapp.model.Order
import com.example.coffeeshopapp.model.OrderStatus
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderHistoryScreen(
    isDarkTheme: Boolean,
    orders: List<Order>,
    onTabSelected: (String) -> Unit,
    onOrderClick: (Order) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val bgColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF121212) else Color(0xFFF8F4F0),
        animationSpec = tween(500), label = "bgColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color.White else Color(0xFF2D4B73),
        animationSpec = tween(500), label = "textColor"
    )

    val cardColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White,
        animationSpec = tween(500), label = "cardColor"
    )

    val primaryColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFFBB86FC) else Color(0xFF2D4B73),
        animationSpec = tween(500), label = "primaryColor"
    )

    val secondaryColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFFD4A574) else Color(0xFFD4A574),
        animationSpec = tween(500), label = "secondaryColor"
    )

    // Filter orders by status
    val ongoingOrders = orders.filter {
        it.status.equals(OrderStatus.PREPARING.name, ignoreCase = true)
    }

    val historyOrders = orders.filter {
        it.status.equals(OrderStatus.COMPLETED.name, ignoreCase = true) ||
                it.status.equals(OrderStatus.CANCELLED.name, ignoreCase = true)
    }

    val displayList = if (selectedTab == 0) ongoingOrders else historyOrders

    Scaffold(
        topBar = {
            // Order History Header
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .shadow(elevation = 4.dp),
                color = cardColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Order History",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    // Stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OrderStat(
                            title = "Active",
                            value = ongoingOrders.size.toString(),
                            icon = Icons.Default.HourglassEmpty,
                            color = primaryColor,
                            cardColor = cardColor
                        )

                        OrderStat(
                            title = "Completed",
                            value = historyOrders.count {
                                it.status.equals(OrderStatus.COMPLETED.name, ignoreCase = true)
                            }.toString(),
                            icon = Icons.Default.CheckCircle,
                            color = Color(0xFF4CAF50),
                            cardColor = cardColor
                        )

                        OrderStat(
                            title = "Total",
                            value = orders.size.toString(),
                            icon = Icons.Default.Receipt,
                            color = secondaryColor,
                            cardColor = cardColor
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Bottom Navigation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor)
            ) {
                // Lưu ý: Đảm bảo BottomNavigationBar đã được định nghĩa ở nơi khác hoặc import đúng
                // Nếu chưa có, bạn cần comment dòng này lại hoặc tạo Composable đó
                BottomNavigationBar(
                     currentTab = "Orders",
                     onTabSelected = onTabSelected
                 )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Tabs with indicator
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 2.dp),
                    color = cardColor
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        OrderTabButton(
                            text = "On Going",
                            count = ongoingOrders.size,
                            isSelected = selectedTab == 0,
                            primaryColor = primaryColor,
                            textColor = textColor,
                            modifier = Modifier.weight(1f)
                        ) { selectedTab = 0 }

                        Spacer(modifier = Modifier.width(16.dp))

                        OrderTabButton(
                            text = "History",
                            count = historyOrders.size,
                            isSelected = selectedTab == 1,
                            primaryColor = primaryColor,
                            textColor = textColor,
                            modifier = Modifier.weight(1f)
                        ) { selectedTab = 1 }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Order List
                if (displayList.isEmpty()) {
                    // Empty state
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
                                .background(primaryColor.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (selectedTab == 0) Icons.Default.HourglassEmpty else Icons.Default.History,
                                contentDescription = "Empty",
                                tint = primaryColor,
                                modifier = Modifier.size(60.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = if (selectedTab == 0) "No active orders" else "No order history",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (selectedTab == 0)
                                "Your active orders will appear here"
                            else
                                "Your completed orders will appear here",
                            fontSize = 16.sp,
                            color = textColor.copy(alpha = 0.6f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        if (selectedTab == 0) {
                            Button(
                                onClick = { onTabSelected("Home") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(50.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.LocalCafe,
                                        contentDescription = "Order",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Order Now",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        // SỬA LỖI 3: PaddingValues
                        // vertical = 16.dp nghĩa là top=16, bottom=16.
                        // Bạn muốn bottom=80, nên phải tách ra:
                        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
                    ) {
                        items(displayList, key = { it.id }) { order ->
                            OrderItemCard(
                                order = order,
                                isDarkTheme = isDarkTheme,
                                textColor = textColor,
                                primaryColor = primaryColor,
                                secondaryColor = secondaryColor,
                                onClick = { onOrderClick(order) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/* ---------- ORDER STAT COMPONENT ---------- */

@Composable
private fun OrderStat(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    cardColor: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Text(
                text = title,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

/* ---------- ORDER TAB BUTTON ---------- */

@Composable
private fun OrderTabButton(
    text: String,
    count: Int,
    isSelected: Boolean,
    primaryColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) primaryColor.copy(alpha = 0.1f) else Color.Transparent
            )
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) primaryColor else textColor.copy(alpha = 0.7f)
            )

            if (count > 0) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            if (isSelected) primaryColor else Color.Gray.copy(alpha = 0.2f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$count",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else textColor.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Selection indicator
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(2.dp))
                    .background(primaryColor)
            )
        }
    }
}

/* ---------- ORDER ITEM CARD ---------- */

@Composable
fun OrderItemCard(
    order: Order,
    isDarkTheme: Boolean,
    textColor: Color,
    primaryColor: Color,
    secondaryColor: Color,
    onClick: () -> Unit
) {
    val dateText = remember(order.date) {
        try {
            SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.getDefault()).format(order.date)
        } catch (e: Exception) {
            order.date.toString()
        }
    }

    val cardColor = if (isDarkTheme) Color(0xFF2C2C2C) else Color(0xFFF9F9F9)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with date and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "Time",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        dateText,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Status badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(getStatusColor(order.status).copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = order.status.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = getStatusColor(order.status)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Order items
            Text(
                text = order.itemsDescription,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = textColor,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer with price and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Total Price",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "$${String.format("%.2f", order.totalPrice)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = primaryColor
                    )
                }

                // Action buttons based on status
                when (order.status.uppercase()) {
                    "PREPARING" -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { /* Track order */ },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = primaryColor
                                ),
                                // SỬA LỖI 4: Dùng BorderStroke trực tiếp
                                border = BorderStroke(width = 1.dp, color = primaryColor),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Track", fontSize = 12.sp)
                            }
                            Button(
                                onClick = { /* View details */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                ),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Details", fontSize = 12.sp)
                            }
                        }
                    }
                    else -> {
                        Button(
                            onClick = { /* Reorder */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = secondaryColor
                            ),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Default.Replay,
                                    contentDescription = "Reorder",
                                    modifier = Modifier.size(14.dp)
                                )
                                Text("Reorder", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ---------- HELPER FUNCTIONS ---------- */

@Composable
private fun getStatusColor(status: String): Color {
    return when (status.uppercase()) {
        "PREPARING" -> Color(0xFFFF9800) // Orange
        "COMPLETED" -> Color(0xFF4CAF50) // Green
        "CANCELLED" -> Color(0xFFF44336) // Red
        else -> Color.Gray
    }
}