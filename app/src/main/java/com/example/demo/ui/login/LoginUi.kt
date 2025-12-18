package com.example.demo.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)),  // Slate-950
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E293B)
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color(0xFF60A5FA)
                )

                Spacer(modifier = Modifier.height(16.dp))



                Text(
                    text = "Xin Chào!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Đăng nhập để tiếp tục",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8)
                )

                Spacer(modifier = Modifier.height(32.dp))


                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Tên đăng nhập", color = Color(0xFF94A3B8)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF60A5FA)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF60A5FA),
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF60A5FA)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mật khẩu", color = Color(0xFF94A3B8)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFF60A5FA)
                        )
                    },
                    visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val img = if(passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (passwordVisible) "Hide password" else "Show password"
                        IconButton(onClick = {passwordVisible = !passwordVisible}) {
                            Icon(
                                imageVector = img,
                                contentDescription = description,
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF60A5FA),
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF60A5FA)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))


                Button(
                    onClick = { viewModel.login(username, password) },
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB),  // Blue-600
                        disabledContainerColor = Color(0xFF334155)
                    )
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "ĐĂNG NHẬP",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Chưa có tài khoản?",
                        color = Color(0xFF94A3B8)
                    )
                    TextButton(onClick = { showDialog = true }) {
                        Text(
                            text = "Đăng ký ngay",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF60A5FA)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))


                AnimatedVisibility(visible = state.err.isNotEmpty()) {
                    val isSuccessMsg = state.err.contains("thành công")
                    val bgColor = if (isSuccessMsg)
                        Color(0xFF10B981).copy(alpha = 0.2f)
                    else
                        Color(0xFFEF4444).copy(alpha = 0.2f)
                    val borderColor = if (isSuccessMsg)
                        Color(0xFF10B981)
                    else
                        Color(0xFFEF4444)
                    val textColor = if (isSuccessMsg)
                        Color(0xFF10B981)
                    else
                        Color(0xFFEF4444)

                    Card(
                        colors = CardDefaults.cardColors(containerColor = bgColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
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
            Text(
                text = "Tạo tài khoản mới",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        text = {
            Column {
                Text(
                    "Nhập thông tin bên dưới để đăng ký:",
                    fontSize = 14.sp,
                    color = Color(0xFF94A3B8)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = userValue,
                    onValueChange = onUserChange,
                    label = { Text("Username", color = Color(0xFF94A3B8)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF60A5FA)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF60A5FA),
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF60A5FA)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = passValue,
                    onValueChange = onPassChange,
                    label = { Text("Password", color = Color(0xFF94A3B8)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFF60A5FA)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF60A5FA),
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF60A5FA)
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Đăng ký", color = Color.White)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy bỏ", color = Color(0xFF94A3B8))
            }
        },
        containerColor = Color(0xFF1E293B),
        shape = RoundedCornerShape(16.dp)
    )
}
