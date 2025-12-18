//package com.example.demo.data
//
//
//object UserManager {
//    var currentUsername: String? = null
//    var currentRole: String? = null
//
//    fun isLoggedIn(): Boolean {
//        return currentUsername != null
//    }
//
//    fun logout() {
//        currentUsername = null
//        currentRole = null
//    }
//}
package com.example.demo.data

object UserManager {
    var currentUsername: String? = null
    var currentRole: String? = null

    var currentHistoryData: HistoryData = HistoryData()

    fun isLoggedIn(): Boolean {
        return currentUsername != null
    }

    fun logout() {
        currentUsername = null
        currentRole = null
        currentHistoryData = HistoryData()
    }

    fun getLatestTimestamp(): Long {
        val humTimestamp = currentHistoryData.humidityHistory.lastOrNull()?.timestamp ?: 0L
        val tempTimestamp = currentHistoryData.temperatureHistory.lastOrNull()?.timestamp ?: 0L
        return maxOf(humTimestamp, tempTimestamp)
    }

    fun isOnline(): Boolean {
        val latestTimestamp = getLatestTimestamp()
        if (latestTimestamp == 0L) return false
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - latestTimestamp
        return timeDifference <= 5 * 60 * 1000 // 5 phut
    }
}