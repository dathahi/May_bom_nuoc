package com.example.demo.ui.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo.data.HistoryPoint
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.gestures.detectTapGestures
import kotlin.math.abs

@Composable
fun GraphUi(
    viewModel: GraphViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF60A5FA))
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E293B), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage ?: "Lỗi không xác định",
                        color = Color(0xFFEF4444)
                    )
                }
            }

            uiState.historyData != null -> {
                val historyData = uiState.historyData!!

                ChartCard(
                    data = historyData.humidityHistory,
                    title = "Lịch sử Độ ẩm",
                    unit = "%",
                    lineColor = Color(0xFF3B82F6),
                    fillColor = Color(0xFF3B82F6).copy(alpha = 0.2f)
                )

                ChartCard(
                    data = historyData.temperatureHistory,
                    title = "Lịch sử Nhiệt độ",
                    unit = "°C",
                    lineColor = Color(0xFFEF4444),
                    fillColor = Color(0xFFEF4444).copy(alpha = 0.2f)
                )
            }
        }
    }
}

@Composable
fun ChartCard(
    data: List<HistoryPoint>,
    title: String,
    unit: String,
    lineColor: Color,
    fillColor: Color,
    modifier: Modifier = Modifier
) {
    var selectedPoint by remember { mutableStateOf<HistoryPoint?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1E293B), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (data.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không có dữ liệu",
                    color = Color(0xFF94A3B8)
                )
            }
        } else {
            // Stats
            StatsRow(data = data, unit = unit, lineColor = lineColor)

            Spacer(modifier = Modifier.height(16.dp))

            // Chart với tooltip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                // Background clickable để tắt tooltip
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { selectedPoint = null }
                )

                // Chart
                LineChart(
                    data = data,
                    lineColor = lineColor,
                    fillColor = fillColor,
                    onPointClick = { point -> selectedPoint = point }
                )

                // Tooltip
                if (selectedPoint != null) {
                    Tooltip(
                        point = selectedPoint!!,
                        unit = unit,
                        lineColor = lineColor,
                        onDismiss = { selectedPoint = null }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Time labels
            TimeLabels(
                startTime = data.first().timestamp,
                endTime = data.last().timestamp
            )
        }
    }
}

@Composable
fun StatsRow(
    data: List<HistoryPoint>,
    unit: String,
    lineColor: Color
) {
    val average = data.map { it.value }.average().toFloat()
    val min = data.minOfOrNull { it.value } ?: 0f
    val max = data.maxOfOrNull { it.value } ?: 0f
    val current = data.lastOrNull()?.value ?: 0f

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatItem(label = "Hiện tại", value = "$current$unit", color = lineColor)
        StatItem(label = "TB", value = String.format("%.1f", average) + unit, color = Color(0xFF94A3B8))
        StatItem(label = "Min", value = "$min$unit", color = Color(0xFF10B981))
        StatItem(label = "Max", value = "$max$unit", color = Color(0xFFEF4444))
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF94A3B8)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TimeLabels(startTime: Long, endTime: Long) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = formatTime(startTime),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF94A3B8)
        )
        Text(
            text = formatTime(endTime),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF94A3B8)
        )
    }
}

@Composable
fun Tooltip(
    point: HistoryPoint,
    unit: String,
    lineColor: Color,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFF334155), RoundedCornerShape(8.dp))
                .padding(12.dp)
                .clickable { onDismiss() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatDateTime(point.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${point.value}$unit",
                style = MaterialTheme.typography.titleLarge,
                color = lineColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LineChart(
    data: List<HistoryPoint>,
    lineColor: Color,
    fillColor: Color,
    onPointClick: (HistoryPoint) -> Unit
) {
    // Lấy mẫu để thưa hơn
    val sampledData = getSampledData(data)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val clickedPoint = findNearestPoint(
                        tapOffset = tapOffset,
                        data = sampledData,
                        canvasWidth = size.width.toFloat(),
                        canvasHeight = size.height.toFloat()
                    )

                    if (clickedPoint != null) {
                        onPointClick(clickedPoint)
                    }
                }
            }
    ) {
        if (sampledData.size < 2) return@Canvas

        val maxValue = sampledData.maxOf { it.value }
        val minValue = sampledData.minOf { it.value }
        val range = if (maxValue == minValue) 1f else maxValue - minValue

        val width = size.width
        val height = size.height
        val xStep = width / (sampledData.size - 1)

        // Vẽ fill area
        drawFillArea(sampledData, xStep, height, minValue, range, fillColor)

        // Vẽ line
        drawLine(sampledData, xStep, height, minValue, range, lineColor)

        // Vẽ points (thưa hơn)
        drawPoints(sampledData, xStep, height, minValue, range, lineColor)
    }
}

