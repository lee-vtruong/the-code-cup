package com.example.coffeeshopapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.coffeeshopapp.model.Order
import com.example.coffeeshopapp.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var isLoggedIn = mutableStateOf(auth.currentUser != null)

    // ====== MỚI ======
    // Thông tin user hiện tại
    var currentUserData = mutableStateOf<UserData?>(null)

    // Lịch sử đơn hàng của user
    var userOrders = mutableStateOf<List<Order>>(emptyList())

    init {
        // Nếu đã login thì load dữ liệu ngay
        if (isLoggedIn.value) {
            fetchUserData()
            fetchUserOrders()
        }
    }

    // =========================
    // LOGIN (GIỮ NGUYÊN)
    // =========================
    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            errorMessage.value = "Please fill in all fields"
            return
        }
        isLoading.value = true
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    isLoggedIn.value = true
                    fetchUserData()
                    fetchUserOrders()
                    onSuccess()
                } else {
                    errorMessage.value =
                        task.exception?.localizedMessage ?: "Login failed"
                }
            }
    }

    // =========================
    // REGISTER (NÂNG CẤP)
    // =========================
    fun register(
        email: String,
        pass: String,
        name: String,
        phone: String,
        onSuccess: () -> Unit
    ) {
        if (email.isBlank() || pass.isBlank() || name.isBlank()) {
            errorMessage.value = "Please fill in all fields"
            return
        }

        isLoading.value = true

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser!!.uid

                    // ====== TẠO USERDATA MẶC ĐỊNH ======
                    val newUser = UserData(
                        id = userId,
                        name = name,
                        email = email,
                        phone = phone,
                        address = "",
                        points = 0,
                        stamps = 0
                    )

                    db.collection("users")
                        .document(userId)
                        .set(newUser)
                        .addOnSuccessListener {
                            isLoading.value = false
                            isLoggedIn.value = true
                            fetchUserData()
                            fetchUserOrders()
                            onSuccess()
                        }
                        .addOnFailureListener {
                            isLoading.value = false
                            errorMessage.value = "Failed to save user data"
                        }
                } else {
                    isLoading.value = false
                    errorMessage.value =
                        task.exception?.localizedMessage ?: "Register failed"
                }
            }
    }

    // =========================
    // LOGOUT (GIỮ NGUYÊN)
    // =========================
    fun logout() {
        auth.signOut()
        isLoggedIn.value = false
        currentUserData.value = null
        userOrders.value = emptyList()
    }

    // =========================
    // FETCH USER DATA (MỚI)
    // =========================
    fun fetchUserData() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    currentUserData.value =
                        snapshot.toObject(UserData::class.java)
                }
            }
    }
    fun fetchUserOrders() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("orders")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener

                if (snapshot != null) {
                    // CÁCH CŨ (SAI): val orders = snapshot.toObjects(Order::class.java)

                    // CÁCH MỚI (ĐÚNG): Duyệt từng document để lấy ID
                    val ordersList = mutableListOf<Order>()

                    for (document in snapshot.documents) {
                        val order = document.toObject(Order::class.java)
                        if (order != null) {
                            // Gán ID của document vào object Order
                            // Lưu ý: order.copy(...) tạo ra bản sao mới có ID
                            val orderWithId = order.copy(id = document.id)
                            ordersList.add(orderWithId)
                        }
                    }

                    userOrders.value = ordersList
                }
            }
    }

    // =========================
    // UPDATE PROFILE (MỚI)
    // =========================
    fun updateProfile(name: String, phone: String, address: String) {
        val userId = auth.currentUser?.uid ?: return
        val updates = mapOf(
            "name" to name,
            "phone" to phone,
            "address" to address
        )
        db.collection("users").document(userId).update(updates)
    }

    // =========================
    // UPDATE POINTS & STAMPS (MỚI)
    // =========================
    fun updatePointsAndStamps(addedPoints: Int, addedStamps: Int) {
        val userId = auth.currentUser?.uid ?: return
        val current = currentUserData.value ?: return

        val newPoints = current.points + addedPoints
        val newStamps = (current.stamps + addedStamps) % 8

        val updates = mapOf(
            "points" to newPoints,
            "stamps" to newStamps
        )
        db.collection("users").document(userId).update(updates)
    }
}
