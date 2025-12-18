package com.example.demo.ui.setting

import android.webkit.WebViewDatabase
import androidx.lifecycle.ViewModel
import com.example.demo.data.Dbresult
import com.example.demo.data.SensorData
import com.example.demo.data.Setting
import com.example.demo.data.changePass
import com.example.demo.data.firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

data class SettingUIState(
    val sensorData: Setting = Setting(),
    val errMessage: String = "",
    val sucMessage: String = ""
)

class SettingViewModel(private val database : firebase= firebase()): ViewModel(){

    private val _uistate = MutableStateFlow(SettingUIState())

    val uiState : StateFlow<SettingUIState> = _uistate.asStateFlow()

    fun display_the_threshold(){
        database.setting_sensor{
            dbresult ->
            when(dbresult){
                is Dbresult.Error -> {
                    _uistate.update { it.copy(errMessage = dbresult.mess) }
                }
                is Dbresult.Success -> {
                    _uistate.update { it.copy(sensorData = dbresult.data) }
                }
            }
        }
    }
    fun adj_the_threshold(hum: Float,temp: Float){
        database.write_db_setting(hum = hum, temp = temp){
                dbresult ->
            when(dbresult){
                is Dbresult.Error -> {
                    _uistate.update { it.copy(errMessage = dbresult.mess) }
                }
                is Dbresult.Success -> {
                    _uistate.update { it.copy(sucMessage = dbresult.data) }
                }
            }
        }
    }

    fun updateTempThreshold(newValue: Float) {
        val roundedValue = (newValue * 10).roundToInt() / 10f
        _uistate.update { currentState ->
            val newSetting = currentState.sensorData.copy(thresholdTemp = roundedValue)
            currentState.copy(sensorData = newSetting)
        }
    }

    fun updateHumidityThreshold(newValue: Float) {
        val roundedValue = (newValue * 10).roundToInt() / 10f
        _uistate.update { currentState ->
            val newSetting = currentState.sensorData.copy(thresholdHum = roundedValue)
            currentState.copy(sensorData = newSetting)
        }
    }

    fun saveSettings() {

        val currentSetting = _uistate.value.sensorData
        val currentTemp = currentSetting.thresholdTemp
        val currentHum = currentSetting.thresholdHum
        adj_the_threshold(hum = currentHum, temp = currentTemp)
    }

    fun changePass(Pass: changePass){
        database.changePass(Pass){
            dbresult -> when(dbresult){
                is Dbresult.Success -> {
                    _uistate.update { it.copy(sucMessage = dbresult.data) }
                }
                is Dbresult.Error -> {
                    _uistate.update { it.copy(errMessage = dbresult.mess) }
                }
            }
        }

    }

    fun resetMessages(){
        _uistate.update { it.copy(sucMessage = "", errMessage = "") }
    }

    override fun onCleared() {
        database.removeSettingListener()
        super.onCleared()
    }
}