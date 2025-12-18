package com.example.demo.ui.graph

import androidx.lifecycle.ViewModel
import com.example.demo.data.Dbresult
import com.example.demo.data.HistoryData
import com.example.demo.data.firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GraphUiState(
    val historyData: HistoryData? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class GraphViewModel : ViewModel() {
    private val firebase = firebase()

    private val _uiState = MutableStateFlow(GraphUiState())
    val uiState: StateFlow<GraphUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory(limit: Int = 50) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        firebase.read_history(limit) { result ->
            when (result) {
                is Dbresult.Success -> {
                    _uiState.value = GraphUiState(
                        historyData = result.data,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is Dbresult.Error -> {
                    _uiState.value = GraphUiState(
                        historyData = null,
                        isLoading = false,
                        errorMessage = result.mess
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        firebase.removeHistoryListener()
    }
}