// Hàm lấy mẫu dữ liệu
fun getSampledData(data: List<HistoryPoint>): List<HistoryPoint> {
    return data.filterIndexed { index, _ -> index % 3 == 0 }
}

// Hàm tìm điểm gần nhất với vị trí click
fun findNearestPoint(
    tapOffset: Offset,
    data: List<HistoryPoint>,
    canvasWidth: Float,
    canvasHeight: Float
): HistoryPoint? {
    if (data.size < 2) return null

    val maxValue = data.maxOf { it.value }
    val minValue = data.minOf { it.value }
    val range = if (maxValue == minValue) 1f else maxValue - minValue
    val xStep = canvasWidth / (data.size - 1)

    var nearestPoint: HistoryPoint? = null
    var minDistance = Float.MAX_VALUE

    data.forEachIndexed { index, point ->
        val x = index * xStep
        val normalizedValue = (point.value - minValue) / range
        val y = canvasHeight - (normalizedValue * canvasHeight * 0.9f) - (canvasHeight * 0.05f)

        val distance = calculateDistance(tapOffset.x, tapOffset.y, x, y)

        if (distance < minDistance) {
            minDistance = distance
            nearestPoint = point
        }
    }

    // Chỉ trả về nếu click đủ gần (trong 40dp)
    return if (minDistance < 40 * 3) nearestPoint else null
}

// Hàm tính khoảng cách
fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
    val dx = x1 - x2
    val dy = y1 - y2
    return kotlin.math.sqrt(dx * dx + dy * dy)
}

// Vẽ fill area
fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFillArea(
    data: List<HistoryPoint>,
    xStep: Float,
    height: Float,
    minValue: Float,
    range: Float,
    fillColor: Color
) {
    val path = Path()

    data.forEachIndexed { index, point ->
        val x = index * xStep
        val normalizedValue = (point.value - minValue) / range
        val y = height - (normalizedValue * height * 0.9f) - (height * 0.05f)

        if (index == 0) {
            path.moveTo(x, height)
            path.lineTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }

    path.lineTo(data.size * xStep, height)
    path.close()

    drawPath(path = path, color = fillColor)
}

// Vẽ line
fun androidx.compose.ui.graphics.drawscope.DrawScope.drawLine(
    data: List<HistoryPoint>,
    xStep: Float,
    height: Float,
    minValue: Float,
    range: Float,
    lineColor: Color
) {
    val path = Path()

    data.forEachIndexed { index, point ->
        val x = index * xStep
        val normalizedValue = (point.value - minValue) / range
        val y = height - (normalizedValue * height * 0.9f) - (height * 0.05f)

        if (index == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }

    drawPath(
        path = path,
        color = lineColor,
        style = Stroke(width = 3 * density)
    )
}

// Vẽ points
fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPoints(
    data: List<HistoryPoint>,
    xStep: Float,
    height: Float,
    minValue: Float,
    range: Float,
    lineColor: Color
) {
    data.forEachIndexed { index, point ->
        // Chỉ vẽ mỗi điểm thứ 2
        if (index % 2 == 0) {
            val x = index * xStep
            val normalizedValue = (point.value - minValue) / range
            val y = height - (normalizedValue * height * 0.9f) - (height * 0.05f)

            // Viền trắng
            drawCircle(
                color = Color.White,
                radius = 6 * density,
                center = Offset(x, y)
            )

            // Điểm màu
            drawCircle(
                color = lineColor,
                radius = 4 * density,
                center = Offset(x, y)
            )
        }
    }
}


fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp.toLong()*1000))
}

fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp.toLong()*1000))
}