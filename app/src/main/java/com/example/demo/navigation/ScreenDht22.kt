package com.example.demo.navigation

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
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.demo.ui.graph.GraphUi
import com.example.demo.ui.login.LoginUi
import com.example.demo.ui.sensor.SensorUi
import com.example.demo.ui.sensor.SensorViewModel
import com.example.demo.ui.setting.SettingUi


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
        unselectedIcon = Icons.Outlined.Analytics
    )
    object Setting: BottomBarScreen(
        route = "setting",
        title = "Cài đặt",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}

@Composable
fun rememberCurrentRoute(navController: NavHostController): NavDestination? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination
}

@Composable
fun MyNavigationBar(navController: NavHostController, currentRoute: NavDestination?) {
    val screens = listOf(
        BottomBarScreen.Sensor,
        BottomBarScreen.Graph,
        BottomBarScreen.Setting

    )

    val bottomBarDestination = screens.any{it.route == currentRoute?.route}

    if(bottomBarDestination){
        NavigationBar(

            containerColor = Color(0xFF1E293B),  // Slate-800
            contentColor = Color.White,
            tonalElevation = 0.dp  // Bỏ shadow
        ) {
            screens.forEach { screen ->
                val isSelected = currentRoute?.hierarchy?.any{it.route == screen.route}?: false
                NavigationBarItem(
                    label = {
                        Text(
                            text = screen.title,
                            // ============================================
                            // CẢI THIỆN: Màu text rõ ràng hơn
                            // ============================================
                            color = if(isSelected) Color(0xFF60A5FA) else Color(0xFF94A3B8),
                            fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selected = isSelected,
                    icon = {
                        Icon(
                            imageVector = if(isSelected) screen.selectedIcon else screen.unselectedIcon,
                            contentDescription = screen.title
                        )
                    },
                    // ============================================
                    // THAY ĐỔI: Colors phù hợp dark theme
                    // ============================================
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF60A5FA),        // Blue-400
                        unselectedIconColor = Color(0xFF94A3B8),      // Slate-400
                        selectedTextColor = Color(0xFF60A5FA),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = Color(0xFF334155)            // Slate-700
                    ),
                    onClick = {
                        navController.navigate(route = screen.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

object Screen{
    const val login = "login"
    const val sensor = "sensor"
    const val graph = "graph"
    const val setting = "setting"
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun Dht22AppBar(isDeviceOnline: Boolean, currentRoute: NavDestination?){
    val title = when (currentRoute?.route) {
        Screen.login -> "Đăng nhập"
        Screen.sensor -> "Cảm biến"
        Screen.graph -> "Biểu đồ"
        Screen.setting -> "Cài đặt"
        else -> "Demo1"
    }
    if (currentRoute?.route == Screen.login) return

    TopAppBar(
        title = {
            // ============================================
            // CẢI THIỆN: Title màu trắng, bold
            // ============================================
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        // ============================================
        // THAY ĐỔI: TopAppBar màu dark
        // ============================================
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF1E293B),  // Slate-800
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        actions = {
            DeviceState(isOnline = isDeviceOnline)
        }
    )
}

@Composable
fun DeviceState(isOnline: Boolean){
    // LOGIC GIỮ NGUYÊN
    val statuscolor = if(isOnline) Color(0xFF10B981) else Color(0xFFEF4444)
    val statustext = if(isOnline) "Online" else "Offline"
    val containerColor = Color.White.copy(alpha = 0.15f)

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
                color = Color.White
            )
        }
    }
}

@Composable
fun Dht22app(
    navController: NavHostController = rememberNavController(),
    viewModel: SensorViewModel = viewModel()
){
    // LOGIC GIỮ NGUYÊN
    val uiState by viewModel.uiState.collectAsState()
    val currentRoute = rememberCurrentRoute(navController)

    Scaffold(
        topBar = {
            Dht22AppBar(uiState.isConnect, currentRoute)
        },
        bottomBar = {
            MyNavigationBar(navController = navController, currentRoute)
        },
        containerColor = Color(0xFF0F172A)
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
                SensorUi()
            }
            composable(Screen.graph) {
                GraphUi()
                Text(text = "hello")
            }
            composable(route = Screen.setting) {
                SettingUi(
                    onLogout = {
                        navController.navigate(Screen.login)
                    }
                )
            }
        }
    }
}