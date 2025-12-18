package com.example.demo.ui.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.demo.data.changePass


@Composable
fun SettingUi(
    viewModelSetting: SettingViewModel = viewModel(),
    onLogout: () -> Unit = {}
){
    val stateSetting by viewModelSetting.uiState.collectAsState()
    var openChangePassDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModelSetting.display_the_threshold()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                CombinedThresholdCard(
                    tempThreshold = stateSetting.sensorData.thresholdTemp,
                    humidityThreshold = stateSetting.sensorData.thresholdHum,
                    onTempThresholdChange = { newValue ->
                        viewModelSetting.updateTempThreshold(newValue)
                    },
                    onHumidityThresholdChange = { newValue ->
                        viewModelSetting.updateHumidityThreshold(newValue)
                    },
                    onSave = {
                        viewModelSetting.saveSettings()
                    }
                )
            }

            item {
                LogoutButton(
                    onClick = onLogout,
                    onChangePassword = {
                        openChangePassDialog = true
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }

    if (stateSetting.sucMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModelSetting.resetMessages() },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Green) },
            title = { Text("Thành công") },
            text = { Text(stateSetting.sucMessage)},
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModelSetting.resetMessages()
                    }
                ) {
                    Text("Đóng")
                }
            }
        )
    }

    if (stateSetting.errMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModelSetting.resetMessages() },
            icon = { Icon(Icons.Default.Error, contentDescription = null, tint = Color.Red) },
            title = { Text("Thất bại") },
            text = { Text(stateSetting.errMessage) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModelSetting.resetMessages()
                    }
                ) {
                    Text("Đóng")
                }
            }
        )
    }

    if(openChangePassDialog){
        ChangePasswordDialog(
            onDismiss = {openChangePassDialog = false},
            onConfirm = {old,new,confirm ->
                val passData = changePass(
                    currentPass = old,
                    newPass = new,
                    continuePass = confirm
                )
                viewModelSetting.changePass(passData)
            }
        )
    }
}

