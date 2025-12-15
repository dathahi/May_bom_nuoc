package com.example.demo.ui.login

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.demo.data.Dbresult
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.demo.data.firebase
import kotlinx.coroutines.flow.update

data class LoginUiState(
    var err: String = "",
    var isSuccess: Boolean = false,
    var isLoading: Boolean = false
)


class LoginViewModel( private val database: firebase = firebase()): ViewModel(){
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun login(
        username: String ,
        password: String
    ){
        if(username.isBlank() || password.isBlank()){
            _uiState.value = _uiState.value.copy(err = "Điền đầy đủ thông tin")
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, err = "")

        database.read_db(username,password){
            dbresult ->
            when(dbresult){
                 is Dbresult.Error -> {
                     _uiState.value = _uiState.value.copy(
                         err = dbresult.mess,
                         isLoading = false
                     )
                 }
                is Dbresult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        err = "",
                        isLoading = false,
                        isSuccess = true
                    )
                }
            }
        }
    }

    fun reg(
        user: String,
        pass : String,
        onSucces: () -> Unit
    ){
        if(user.isBlank() || pass.isBlank()){
            _uiState.value = _uiState.value.copy(err = "Điền đầy đủ thông tin")
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        database.reg_db(user, pass) { result ->
            when (result) {
                is Dbresult.Success -> {
                    _uiState.update { it.copy(isLoading = false, err = "Đăng ký thành công! Hãy đăng nhập.") }
                    onSucces()
                }
                is Dbresult.Error -> {
                    _uiState.update { it.copy(isLoading = false, err = result.mess) }
                }
            }
        }
    }

    fun resetState(){
        _uiState.update { it.copy(isSuccess = false, err = "") }
    }

}