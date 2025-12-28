package com.example.coffeeshopapp.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke // Cần import cái này
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.coffeeshopapp.model.Order
import java.text.SimpleDateFormat
import java.util.*
import com.example.coffeeshopapp.R // Đảm bảo import R của project bạn

@Composable
fun RewardsScreen(
    isDarkTheme: Boolean,
    currentPoints: Int,
    loyaltyStamps: Int,
    orderHistory: List<Order>,
    onTabSelected: (String) -> Unit
) {
    // SỬA: Dùng MaterialTheme colors thay vì màu cứng
    val backgroundColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.background,
        animationSpec = tween(500), label = "bg"
    )

    val textColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.onBackground,
        animationSpec = tween(500), label = "text"
    )

    val cardColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.surface,
        animationSpec = tween(500), label = "card"
    )

    val surfaceVariantColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(500), label = "surfaceVariant"
    )

    val primaryColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.primary,
        animationSpec = tween(500), label = "primary"
    )

    val secondaryColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.secondary,
        animationSpec = tween(500), label = "secondary"
    )

    val tertiaryColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.tertiary,
        animationSpec = tween(500), label = "tertiary"
    )

    var isRedeeming by remember { mutableStateOf(false) }
    var selectedReward by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            // Header with back button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .shadow(elevation = 4.dp),
                color = cardColor
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isRedeeming) {
                        IconButton(
                            onClick = { isRedeeming = false },
                            modifier = Modifier
                                .size(44.dp)
                                .align(Alignment.CenterStart)
                                .background(primaryColor.copy(alpha = 0.1f), CircleShape)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = primaryColor
                            )
                        }
                    }

                    Text(
                        text = if (isRedeeming) "Redeem Rewards" else "My Rewards",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // Points indicator
                    if (!isRedeeming) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clip(RoundedCornerShape(12.dp))
                                .background(primaryColor.copy(alpha = 0.1f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Loyalty,
                                    contentDescription = "Points",
                                    tint = primaryColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$currentPoints pts",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryColor
                                )
                            }
                        }
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
                BottomNavigationBar(
                    currentTab = "Rewards",
                    onTabSelected = onTabSelected
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
        ) {
            if (!isRedeeming) {
                // ========== REWARDS DASHBOARD ==========

                // Loyalty Card Section
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Loyalty Program",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Giả định bạn đã có LoyaltyCard component.
                    // Nếu chưa có, hãy tạo một placeholder tạm thời hoặc import nó.
                    LoyaltyCard(earnedCount = loyaltyStamps)
                }

                // Points Card
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = primaryColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(140.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Available Points",
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "$currentPoints",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "= ${currentPoints / 100} free drinks",
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Button(
                            onClick = { isRedeeming = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                contentColor = primaryColor
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Default.CardGiftcard,
                                    contentDescription = "Redeem",
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    "Redeem Now",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Recent Rewards History
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Recent Rewards",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = textColor
                        )

                        TextButton(
                            onClick = { /* View all */ }
                        ) {
                            Text(
                                "View All",
                                color = primaryColor,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (orderHistory.isEmpty()) {
                        // Empty state
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(primaryColor.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.History,
                                    contentDescription = "No History",
                                    tint = primaryColor,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No rewards yet",
                                color = textColor.copy(alpha = 0.6f),
                                fontSize = 16.sp
                            )
                            Text(
                                "Make your first purchase to earn points!",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(orderHistory.take(5)) { order ->
                                HistoryRewardItem(
                                    order = order,
                                    textColor = textColor,
                                    primaryColor = primaryColor
                                )
                            }
                        }
                    }
                }
            } else {
                // ========== REDEEM SECTION ==========

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Redeemable drinks header
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = cardColor
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                "Redeem Drinks",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Exchange your points for free drinks",
                                fontSize = 14.sp,
                                color = textColor.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Redeemable items grid
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(getRedeemableItems()) { item ->
                            RedeemItemCard(
                                item = item,
                                currentPoints = currentPoints,
                                textColor = textColor,
                                primaryColor = primaryColor,
                                secondaryColor = tertiaryColor,
                                cardColor = cardColor,
                                surfaceVariantColor = surfaceVariantColor,
                                isSelected = selectedReward == item.name,
                                onSelect = { selectedReward = item.name },
                                onRedeem = {
                                    // Handle redeem logic
                                    selectedReward = null
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------
// SỬA LỖI 1: Data class không được chứa Composable function
// --------------------------------------------------------
private data class RedeemableItem(
    val name: String,
    val description: String,
    val points: Int,
    val imageRes: Int? = null // Lưu ID resource thay vì hashCode
)

@Composable
private fun RedeemItemCard(
    item: RedeemableItem,
    currentPoints: Int,
    textColor: Color,
    primaryColor: Color,
    secondaryColor: Color,
    cardColor: Color,
    surfaceVariantColor: Color,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onRedeem: () -> Unit
) {
    val canAfford = currentPoints >= item.points

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) primaryColor.copy(alpha = 0.1f) else cardColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (canAfford) onSelect() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        // SỬA LỖI 2: Dùng BorderStroke trực tiếp, không dùng copy
        border = if (isSelected) {
            BorderStroke(width = 2.dp, color = primaryColor)
        } else {
            BorderStroke(width = 1.dp, color = Color.Transparent)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Drink image/icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(secondaryColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Nếu item có ảnh thật thì dùng Image, không thì dùng Icon mặc định
                if (item.imageRes != null && item.imageRes != 0) {
                    // Nếu có ảnh resource, bạn có thể uncomment dòng này
                    // Image(painter = painterResource(id = item.imageRes), ...)
                    // Ở đây demo dùng Icon cho tiện
                    Icon(
                        Icons.Default.LocalCafe,
                        contentDescription = item.name,
                        tint = secondaryColor,
                        modifier = Modifier.size(30.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.LocalCafe,
                        contentDescription = item.name,
                        tint = secondaryColor,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = textColor
                )
                Text(
                    item.description,
                    fontSize = 12.sp,
                    color = textColor.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    "Valid for 30 days",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    "${item.points} pts",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (canAfford) primaryColor else MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onRedeem,
                    enabled = canAfford && isSelected,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canAfford) primaryColor else surfaceVariantColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        "Redeem",
                        fontSize = 12.sp,
                        color = if (canAfford) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/* ---------- HISTORY REWARD ITEM ---------- */

@Composable
private fun HistoryRewardItem(
    order: Order,
    textColor: Color,
    primaryColor: Color
) {
    val dateText = remember(order.date) {
        try {
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(order.date)
        } catch (e: Exception) { "" }
    }

    val earnedPoints = order.totalPrice.toInt()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    order.itemsDescription,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = textColor,
                    maxLines = 1
                )
                Text(
                    dateText,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(primaryColor.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    "+ $earnedPoints Pts",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = primaryColor
                )
            }
        }
    }
}

private fun getRedeemableItems(): List<RedeemableItem> {
    // SỬA: Loại bỏ lời gọi painterResource() ở đây
    return listOf(
        RedeemableItem(
            name = "Cafe Latte",
            description = "Medium size, any milk",
            points = 1340,
            imageRes = R.drawable.coffee1 // Đảm bảo bạn có hình này hoặc để null
        ),
        RedeemableItem(
            name = "Flat White",
            description = "Strong and creamy",
            points = 1340,
            imageRes = R.drawable.coffee1
        ),
        RedeemableItem(
            name = "Cappuccino",
            description = "Classic Italian style",
            points = 1340
        ),
        RedeemableItem(
            name = "Espresso",
            description = "Single shot, bold flavor",
            points = 1000
        ),
        RedeemableItem(
            name = "Iced Americano",
            description = "Perfect for hot days",
            points = 1200
        ),
        RedeemableItem(
            name = "Mocha",
            description = "Chocolate coffee delight",
            points = 1500
        )
    )
}