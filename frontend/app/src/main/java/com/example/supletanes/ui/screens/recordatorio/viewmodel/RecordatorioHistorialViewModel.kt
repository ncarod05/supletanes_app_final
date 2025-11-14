package com.example.supletanes.ui.screens.recordatorio.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.supletanes.data.model.Recordatorio
import com.example.supletanes.data.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class RecordatorioHistorialUiState(
    val recordatorios: List<Recordatorio> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class RecordatorioHistorialViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RecordatorioHistorialUiState())
    val uiState: StateFlow<RecordatorioHistorialUiState> = _uiState.asStateFlow()

    init {
        obtenerRecordatorios()
    }

    private fun getDeviceUserId(): String {
        val sharedPreferences = getApplication<Application>().getSharedPreferences(
            "app_prefs",
            Context.MODE_PRIVATE
        )
        var userId = sharedPreferences.getString("user_id", null)
        if (userId == null) {
            userId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString("user_id", userId).apply()
        }
        return userId
    }

    fun obtenerRecordatorios() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = ApiClient.recordatorioApiService.obtenerRecordatoriosPorUsuario(getDeviceUserId())
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(recordatorios = response.body() ?: emptyList(), isLoading = false)
                    }
                } else {
                    _uiState.update { it.copy(error = "Error al obtener recordatorios", isLoading = false) }
                }
            } catch (e: Exception) {
                Log.e("HistorialViewModel", "Excepción al obtener recordatorios", e)
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun eliminarRecordatorio(id: Long) {
        viewModelScope.launch {
            try {
                val response = ApiClient.recordatorioApiService.eliminarRecordatorio(id)
                if (response.isSuccessful) {
                    // Refrescar la lista después de borrar
                    obtenerRecordatorios()
                } else {
                    _uiState.update { it.copy(error = "Error al eliminar el recordatorio") }
                }
            } catch (e: Exception) {
                Log.e("HistorialViewModel", "Excepción al eliminar recordatorio", e)
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    // Aquí añadiremos la lógica para actualizar en el futuro
}