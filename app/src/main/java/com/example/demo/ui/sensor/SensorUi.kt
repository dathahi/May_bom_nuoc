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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo.data.UserManager
import kotlinx.coroutines.delay
import androidx.compose.runtime.setValue

@Composable
fun SensorUi(
    viewModel: SensorViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var isOnline by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.get_info_sensor()
    }

    LaunchedEffect(Unit) {
        while (true) {
            isOnline = UserManager.isOnline()
            delay(5000L)
        }
    }


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