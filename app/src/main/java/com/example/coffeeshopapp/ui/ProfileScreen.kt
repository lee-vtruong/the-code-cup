package com.example.coffeeshopapp.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeshopapp.R
import com.example.coffeeshopapp.model.UserData
import com.example.coffeeshopapp.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    isDarkTheme: Boolean,
    onTabSelected: (String) -> Unit,
    onLogout: () -> Unit
) {
    // ===== THEME COLORS =====
    val bgColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF121212) else Color(0xFFF8F4F0),
        animationSpec = tween(500)
    )

    val cardColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White,
        animationSpec = tween(500)
    )

    val textColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color.White else Color(0xFF2D4B73),
        animationSpec = tween(500)
    )

    val primaryColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFFBB86FC) else Color(0xFF2D4B73),
        animationSpec = tween(500)
    )

    // ===== USER DATA =====
    val userData = authViewModel.currentUserData.value
        ?: UserData(name = "Loading...", phone = "", email = "", address = "")

    // ===== DIALOG STATE =====
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var tempValue by remember { mutableStateOf("") }
    var editingField by remember { mutableStateOf("") }

    fun openEditDialog(title: String, field: String, currentValue: String) {
        dialogTitle = title
        editingField = field
        tempValue = currentValue
        showDialog = true
    }

    val scrollState = rememberScrollState()

    fun onSave() {
        when (editingField) {
            "Name" -> authViewModel.updateProfile(
                tempValue,
                userData.phone,
                userData.address
            )
            "Phone" -> authViewModel.updateProfile(
                userData.name,
                tempValue,
                userData.address
            )
            "Address" -> authViewModel.updateProfile(
                userData.name,
                userData.phone,
                tempValue
            )
        }
        showDialog = false
    }

    // ================= UI =================
    Scaffold(
        topBar = {
            // Profile Header with gradient
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .shadow(elevation = 8.dp),
                color = cardColor
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .shadow(12.dp, shape = CircleShape)
                            .clip(CircleShape)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                                    colors = listOf(
                                        primaryColor.copy(alpha = 0.8f),
                                        primaryColor.copy(alpha = 0.4f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )

                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // User Info
                    Column {
                        Text(
                            text = userData.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            text = userData.email,
                            fontSize = 14.sp,
                            color = textColor.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        // Member since badge
                        Row(
                            modifier = Modifier.padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(primaryColor.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = "Member",
                                    tint = primaryColor,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Member since 2024",
                                fontSize = 12.sp,
                                color = primaryColor
                            )
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
                    currentTab = "Profile",
                    onTabSelected = onTabSelected
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // Stats Cards Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Points Card
                StatsCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Loyalty,
                    title = "Points",
                    value = "${userData.points}",
                    color = if (isDarkTheme) Color(0xFF00E5FF) else Color(0xFF006400),
                    cardColor = cardColor
                )

                // Stamps Card
                StatsCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.LocalCafe,
                    title = "Stamps",
                    value = "${userData.stamps}/8",
                    color = if (isDarkTheme) Color(0xFFD4A574) else Color(0xFF8B4513),
                    cardColor = cardColor
                )

                // Orders Card
                StatsCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.ShoppingBag,
                    title = "Orders",
                    value = "12", // Hardcoded for now
                    color = if (isDarkTheme) Color(0xFFBB86FC) else Color(0xFF6A1B9A),
                    cardColor = cardColor
                )
            }

            // Profile Info Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    ProfileItem(
                        icon = Icons.Default.Person,
                        title = "Full name",
                        value = userData.name,
                        textColor = textColor,
                        primaryColor = primaryColor
                    ) {
                        openEditDialog("Edit Name", "Name", userData.name)
                    }

                    ProfileItem(
                        icon = Icons.Default.Phone,
                        title = "Phone number",
                        value = userData.phone.ifEmpty { "Not set" },
                        textColor = textColor,
                        primaryColor = primaryColor
                    ) {
                        openEditDialog("Edit Phone", "Phone", userData.phone)
                    }

                    ProfileItem(
                        icon = Icons.Default.Email,
                        title = "Email address",
                        value = userData.email,
                        textColor = textColor,
                        primaryColor = primaryColor,
                        isEditable = false
                    ) {
                        // Email không cho sửa
                    }

                    ProfileItem(
                        icon = Icons.Default.LocationOn,
                        title = "Delivery address",
                        value = userData.address.ifEmpty { "Tap to add address" },
                        textColor = textColor,
                        primaryColor = primaryColor
                    ) {
                        openEditDialog("Edit Address", "Address", userData.address)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Settings Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "App Settings",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    )

                    ProfileItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        value = "Enabled",
                        textColor = textColor,
                        primaryColor = primaryColor,
                        showChevron = false
                    ) {
                        // Toggle notifications
                    }

                    ProfileItem(
                        icon = Icons.Default.Security,
                        title = "Privacy & Security",
                        value = "",
                        textColor = textColor,
                        primaryColor = primaryColor
                    ) {
                        // Navigate to privacy
                    }

                    ProfileItem(
                        icon = Icons.Default.Help,
                        title = "Help & Support",
                        value = "",
                        textColor = textColor,
                        primaryColor = primaryColor
                    ) {
                        // Navigate to help
                    }

                    ProfileItem(
                        icon = Icons.Default.Info,
                        title = "About App",
                        value = "Version 1.0.0",
                        textColor = textColor,
                        primaryColor = primaryColor,
                        showChevron = false
                    ) {
                        // Show app info
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935).copy(alpha = if (isDarkTheme) 0.8f else 1f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // Bỏ padding vertical ở đây để dễ canh chỉnh với spacer hơn
                    .padding(bottom = 30.dp) // Thêm padding đáy để không bị sát mép dưới quá
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                    Text(
                        text = "Log Out",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }

    // ===== EDIT DIALOG =====
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    dialogTitle,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OutlinedTextField(
                    value = tempValue,
                    onValueChange = { tempValue = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = { onSave() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = textColor.copy(alpha = 0.7f)
                    )
                ) {
                    Text("Cancel", fontWeight = FontWeight.Medium)
                }
            },
            containerColor = cardColor,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun StatsCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    value: String,
    color: Color,
    cardColor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    title: String,
    value: String,
    textColor: Color,
    primaryColor: Color,
    isEditable: Boolean = true,
    showChevron: Boolean = true,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEditable) { onEditClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isEditable) primaryColor else Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                value,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = if (value == "Tap to add address" || value == "Not set")
                    Color.Gray
                else
                    textColor
            )
        }

        if (isEditable && showChevron) {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Edit",
                tint = primaryColor.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    }

    // Divider
    if (showChevron) {
        Divider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color.Gray.copy(alpha = 0.1f),
            thickness = 1.dp
        )
    }
}