@Composable
fun LogoutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onChangePassword: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = modifier.fillMaxWidth(0.9f),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ManageAccounts,
                    contentDescription = null,
                    tint = Color(0xFF60A5FA),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "TÀI KHOẢN",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF60A5FA),
                    letterSpacing = 1.2.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onChangePassword()},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC2626)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LockReset,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Đoi mat khau",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC2626)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Đăng Xuất",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            icon = {
                Icon(
                    Icons.Default.Logout,
                    contentDescription = null,
                    tint = Color(0xFFDC2626)
                )
            },
            title = { Text("Xác nhận đăng xuất") },
            text = { Text("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onClick()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFDC2626)
                    )
                ) {
                    Text("Đăng xuất")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Hủy")
                }
            }
        )
    }
}
@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Đổi Mật Khẩu", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = oldPass,
                    onValueChange = { oldPass = it },
                    label = { Text("Mật khẩu hiện tại") },
                    singleLine = true,
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = newPass,
                    onValueChange = { newPass = it },
                    label = { Text("Mật khẩu mới") },
                    singleLine = true,
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = confirmPass,
                    onValueChange = { confirmPass = it },
                    label = { Text("Xác nhận mật khẩu mới") },
                    singleLine = true,
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (oldPass.isNotEmpty() && newPass.isNotEmpty()) {
                        onConfirm(oldPass, newPass, confirmPass)
                        onDismiss()
                    }
                }
            ) {
                Text("Lưu thay đổi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
data class ThresholdZone(
    val label: String,
    val range: String,
    val color: Color,
    val icon: @Composable () -> Unit
)

@Composable
fun CombinedThresholdCard(
    tempThreshold: Float = 30f,
    humidityThreshold: Float = 80f,
    onTempThresholdChange: (Float) -> Unit = {},
    onHumidityThresholdChange: (Float) -> Unit = {},
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tempSlider by remember { mutableStateOf(tempThreshold) }
    var humiditySlider by remember { mutableStateOf(humidityThreshold) }

    LaunchedEffect(tempThreshold) {
        tempSlider = tempThreshold
    }
    LaunchedEffect(humidityThreshold) {
        humiditySlider = humidityThreshold
    }

    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = Color(0xFF60A5FA),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CẤU HÌNH CẢNH BÁO",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF60A5FA),
                    letterSpacing = 1.2.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Thermostat,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Nhiệt độ",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${tempSlider.toInt()}°C",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = getTempColor(tempSlider)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))


                VisualThresholdZones(
                    zones = listOf(
                        ThresholdZone("OK", "0-25°C", Color(0xFF10B981)) {
                            Icon(Icons.Default.Check, null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
                        },
                        ThresholdZone("WARN", "25-30°C", Color(0xFFFBBF24)) {
                            Icon(Icons.Default.Warning, null, tint = Color(0xFFFBBF24), modifier = Modifier.size(14.dp))
                        },
                        ThresholdZone("HIGH", ">30°C", Color(0xFFEF4444)) {
                            Icon(Icons.Default.Close, null, tint = Color(0xFFEF4444), modifier = Modifier.size(14.dp))
                        }
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))


                TemperatureSlider(
                    value = tempSlider,
                    onValueChange = {
                        tempSlider = it
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0°", fontSize = 10.sp, color = Color(0xFF64748B))
                    Text("25°", fontSize = 10.sp, color = Color(0xFF64748B))
                    Text("50°", fontSize = 10.sp, color = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Độ ẩm",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${humiditySlider.toInt()}%",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = getHumidityColor(humiditySlider)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))


                VisualThresholdZones(
                    zones = listOf(
                        ThresholdZone("DRY", "0-40%", Color(0xFFF59E0B)) {
                            Icon(Icons.Default.Warning, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(14.dp))
                        },
                        ThresholdZone("OK", "40-80%", Color(0xFF10B981)) {
                            Icon(Icons.Default.Check, null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
                        },
                        ThresholdZone("WET", ">80%", Color(0xFF3B82F6)) {
                            Icon(Icons.Default.WaterDrop, null, tint = Color(0xFF3B82F6), modifier = Modifier.size(14.dp))
                        }
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))


                HumiditySlider(
                    value = humiditySlider,
                    onValueChange = {
                        humiditySlider = it
                    }
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0%", fontSize = 10.sp, color = Color(0xFF64748B))
                    Text("50%", fontSize = 10.sp, color = Color(0xFF64748B))
                    Text("100%", fontSize = 10.sp, color = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onTempThresholdChange(tempSlider)
                    onHumidityThresholdChange(humiditySlider)
                    onSave()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Lưu Cấu Hình",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun VisualThresholdZones(zones: List<ThresholdZone>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        zones.forEachIndexed { index, zone ->
            // Zone Item
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                // Icon Circle
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(zone.color.copy(alpha = 0.2f))
                        .border(2.dp, zone.color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    zone.icon()
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Label
                Text(
                    text = zone.label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = zone.color
                )

                // Range
                Text(
                    text = zone.range,
                    fontSize = 9.sp,
                    color = Color(0xFF64748B)
                )
            }

            if (index < zones.size - 1) {
                GradientLine(
                    startColor = zone.color,
                    endColor = zones[index + 1].color,
                    modifier = Modifier.weight(0.3f)
                )
            }
        }
    }
}


@Composable
fun GradientLine(
    startColor: Color,
    endColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .height(2.dp)
            .padding(horizontal = 4.dp)
    ) {
        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(startColor, endColor)
            ),
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = size.height,
            cap = StrokeCap.Round
        )
    }
}


@Composable
fun TemperatureSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // Gradient background
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF10B981), // Green
                        Color(0xFFFBBF24), // Yellow
                        Color(0xFFEF4444)  // Red
                    )
                )
            )
        }

        // Slider
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..50f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun HumiditySlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // Gradient background
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFF59E0B), // Orange (Dry)
                        Color(0xFF10B981), // Green (OK)
                        Color(0xFF3B82F6)  // Blue (Wet)
                    )
                )
            )
        }

        // Slider
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun getTempColor(value: Float): Color {
    return when {
        value < 25f -> Color(0xFF10B981) // Green
        value < 30f -> Color(0xFFFBBF24) // Yellow
        else -> Color(0xFFEF4444) // Red
    }
}

fun getHumidityColor(value: Float): Color {
    return when {
        value < 40f -> Color(0xFFF59E0B) // Orange
        value < 80f -> Color(0xFF10B981) // Green
        else -> Color(0xFF3B82F6) // Blue
    }
}

