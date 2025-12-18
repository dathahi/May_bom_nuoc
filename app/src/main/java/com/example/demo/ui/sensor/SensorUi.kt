//package com.example.demo.ui.sensor
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Thermostat
//import androidx.compose.material.icons.filled.WaterDrop
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedCard
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//@Composable
//fun SensorUi(
//    viewModel: SensorViewModel = viewModel(),
//) {
//    val state by viewModel.uiState.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.get_info_sensor()
//        viewModel.get_history() // Lấy history để có timestamp
//    }
//
//    // Lấy timestamp mới nhất từ history (cả humidity và temperature)
//    val latestTimestamp = maxOf(
//        state.historyData.humidityHistory.maxOfOrNull { it.timestamp } ?: 0L,
//        state.historyData.temperatureHistory.maxOfOrNull { it.timestamp } ?: 0L
//    )
//
//    // Kiểm tra trạng thái online/offline (>5 phút = offline)
//    val isOnline = if (latestTimestamp > 0) {
//        val currentTime = System.currentTimeMillis()
//        val timeDifference = currentTime - latestTimestamp
//        timeDifference <= 5 * 60 * 1000 // 5 phút
//    } else {
//        false
//    }
//
//    Column(
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        // Status indicator
//        StatusIndicator(isOnline = isOnline)
//
//        CardSensor(
//            label = "Nhiệt Độ",
//            value = state.sensorData.temperature,
//            unit = "°C",
//            icon = Icons.Default.Thermostat,
//            isOnline = isOnline
//        )
//
//        CardSensor(
//            label = "Humidity",
//            value = state.sensorData.humidity,
//            unit = "%",
//            icon = Icons.Default.WaterDrop,
//            isOnline = isOnline
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = "Thống kê hôm nay",
//            fontWeight = FontWeight.Bold
//        )
//    }
//}
//
//@Composable
//fun StatusIndicator(isOnline: Boolean) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Box(
//            modifier = Modifier
//                .size(12.dp)
//                .background(
//                    color = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336),
//                    shape = CircleShape
//                )
//        )
//
//        Spacer(modifier = Modifier.padding(start = 8.dp))
//
//        Text(
//            text = if (isOnline) "Online" else "Offline",
//            style = MaterialTheme.typography.bodyMedium.copy(
//                fontWeight = FontWeight.SemiBold,
//                color = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336)
//            )
//        )
//    }
//}
//
//@Composable
//fun CardSensor(
//    label: String,
//    value: Float,
//    unit: String,
//    icon: ImageVector,
//    isOnline: Boolean = true
//) {
//    OutlinedCard(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 6.dp),
//        colors = CardDefaults.outlinedCardColors(
//            containerColor = if (isOnline)
//                MaterialTheme.colorScheme.surface
//            else
//                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
//        ),
//        border = BorderStroke(
//            width = 1.dp,
//            color = if (isOnline)
//                MaterialTheme.colorScheme.outlineVariant
//            else
//                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
//        ),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(14.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = icon,
//                contentDescription = null,
//                tint = if (isOnline)
//                    MaterialTheme.colorScheme.primary
//                else
//                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
//                modifier = Modifier.size(32.dp)
//            )
//
//            Spacer(modifier = Modifier.padding(start = 12.dp, end = 12.dp))
//
//            Column {
//                Text(
//                    text = label,
//                    style = MaterialTheme.typography.labelMedium.copy(
//                        color = if (isOnline)
//                            MaterialTheme.colorScheme.primary
//                        else
//                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
//                    )
//                )
//
//                Text(
//                    text = if (isOnline) "$value $unit" else "-- $unit",
//                    style = MaterialTheme.typography.headlineSmall.copy(
//                        fontWeight = FontWeight.Bold,
//                        color = if (isOnline)
//                            MaterialTheme.colorScheme.onSurface
//                        else
//                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
//                    )
//                )
//            }
//        }
//    }
//}

package com.example.demo.ui.sensor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo.data.UserManager

@Composable
fun SensorUi(
    viewModel: SensorViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.get_info_sensor()
    }

    val isOnline = UserManager.isOnline()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Card hiển thị trạng thái kết nối
        ConnectionStatusCard(isOnline = isOnline)

        // Card Nhiệt độ
        CardSensor(
            label = "Nhiệt Độ",
            value = state.sensorData.temperature,
            unit = "°C",
            icon = Icons.Default.Thermostat
        )

        // Card Độ ẩm
        CardSensor(
            label = "Humidity",
            value = state.sensorData.humidity,
            unit = "%",
            icon = Icons.Default.WaterDrop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Thống kê hôm nay",
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ConnectionStatusCard(isOnline: Boolean) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isOnline)
                Color(0xFFE8F5E9)
            else
                Color(0xFFFFEBEE),
        ),
        border = BorderStroke(
            width = 2.dp,
            color = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isOnline) Icons.Default.Wifi else Icons.Default.WifiOff,
                    contentDescription = null,
                    tint = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336),
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.padding(start = 12.dp))

                Column {
                    Text(
                        text = "Trạng thái kết nối",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (isOnline) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    )

                    Text(
                        text = if (isOnline) "Đang kết nối" else "Mất kết nối",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isOnline) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        color = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun CardSensor(
    label: String,
    value: Float,
    unit: String,
    icon: ImageVector
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.padding(start = 12.dp, end = 12.dp))

            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Text(
                    text = "$value $unit",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}