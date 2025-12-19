package com.example.demo.data

object UserManager {
    var currentUsername: String? = null

    var currentHistoryData: HistoryData = HistoryData()

    fun getLatestTimestamp(): Long {
        val humTimestamp = currentHistoryData.humidityHistory.lastOrNull()?.timestamp ?: 0L
        val tempTimestamp = currentHistoryData.temperatureHistory.lastOrNull()?.timestamp ?: 0L
        val maxTs = maxOf(humTimestamp, tempTimestamp)

        return if (maxTs > 0 && maxTs < 10_000_000_000L) {
            maxTs * 1000
        } else {
            maxTs
        }
    }

    fun isOnline(): Boolean {
        val latestTimestamp = getLatestTimestamp()
        if (latestTimestamp == 0L) return false

        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - latestTimestamp
        return timeDifference <= 30 * 60 * 1000 // 30 phút
    }
}



