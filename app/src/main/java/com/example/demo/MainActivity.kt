//package com.example.demo
//
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Call
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.google.firebase.database.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
//
//data class SensorData(
//    val temperature: Float = 0f,
//    val humidity: Float = 0f,
//    val timestamp: Long = 0L
//)
//
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Initialize Firebase
//        val rootDatabase = FirebaseDatabase.getInstance("https://fir-ad53c-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
//        val sensorRef = rootDatabase.child("sensor")
//        val settingRef = rootDatabase.child("setting")
//        val loginRef = rootDatabase.child("login")
//        setContent {
//            DHT22MonitorTheme {
//                var currentPage by remember { mutableIntStateOf(1) }
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    when(currentPage){
//                        1 -> LoginPage(
//                            database = loginRef,
//                            onLoginSuccess = {currentPage = 2}
//                        )
//                        2 -> SensorMonitorScreen(
//                            database = sensorRef,
//                            onNavigateToChinh = {currentPage = 3},
//                            onLogout = {currentPage = 1}
//                        )
//                        3 -> Chinhthongso(
//                            database = settingRef,
//                            onNavigateToBack = {currentPage = 2}
//                        )
//                    }
//
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun DHT22MonitorTheme(content: @Composable () -> Unit) {
//    MaterialTheme(
//        colorScheme = lightColorScheme(
//            primary = Color(0xFF6200EE),
//            secondary = Color(0xFF03DAC6),
//            background = Color(0xFFF5F5F5)
//        ),
//        content = content
//    )
//}
//
//@Composable
//fun LoginPage(
//    database: DatabaseReference,
//    onLoginSuccess: () -> Unit
//){
//    var showRegisterDialog by remember { mutableStateOf(false) }
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf("") }
//    var isLoading by remember { mutableStateOf(false) }
//    var isPasswordVisible by remember { mutableStateOf(false) }
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(24.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Đăng nhập",
//            fontSize = 22.sp,
//            fontWeight = FontWeight.Bold
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        OutlinedTextField(
//            value = username,
//            onValueChange = {username = it},
//            label = {Text(text = "Tên Đăng Nhập")},
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = {password= it},
//            label = {Text(text = "Mật Khẩu")},
//            singleLine = true,
//            visualTransformation = if(isPasswordVisible) {
//                VisualTransformation.None
//            }else{
//                PasswordVisualTransformation()
//            },
//            trailingIcon = {
//                val image = if (isPasswordVisible)
//                    Icons.Default.Visibility
//                else
//                    Icons.Default.VisibilityOff
//
//                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
//                    Icon(imageVector = image, contentDescription = null)
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Button(
//            onClick = {
//                if (username.isBlank() || password.isBlank()) {
//                    errorMessage = "Vui lòng nhập đầy đủ thông tin"
//                    return@Button
//                }
//
//                isLoading = true
//                errorMessage = ""
//
//                database.addListenerForSingleValueEvent(object : ValueEventListener{
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        isLoading = false
//                        var found = false
//
//                        for(child in snapshot.children){
//                            val dbUsername = child.child("username").getValue(String::class.java)
//                            val dbPassword = child.child("password").getValue(String::class.java)
//                            if(username == dbUsername && password == dbPassword){
//                                found = true
//                                break
//                            }
//                        }
//                        if(found){
//                            onLoginSuccess()
//                            errorMessage = ""
//                        }else{
//                            errorMessage = "Sai tên đăng nhập hoặc mật khẩu"
//                        }
//                    }
//                    override fun onCancelled(error: DatabaseError){
//                        isLoading = false
//                        errorMessage = "lỗi kết nối Firebase"
//                    }
//                })
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            if (isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(20.dp),
//                    strokeWidth = 2.dp
//                )
//            } else {
//                Text("Đăng nhập")
//            }
//        }
//
//        Button(
//            onClick = {
//                showRegisterDialog = true},
//            modifier = Modifier.fillMaxWidth(),
//        ) {
//            Text("Đăng ký")
//        }
//        if (showRegisterDialog) {
//            RegisterDialog(
//                database = database,
//                onDismiss = { showRegisterDialog = false }
//            )
//        }
//        if (errorMessage.isNotEmpty()) {
//            Spacer(modifier = Modifier.height(12.dp))
//            Text(
//                text = errorMessage,
//                color = Color.Red,
//                fontSize = 14.sp
//            )
//        }
//    }
//}
//@Composable
//fun RegisterDialog(
//    database : DatabaseReference,
//    onDismiss: () -> Unit
//) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Đăng ký tài khoản") },
//        text = {
//            Column {
//                OutlinedTextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = { Text("Email") }
//                )
//
//                OutlinedTextField(
//                    value = password,
//                    onValueChange = { password = it },
//                    label = { Text("Mật khẩu") }
//                )
//            }
//        },
//        confirmButton = {
//            Button(onClick = {
//                val newUserRef = database.push()
//                newUserRef.setValue(mapOf(
//                    "username" to email,
//                    "password" to password
//                ))
//                onDismiss()
//            }) {
//                Text("Tạo tài khoản")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Hủy")
//            }
//        }
//    )
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SensorMonitorScreen(
//    database: DatabaseReference,
//    onNavigateToChinh: () -> Unit,
//    onLogout: () -> Unit
//) {
//
//    var sensorData by remember { mutableStateOf(SensorData()) }
//    var isConnected by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val temp = snapshot.child("temperature").getValue(Float::class.java) ?: 0f
//                val hum = snapshot.child("humidity").getValue(Float::class.java) ?: 0f
//                val time = snapshot.child("timestamp").getValue(Long::class.java) ?: 0L
//
//                sensorData = SensorData(temp, hum, time)
//                isConnected = true
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                isConnected = false
//            }
//        })
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        "DHT22 Monitor",
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    titleContentColor = Color.White
//                )
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            // Connection Status
//            ConnectionStatusCard(isConnected)
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Temperature Card
//            SensorCard(
//                title = "Nhiệt Độ",
//                value = String.format(Locale.getDefault(), "%.1f°C", sensorData.temperature),
//                icon = Icons.Default.Add,
//                gradient = Brush.horizontalGradient(
//                    colors = listOf(Color(0xFFFF6B6B), Color(0xFFFF8E53))
//                )
//            )
//
//            // Humidity Card
//            SensorCard(
//                title = "Độ Ẩm",
//                value = String.format(Locale.getDefault(), "%.1f%%", sensorData.humidity),
//                icon = Icons.Default.Call,
//                gradient = Brush.horizontalGradient(
//                    colors = listOf(Color(0xFF4E54C8), Color(0xFF8F94FB))
//                )
//            )
//            Button(
//                onClick = onNavigateToChinh,
//                modifier = Modifier.fillMaxWidth()
//            ) {Text(text = "Chinh thong so") }
//            // Last Update
//            if (sensorData.timestamp > 0) {
//                Text(
//                    text = "Cập nhật: ${formatTimestamp(sensorData.timestamp)}",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color.Gray
//                )
//            }
//            Button(
//                onClick = onLogout,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp)
//            ) {
//                Text("Đăng xuất")
//            }
//        }
//
//    }
//}
//
//
//@Composable
//fun Chinhthongso(
//    database: DatabaseReference,
//    onNavigateToBack: () -> Unit
//) {
//    // State kiểu Float
//    var thresholdTemp by remember { mutableFloatStateOf(25f) }
//    var thresholdHum by remember { mutableFloatStateOf(50f) }
//
//    // State kiểu String để nhập
//    var tempText by remember { mutableStateOf(thresholdTemp.toString()) }
//    var humText by remember { mutableStateOf(thresholdHum.toString()) }
//
//    // Lấy dữ liệu từ Firebase
//    LaunchedEffect(Unit) {
//        database.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                thresholdTemp = snapshot.child("thresholdTemp").getValue(Float::class.java) ?: 25f
//                thresholdHum = snapshot.child("thresholdHum").getValue(Float::class.java) ?: 50f
//                tempText = thresholdTemp.toString()
//                humText = thresholdHum.toString()
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            "Chỉnh Thông Số",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            color = MaterialTheme.colorScheme.primary
//        )
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Nhiệt độ ngưỡng
//        OutlinedTextField(
//            value = tempText,
//            onValueChange = { tempText = it },
//            label = { Text("Nhiệt độ ngưỡng") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Độ ẩm ngưỡng
//        OutlinedTextField(
//            value = humText,
//            onValueChange = { humText = it },
//            label = { Text("Độ ẩm ngưỡng") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Button(
//            onClick = {
//                // Convert từ String sang Float khi lưu
//                thresholdTemp = tempText.toFloatOrNull() ?: thresholdTemp
//                thresholdHum = humText.toFloatOrNull() ?: thresholdHum
//
//                database.child("thresholdTemp").setValue(thresholdTemp)
//                database.child("thresholdHum").setValue(thresholdHum)
//                onNavigateToBack()
//            },
//            modifier = Modifier.fillMaxWidth(),
//        ) {
//            Text("Lưu")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = onNavigateToBack,
//            modifier = Modifier.fillMaxWidth(),
//        ) {
//            Text("Quay lại")
//        }
//    }
//}
//
//@Composable
//fun ConnectionStatusCard(isConnected: Boolean) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = if (isConnected) Color(0xFF4CAF50) else Color(0xFFF44336)
//        ),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(12.dp)
//                    .background(Color.White, shape = RoundedCornerShape(6.dp))
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = if (isConnected) "Đã kết nối" else "Mất kết nối",
//                color = Color.White,
//                fontWeight = FontWeight.Bold
//            )
//        }
//    }
//}
//
//@Composable
//fun SensorCard(
//    title: String,
//    value: String,
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    gradient: Brush
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(180.dp),
//        shape = RoundedCornerShape(20.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(gradient)
//                .padding(24.dp)
//        ) {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.SpaceBetween
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = title,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Medium,
//                        color = Color.White
//                    )
//                    Icon(
//                        imageVector = icon,
//                        contentDescription = title,
//                        tint = Color.White.copy(alpha = 0.7f),
//                        modifier = Modifier.size(32.dp)
//                    )
//                }
//
//                Text(
//                    text = value,
//                    fontSize = 48.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )
//            }
//        }
//    }
//}
//
//fun formatTimestamp(timestamp: Long): String {
//    val sdf = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault())
//    return sdf.format(Date(timestamp))
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun SensorCardPreview() {
//    SensorCard(
//        title = "Nhiệt Độ",
//        value = "25.5°C",
//        icon = Icons.Default.Add,
//        gradient = Brush.horizontalGradient(
//            colors = listOf(Color(0xFFFF6B6B), Color(0xFFFF8E53))
//        )
//    )
//}
//@Preview(showBackground = true)
//@Composable
//fun ConnectionStatusCardPreview() {
//    Column(
//        modifier = Modifier.padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        ConnectionStatusCard(isConnected = true)
//        ConnectionStatusCard(isConnected = false)
//    }
//}
//@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//@Composable
//fun SensorMonitorScreenPreview() {
//    val dummyData = SensorData(temperature = 26.5f, humidity = 55.2f, timestamp = System.currentTimeMillis())
//
//    var sensorData by remember { mutableStateOf(dummyData) }
//    var isConnected by remember { mutableStateOf(true) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(24.dp)
//    ) {
//        ConnectionStatusCard(isConnected)
//        SensorCard(
//            title = "Nhiệt Độ",
//            value = String.format(Locale.getDefault(), "%.1f°C", sensorData.temperature),
//            icon = Icons.Default.Add,
//            gradient = Brush.horizontalGradient(
//                colors = listOf(Color(0xFFFF6B6B), Color(0xFFFF8E53))
//            )
//        )
//        SensorCard(
//            title = "Độ Ẩm",
//            value = String.format(Locale.getDefault(), "%.1f%%", sensorData.humidity),
//            icon = Icons.Default.Call,
//            gradient = Brush.horizontalGradient(
//                colors = listOf(Color(0xFF4E54C8), Color(0xFF8F94FB))
//            )
//        )
//        Text(
//            text = "Cập nhật: ${formatTimestamp(sensorData.timestamp)}",
//            color = Color.Gray
//        )
//    }
//}

package com.example.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.demo.navigation.Dht22app
import com.google.firebase.database.*
import java.util.*

@Composable
fun DHT22MonitorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFFF5F5F5)
        ),
        content = content
    )
}


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DHT22MonitorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Dht22app()
                }
            }
        }
    }
}


