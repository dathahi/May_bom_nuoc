package com.example.demo.data

data class User (
    val username : String = "",
    val password : String = "",
)

data class SensorData(
    val isconnect: Boolean = false,
    val humidity: Float = 0f,
    val temperature: Float = 0f,
    val timestamp: Long = 0L
) {
    fun isValid(): Boolean {
        return temperature in -40f..80f &&
                humidity in 0f..80f &&
                timestamp > 0

    }
}


data class Setting(
    val thresholdTemp: Float = 0f,
    val thresholdHum: Float = 0f
)

sealed class Dbresult<out T>{

    data class Success<out T>(val data: T): Dbresult<T>()

    data class Error(val mess: String): Dbresult<Nothing>()

}

