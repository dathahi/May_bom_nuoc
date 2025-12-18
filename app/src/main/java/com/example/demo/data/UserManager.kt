package com.example.demo.data


object UserManager {
    var currentUsername: String? = null
    var currentRole: String? = null

    fun isLoggedIn(): Boolean {
        return currentUsername != null
    }

    fun logout() {
        currentUsername = null
        currentRole = null
    }
}