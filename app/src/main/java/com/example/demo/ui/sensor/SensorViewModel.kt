package com.example.demo.ui.sensor

import androidx.lifecycle.ViewModel
import com.example.demo.data.Dbresult
import com.example.demo.data.SensorData
import com.example.demo.data.firebase
import com.example.demo.ui.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SensorUiState(
    val sensorData: SensorData = SensorData(),
    val errMessage: String = "",
    val isConnect: Boolean = false,
    val PopupAlert: Boolean = false,
    val AlertMessage: String = "",
    val isRefreshState: Boolean = false
)

class SensorViewModel(private val database: firebase = firebase()): ViewModel(){
    private val _uiState = MutableStateFlow(SensorUiState())
    val uiState: StateFlow<SensorUiState> = _uiState.asStateFlow()

    fun get_info_sensor(){
        database.info_sensor {
            dbresult ->
            when(dbresult){
                is Dbresult.Error -> {
                    _uiState.update { it.copy(
                        errMessage = dbresult.mess,
                    ) }
                }
                is Dbresult.Success->{
                    _uiState.update { it.copy(
                        errMessage = "",
                        isConnect = dbresult.data.isconnect,
                        sensorData = dbresult.data,
                    ) }
//                    CheckThreshold(dbresult.data.isconnect)
                }
            }

        }
    }

//    fun CheckThreshold(){
//
//    }

}