package com.example.coffeeshopapp.ui

import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import kotlinx.coroutines.launch
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeshopapp.data.DataSource
import com.example.coffeeshopapp.model.Coffee
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    userName: String,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    loyaltyStamps: Int,
    onCoffeeClick: (Coffee) -> Unit,
    onCartClick: () -> Unit,
    onTabSelected: (String) -> Unit
) {
    val menu = DataSource.coffeeMenu
    val coffeeCategories = listOf("All", "Hot", "Iced", "Special")
    var selectedCategory by remember { mutableStateOf("All") }

    /* ================== ANIMATION STATE ================== */
    var cartPosition by remember { mutableStateOf(Offset.Zero) }
    var flyingItemStartPos by remember { mutableStateOf<Offset?>(null) }
    val flyProgress = remember { Animatable(0f) }
    val cartRotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    /* ================== THEME COLORS ================== */
    val backgroundColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF121212) else Color(0xFFF8F4F0),
        animationSpec = tween(500)
    )
    val mainTextColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color.White else Color(0xFF2D4B73)
    )
    val cardColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    )

    fun triggerFlyAnimation(startPos: Offset, onFinish: () -> Unit) {
        scope.launch {
            flyingItemStartPos = startPos
            flyProgress.snapTo(0f)

            flyProgress.animateTo(
                1f,
                tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )

            flyingItemStartPos = null
            onFinish()

            launch {
                cartRotation.animateTo(15f, spring(stiffness = Spring.StiffnessHigh))
                cartRotation.animateTo(-15f, spring(stiffness = Spring.StiffnessHigh))
                cartRotation.animateTo(8f, spring(stiffness = Spring.StiffnessHigh))
                cartRotation.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .statusBarsPadding()
        ) {

            /* ---------- HEADER IMPROVED ---------- */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Good morning,",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light
                        )
                        Text(
                            userName,
                            color = mainTextColor,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ThemeSwitcher(
                            isDarkTheme = isDarkTheme,
                            onToggle = onThemeToggle
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (isDarkTheme) Color(0xFF2D4B73) else Color(0xFF2D4B73).copy(alpha = 0.1f))
                                .onGloballyPositioned { coordinates ->
                                    val pos = coordinates.positionInRoot()
                                    val size = coordinates.size
                                    cartPosition = Offset(
                                        pos.x + size.width / 2,
                                        pos.y + size.height / 2
                                    )
                                }
                                .clickable { onCartClick() }
                                .padding(8.dp)
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = if (isDarkTheme) Color.White else Color(0xFF2D4B73),
                                modifier = Modifier
                                    .size(22.dp)
                                    .rotate(cartRotation.value)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (isDarkTheme) Color(0xFF2D4B73) else Color(0xFF2D4B73).copy(alpha = 0.1f))
                                .clickable { onTabSelected("Profile") }
                                .padding(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = if (isDarkTheme) Color.White else Color(0xFF2D4B73),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                /* ---------- GREETING CARD ---------- */
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) Color(0xFF2D4B73) else Color(0xFF2D4B73).copy(alpha = 0.95f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Start your day with",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 14.sp
                            )
                            Text(
                                "Perfect Coffee",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        // SỬA: Dùng Icons.Default.Coffee thay cho LocalCafe
                        Icon(
                            Icons.Default.Coffee,
                            contentDescription = "Coffee",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            /* ---------- LOYALTY CARD ---------- */
            LoyaltyCard(earnedCount = loyaltyStamps)

            /* ---------- CATEGORY FILTER ---------- */
            /* ---------- CATEGORY FILTER ---------- */
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(coffeeCategories) { category ->
                    val isSelected = (selectedCategory == category)

                    // --- GIẢI PHÁP THAY THẾ: TỰ CUSTOM CHIP ---
                    Surface(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(8.dp), // Bo góc giống FilterChip
                        color = if (isSelected) Color(0xFF2D4B73)
                        else if (isDarkTheme) Color(0xFF2C2C2C)
                        else Color(0xFFF0F0F0),
                        // Viền trong suốt hoặc có màu tùy ý
                        border = if (isSelected) null
                        else androidx.compose.foundation.BorderStroke(1.dp, Color.Transparent),
                        modifier = Modifier.height(32.dp) // Chiều cao chuẩn của Chip
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = category,
                                fontSize = 14.sp,
                                color = if (isSelected) Color.White
                                else if (isDarkTheme) Color.White
                                else Color.Black
                            )
                        }
                    }
                    // ------------------------------------------
                }
            }

            /* ---------- COFFEE LIST ---------- */
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(
                        if (isDarkTheme) Color(0xFF1A1A1A) else Color(0xFF2D4B73).copy(alpha = 0.05f)
                    )
            ) {
                Text(
                    "Featured Drinks",
                    color = if (isDarkTheme) Color(0xFFBB86FC) else Color(0xFF2D4B73),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(menu) { coffee ->
                        CoffeeItem(
                            coffee = coffee,
                            isDarkTheme = isDarkTheme,
                            onAddClick = { startPos ->
                                triggerFlyAnimation(startPos) {
                                    onCoffeeClick(coffee)
                                }
                            }
                        )
                    }
                }
            }
        }

        /* ---------- FLYING ANIMATION ---------- */
        flyingItemStartPos?.let { start ->
            val controlPoint = Offset(
                (start.x + cartPosition.x) / 2,
                minOf(start.y, cartPosition.y) - 300f
            )

            val currentPos = calculateBezierPoint(
                flyProgress.value,
                start,
                controlPoint,
                cartPosition
            )

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            currentPos.x.roundToInt(),
                            currentPos.y.roundToInt()
                        )
                    }
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (isDarkTheme) Color(0xFFBB86FC) else Color(0xFF6F4E37)
                    )
            ) {
                // SỬA: Dùng Coffee icon thay cho LocalCafe
                Icon(
                    Icons.Default.Coffee,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.Center)
                )
            }
        }

        // Bottom Navigation
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavigationBar(
                currentTab = "Home",
                onTabSelected = onTabSelected
            )
        }
    }
}