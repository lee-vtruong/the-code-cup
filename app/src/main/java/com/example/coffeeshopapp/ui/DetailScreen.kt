package com.example.coffeeshopapp.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeshopapp.model.Coffee
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    coffee: Coffee,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onAddToCart: (Coffee, Int, String, String, String) -> Unit
) {
    var quantity by remember { mutableStateOf(1) }
    var selectedSize by remember { mutableStateOf("M") }
    var selectedType by remember { mutableStateOf("Ice") }
    var selectedIceLevel by remember { mutableStateOf("Full") }
    var selectedSugarLevel by remember { mutableStateOf("Normal") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val bgColor by animateColorAsState(if (isDarkTheme) Color(0xFF121212) else Color.White)
    val textColor by animateColorAsState(if (isDarkTheme) Color.White else Color.Black)
    val boxColor by animateColorAsState(if (isDarkTheme) Color(0xFF2C2C2C) else Color(0xFFF5F5F5))
    val primaryColor by animateColorAsState(if (isDarkTheme) Color(0xFFBB86FC) else Color(0xFF2D4B73))
    val secondaryColor by animateColorAsState(Color(0xFFD4A574))

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = bgColor,

        /* ================= TOP BAR ================= */
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                primaryColor.copy(alpha = 0.9f),
                                primaryColor.copy(alpha = 0.6f)
                            )
                        )
                    )
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .align(Alignment.Center),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "Coffee Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = Color.White
                        )
                    }
                }
            }
        },

        /* ================= BOTTOM BAR ================= */
        bottomBar = {
            DetailScreenFooter(
                totalPrice = coffee.price * quantity,
                textColor = textColor,
                primaryColor = primaryColor,
                isDarkTheme = isDarkTheme
            ) {
                onAddToCart(
                    coffee,
                    quantity,
                    selectedSize,
                    selectedIceLevel,
                    selectedSugarLevel
                )
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Added ${coffee.name} to cart!"
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            /* ---------- CUP ---------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp), // Chiều cao tổng của khu vực chứa ly
                contentAlignment = Alignment.Center // Căn giữa khung tròn trong khu vực này
            ) {
                // Khung tròn/nền chứa cái ly
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .shadow(16.dp, CircleShape)
                        .background(if (isDarkTheme) Color(0xFF333333) else Color.White, CircleShape), // Thêm nền màu để shadow rõ hơn
                    contentAlignment = Alignment.Center // <--- QUAN TRỌNG: Căn giữa cái ly trong khung tròn
                ) {
                    DynamicCup(
                        size = selectedSize,
                        iceLevel = if (selectedType == "Hot") "None" else selectedIceLevel,
                        sugarLevel = selectedSugarLevel,
                        coffeeColor = if (isDarkTheme) Color(0xFF6F4E37) else Color(0xFF8B4513)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = boxColor),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            coffee.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            coffee.description,
                            fontSize = 14.sp,
                            color = textColor.copy(alpha = 0.7f)
                        )

                        Spacer(Modifier.height(12.dp))

                        Row {
                            TypeButton("Hot", selectedType == "Hot", primaryColor) {
                                selectedType = "Hot"
                            }
                            Spacer(Modifier.width(8.dp))
                            TypeButton("Ice", selectedType == "Ice", primaryColor) {
                                selectedType = "Ice"
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                SelectionSectionEnhanced(
                    "Size",
                    listOf("S", "M", "L"),
                    selectedSize,
                    boxColor,
                    textColor,
                    primaryColor
                ) { selectedSize = it }

                if (selectedType == "Ice") {
                    SelectionSectionEnhanced(
                        "Ice Level",
                        listOf("None", "Low", "Full"),
                        selectedIceLevel,
                        boxColor,
                        textColor,
                        secondaryColor
                    ) { selectedIceLevel = it }
                }

                SelectionSectionEnhanced(
                    "Sugar Level",
                    listOf("0%", "50%", "Normal"),
                    selectedSugarLevel,
                    boxColor,
                    textColor,
                    secondaryColor
                ) { selectedSugarLevel = it }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

/* ---------- FOOTER ---------- */

@Composable
private fun DetailScreenFooter(
    totalPrice: Double,
    textColor: Color,
    primaryColor: Color,
    isDarkTheme: Boolean,
    onAddToCart: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Total Price", color = Color.Gray)
                Text(
                    "$${String.format("%.2f", totalPrice)}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }

            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Text("Add to Cart", color = Color.White)
            }
        }
    }
}

/* ---------- COMPONENTS ---------- */

@Composable
private fun TypeButton(
    text: String,
    isSelected: Boolean,
    primaryColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) primaryColor else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SelectionSectionEnhanced(
    title: String,
    options: List<String>,
    selectedOption: String,
    boxColor: Color,
    textColor: Color,
    primaryColor: Color,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 20.dp)) {
        Text(title, fontWeight = FontWeight.Bold, color = textColor)

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) primaryColor else boxColor)
                        .clickable { onOptionSelected(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        option,
                        color = if (isSelected) Color.White else textColor,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
