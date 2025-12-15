package com.example.demo.navigation

import android.health.connect.datatypes.Device
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.demo.R
import com.example.demo.ui.login.LoginUi
import com.example.demo.ui.sensor.SensorUi
import com.example.demo.ui.sensor.SensorViewModel




sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
){
    object Sensor: BottomBarScreen(
        route = "sensor",
        title = " Cảm biến",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    object Graph: BottomBarScreen(
        route = "graph",
        title = " Biểu đồ",
        selectedIcon = Icons.Filled.Analytics,
        unselectedIcon = Icons.Outlined.Home
    )
    object Setting: BottomBarScreen(
        route = "setting",
        title = "Cài đặt",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}


object Screen{
    const val login = "login"
    const val sensor = "sensor"
    const val setting = "setting"
}
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun Dht22AppBar(isDeviceOnline: Boolean){
    TopAppBar(
        title = {Text(text = stringResource(R.string.app))},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            DeviceState(isOnline = isDeviceOnline)
        }
    )
}
@Composable
fun DeviceState(isOnline: Boolean){
    val statuscolor = if(isOnline) Color(0xFF4CAF50) else Color(0xFFFF5252)
    val statustext = if(isOnline) "Online" else "Offline"
    val containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
    Surface(
        shape = RoundedCornerShape(50),
        color = containerColor,
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color = statuscolor, shape = CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))


            Text(
                text = statustext,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}





@Composable
fun Dht22app(
    navController: NavHostController = rememberNavController(),
    viewModel: SensorViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            Dht22AppBar(uiState.isConnect)
        }
    )
    {
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.login,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Screen.login) {
                LoginUi(
                    onClickButton = {
                        navController.navigate(Screen.sensor)
                    },
                )
            }
            composable(Screen.sensor) {
                SensorUi(
                onClickButton = {
                    navController.navigate(Screen.setting)
                },
                onBackButton = {
                    navController.navigate(Screen.login)
                }
                )
            }
        }
    }
}