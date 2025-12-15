//package com.example.demo.ui.login
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.graphics.Color
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//
//@Composable
//fun LoginUi(
//    onClickButton : () -> Unit,
//    viewModel: LoginViewModel = viewModel()
//){
//    var Username by remember { mutableStateOf("") }
//    var Password by remember { mutableStateOf("") }
//    val state by viewModel.uiState.collectAsState()
//    var showDialog by remember { mutableStateOf(false) }
//    var regUser by remember { mutableStateOf("") }
//    var regPass by remember { mutableStateOf("") }
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Đăng nhập",
//            fontWeight = FontWeight.Bold,
//            fontSize = 24.sp
//            )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        OutlinedTextField(
//            value = Username,
//            onValueChange = { Username = it },
//            label = {Text("Tên Đăng Nhập")},
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedTextField(
//            value = Password,
//            onValueChange = {Password = it},
//            label = {Text("Mật khẩu")},
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                viewModel.login(Username,Password)
//            },
//            enabled = !state.isLoading
//        ) {
//            LaunchedEffect(state.isSuccess) {
//                if(state.isSuccess){
//                    onClickButton()
//                    viewModel.resetState()
//                    showDialog = false
//                    regUser = ""
//                    regUser = ""
//                }
//            }
//            if (state.isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(20.dp),
//                    strokeWidth = 2.dp
//                )
//            } else {
//                Text("Đăng nhập")
//            }
//        }
//        Button(
//            {
//                showDialog = true
//            }){
//                Text(text = "Đăng Ký")
//            }
//        if (state.err.isNotEmpty()) {
//            val mauSac = if (state.err.contains("Đăng ký thành công! Hãy đăng nhập.")) Color.Green else Color.Red
//            Text(
//                text = state.err,
//                color = mauSac,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//        }
//    }
//    if(showDialog){
//        RegisterDialog(
//            userValue = regUser,
//            passValue = regPass,
//            isLoading = state.isLoading,
//            onUserChange = { regUser = it },
//            onPassChange = { regPass = it },
//            onDismiss = { showDialog = false },
//            onConfirm = {
//                viewModel.reg(regUser,regPass,
//                    onSucces = {
//                        showDialog = false
//                        regUser = ""
//                        regPass = ""
//                    })
//            })
//    }
//}
//
//@Composable
//fun RegisterDialog(
//    userValue: String,
//    passValue: String,
//    isLoading: Boolean,
//    onUserChange: (String) -> Unit,
//    onPassChange: (String) -> Unit,
//    onDismiss: () -> Unit,
//    onConfirm: () -> Unit
//){
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Đăng ký tài khoản") },
//        text = {
//            Column {
//                OutlinedTextField(value = userValue, onValueChange = onUserChange, label = { Text("User mới") })
//                Spacer(modifier = Modifier.height(8.dp))
//                OutlinedTextField(value = passValue, onValueChange = onPassChange, label = { Text("Pass mới") })
//            }
//        },
//        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } },
//        confirmButton = {
//            Button(onClick = onConfirm) {
//                if(isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp)) else Text("Đăng ký")
//            }
//        }
//    )
//}
//
//

package com.example.demo.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginUi(
    onClickButton: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var regUser by remember { mutableStateOf("") }
    var regPass by remember { mutableStateOf("") }


    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onClickButton()
            viewModel.resetState()
            showDialog = false
            regUser = ""
            regPass = ""
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Xin Chào!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Đăng nhập để tiếp tục",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Tên đăng nhập") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val img = if(passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = {passwordVisible = !passwordVisible}) {
                    Icon(imageVector = img, contentDescription = description)
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.login(username, password) },
            enabled = !state.isLoading,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(text = "ĐĂNG NHẬP", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Chưa có tài khoản?", color = Color.Gray)
            TextButton(onClick = { showDialog = true }) {
                Text(text = "Đăng ký ngay", fontWeight = FontWeight.Bold)
            }
        }


        Spacer(modifier = Modifier.height(8.dp))


        AnimatedVisibility(visible = state.err.isNotEmpty()) {
            val isSuccessMsg = state.err.contains("thành công")
            val bgColor = if (isSuccessMsg) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
            val textColor = if (isSuccessMsg) Color(0xFF2E7D32) else Color(0xFFC62828)

            Card(
                colors = CardDefaults.cardColors(containerColor = bgColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = state.err,
                    color = textColor,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    if (showDialog) {
        RegisterDialog(
            userValue = regUser,
            passValue = regPass,
            isLoading = state.isLoading,
            onUserChange = { regUser = it },
            onPassChange = { regPass = it },
            onDismiss = { showDialog = false },
            onConfirm = {
                viewModel.reg(regUser, regPass, onSucces = {
                    showDialog = false
                    regUser = ""
                    regPass = ""
                })
            }
        )
    }
}

@Composable
fun RegisterDialog(
    userValue: String,
    passValue: String,
    isLoading: Boolean,
    onUserChange: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Tạo tài khoản mới", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text("Nhập thông tin bên dưới để đăng ký:", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = userValue,
                    onValueChange = onUserChange,
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = passValue,
                    onValueChange = onPassChange,
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Đăng ký")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy bỏ")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp)
    )
}
