package com.example.coffeeshopapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coffeeshopapp.data.DataSource
import com.example.coffeeshopapp.model.Coffee
import com.example.coffeeshopapp.model.Order
import com.example.coffeeshopapp.model.OrderStatus
import com.example.coffeeshopapp.ui.*
import com.example.coffeeshopapp.ui.theme.DarkColorScheme
import com.example.coffeeshopapp.ui.theme.LightColorScheme
import com.example.coffeeshopapp.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/* ---------- SCREENS ENUM ---------- */
enum class Screen {
    SPLASH, LOGIN, SIGNUP,
    HOME, DETAIL, CART, SUCCESS,
    PROFILE, REWARDS,
    ORDERS,
    ORDER_DETAIL
}

class MainActivity : ComponentActivity() {

    /* ---------- SHAKE DETECTION ---------- */
    private var shakeDetector: ShakeDetector? = null
    private val showRandomDialog = mutableStateOf(false)
    private val randomCoffeeName = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo cảm biến lắc
        shakeDetector = ShakeDetector(this) {
            vibratePhone()
            val randomItem = DataSource.coffeeMenu.random()
            randomCoffeeName.value = randomItem.name
            showRandomDialog.value = true
        }

        setContent {
            /* ---------- THEME STATE ---------- */
            var isDarkTheme by remember { mutableStateOf(false) }

            // Áp dụng theme cho toàn bộ app
            MaterialTheme(
                colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme
            ) {
                /* ---------- VIEWMODEL & DATA ---------- */
                val authViewModel: AuthViewModel = viewModel()
                val userData by authViewModel.currentUserData
                val userOrders by authViewModel.userOrders

                /* ---------- UI STATE ---------- */
                var currentScreen by remember { mutableStateOf(Screen.SPLASH) }

                // Biến lưu tạm dữ liệu để truyền giữa các màn hình
                var selectedCoffee by remember { mutableStateOf<Coffee?>(null) }
                var selectedOrderHistory by remember { mutableStateOf<Order?>(null) }

                /* ---------- CART STATE ---------- */
                val cartItems = remember { mutableStateListOf<Pair<Coffee, Int>>() }

                // Hàm chuyển tab chung cho BottomNavigation
                val onTabSelected: (String) -> Unit = { tab ->
                    currentScreen = when (tab) {
                        "Home" -> Screen.HOME
                        "Profile" -> Screen.PROFILE
                        "Rewards" -> Screen.REWARDS
                        "Orders" -> Screen.ORDERS
                        else -> currentScreen
                    }
                }

                /* ---------- NAVIGATION LOGIC ---------- */
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (currentScreen) {

                        Screen.SPLASH ->
                            SplashScreen {
                                currentScreen = if (authViewModel.isLoggedIn.value) Screen.HOME else Screen.LOGIN
                            }

                        /* ---------- AUTHENTICATION ---------- */
                        Screen.LOGIN ->
                            LoginScreen(
                                authViewModel = authViewModel,
                                onNavigateToSignUp = { currentScreen = Screen.SIGNUP },
                                onLoginSuccess = { currentScreen = Screen.HOME }
                            )

                        Screen.SIGNUP ->
                            SignUpScreen(
                                authViewModel = authViewModel,
                                onNavigateToLogin = { currentScreen = Screen.LOGIN },
                                onSignUpSuccess = { currentScreen = Screen.HOME }
                            )

                        /* ---------- MAIN FEATURES ---------- */
                        Screen.HOME ->
                            HomeScreen(
                                userName = userData?.name ?: "Guest",
                                isDarkTheme = isDarkTheme,
                                onThemeToggle = { isDarkTheme = !isDarkTheme },
                                loyaltyStamps = userData?.stamps ?: 0,
                                onCoffeeClick = {
                                    selectedCoffee = it
                                    currentScreen = Screen.DETAIL
                                },
                                onCartClick = { currentScreen = Screen.CART },
                                onTabSelected = onTabSelected
                            )

                        Screen.DETAIL ->
                            selectedCoffee?.let { coffee ->
                                DetailScreen(
                                    coffee = coffee,
                                    isDarkTheme = isDarkTheme,
                                    onBack = { currentScreen = Screen.HOME },
                                    onAddToCart = { item, qty, _, _, _ ->
                                        cartItems.add(item to qty)
                                        currentScreen = Screen.CART
                                    }
                                )
                            }

                        Screen.CART ->
                            CartScreen(
                                cartItems = cartItems,
                                isDarkTheme = isDarkTheme,
                                onBack = { currentScreen = Screen.HOME },
                                onCheckout = {
                                    if (cartItems.isNotEmpty()) {
                                        val totalAmount = cartItems.sumOf { it.first.price * it.second }
                                        val totalCups = cartItems.sumOf { it.second }
                                        val desc = cartItems.joinToString(", ") { "${it.first.name} x${it.second}" }

                                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                                        if (userId != null) {
                                            // TẠO ĐƠN HÀNG MỚI
                                            val orderMap = hashMapOf(
                                                "userId" to userId,
                                                "date" to Date(),
                                                "itemsDescription" to desc,
                                                "totalPrice" to totalAmount,
                                                "status" to OrderStatus.PREPARING.name // Lưu là "PREPARING"
                                            )

                                            FirebaseFirestore.getInstance()
                                                .collection("orders")
                                                .add(orderMap)

                                            authViewModel.updatePointsAndStamps(
                                                addedPoints = totalAmount.toInt(),
                                                addedStamps = totalCups
                                            )

                                            cartItems.clear()
                                            currentScreen = Screen.SUCCESS
                                        }
                                    }
                                },
                                onDeleteItem = { item ->
                                    cartItems.removeIf { it.first == item }
                                }
                            )

                        Screen.SUCCESS ->
                            SuccessScreen { currentScreen = Screen.ORDERS }

                        Screen.PROFILE ->
                            ProfileScreen(
                                isDarkTheme = isDarkTheme,
                                authViewModel = authViewModel,
                                onTabSelected = onTabSelected,
                                onLogout = {
                                    authViewModel.logout()
                                    currentScreen = Screen.LOGIN
                                }
                            )

                        Screen.REWARDS ->
                            RewardsScreen(
                                isDarkTheme = isDarkTheme,
                                currentPoints = userData?.points ?: 0,
                                loyaltyStamps = userData?.stamps ?: 0,
                                orderHistory = userOrders,
                                onTabSelected = onTabSelected
                            )

                        Screen.ORDERS ->
                            OrderHistoryScreen(
                                isDarkTheme = isDarkTheme,
                                orders = userOrders,
                                onTabSelected = onTabSelected,
                                onOrderClick = { order ->
                                    selectedOrderHistory = order
                                    currentScreen = Screen.ORDER_DETAIL
                                }
                            )

                        /* ---------- ORDER DETAIL ---------- */
                        Screen.ORDER_DETAIL ->
                            selectedOrderHistory?.let { order ->
                                OrderDetailScreen(
                                    order = order,
                                    isDarkTheme = isDarkTheme,
                                    onBack = { currentScreen = Screen.ORDERS },
                                    onCompleteOrder = {
                                        // 1. Kiểm tra ID đơn hàng có tồn tại không
                                        if (order.id.isNotEmpty()) {
                                            FirebaseFirestore.getInstance()
                                                .collection("orders")
                                                .document(order.id)
                                                .update("status", OrderStatus.COMPLETED.name) // Update thành "COMPLETED"
                                                .addOnSuccessListener {
                                                    Toast.makeText(this@MainActivity, "Order Completed!", Toast.LENGTH_SHORT).show()
                                                    // 2. Quay lại màn hình Orders sau khi thành công
                                                    currentScreen = Screen.ORDERS
                                                }
                                                .addOnFailureListener {
                                                    Toast.makeText(this@MainActivity, "Failed to update order", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            Toast.makeText(this@MainActivity, "Error: Order ID missing", Toast.LENGTH_SHORT).show()
                                            // Vẫn quay lại để user không bị kẹt, nhưng báo lỗi
                                            currentScreen = Screen.ORDERS
                                        }
                                    }
                                )
                            }
                    }

                    /* ---------- RANDOM SHAKE DIALOG ---------- */
                    if (showRandomDialog.value) {
                        AlertDialog(
                            onDismissRequest = { showRandomDialog.value = false },
                            title = {
                                Text(
                                    "Universe Suggestion ✨",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            text = {
                                Text(
                                    "The universe says you should drink ${randomCoffeeName.value}!",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            containerColor = MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.large,
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showRandomDialog.value = false
                                        DataSource.coffeeMenu.find { it.name == randomCoffeeName.value }?.let {
                                            selectedCoffee = it
                                            currentScreen = Screen.DETAIL
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(
                                        "View it now",
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { showRandomDialog.value = false },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Text(
                                        "Close",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    /* ---------- LIFECYCLE & UTILS ---------- */
    override fun onResume() {
        super.onResume()
        shakeDetector?.start()
    }

    override fun onPause() {
        super.onPause()
        shakeDetector?.stop()
    }

    private fun vibratePhone() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(200)
        }
    }
}