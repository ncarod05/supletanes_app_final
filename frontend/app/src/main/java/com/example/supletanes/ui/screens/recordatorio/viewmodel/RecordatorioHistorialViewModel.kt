package com.example.supletanes.ui.screens.recordatorio.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.supletanes.data.model.Recordatorio
import com.example.supletanes.data.model.RecordatorioRequest
import com.example.supletanes.data.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.UUID

data class RecordatorioHistorialUiState(
    val recordatorios: List<Recordatorio> = emptyList(),
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null
)

@RequiresApi(Build.VERSION_CODES.O)
class RecordatorioHistorialViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RecordatorioHistorialUiState())
    val uiState: StateFlow<RecordatorioHistorialUiState> = _uiState.asStateFlow()

    // --- Dialog state management (like PlanViewModel) ---
    private val _showCalendarDialog = MutableStateFlow(false)
    val showCalendarDialog = _showCalendarDialog.asStateFlow()

    private val _showTimeDialog = MutableStateFlow(false)
    val showTimeDialog = _showTimeDialog.asStateFlow()

    private val _showMensajeDialog = MutableStateFlow(false)
    val showMensajeDialog = _showMensajeDialog.asStateFlow()

    private var _selectedDate: LocalDate? = null
    private var _selectedDateTime: LocalDateTime? = null

    init {
        obtenerRecordatorios()
    }

    // --- Dialog control functions ---
    fun onCalendarIconClick() { _showCalendarDialog.value = true }
    fun onDismissCalendar() { _showCalendarDialog.value = false }
    fun onDismissTimeDialog() { _showTimeDialog.value = false }
    fun onDismissMensajeDialog() { _showMensajeDialog.value = false }

    fun onDateSelected(dateInMillis: Long) {
        _selectedDate = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        _showCalendarDialog.value = false
        _showTimeDialog.value = true
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        _selectedDate?.let {
            _selectedDateTime = LocalDateTime.of(it, LocalTime.of(hour, minute))
        }
        _showTimeDialog.value = false
        _showMensajeDialog.value = true
    }

    private fun getDeviceUserId(): String {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
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
                    _uiState.update { it.copy(recordatorios = response.body() ?: emptyList(), isLoading = false) }
                } else {
                    _uiState.update { it.copy(snackbarMessage = "Error al obtener recordatorios", isLoading = false) }
                }
            } catch (e: Exception) {
                Log.e("HistorialViewModel", "Excepción al obtener recordatorios", e)
                _uiState.update { it.copy(snackbarMessage = e.message, isLoading = false) }
            }
        }
    }

    fun crearRecordatorio(mensaje: String) = viewModelScope.launch {
        _showMensajeDialog.value = false
        _selectedDateTime?.let { fechaHora ->
            _uiState.update { it.copy(isLoading = true) }
            val request = RecordatorioRequest(
                idUsuario = getDeviceUserId(),
                mensaje = mensaje,
                fechaHora = fechaHora
            )
            try {
                val response = ApiClient.recordatorioApiService.crearRecordatorio(request)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(snackbarMessage = "Recordatorio creado") }
                    obtenerRecordatorios() // Refresh list
                } else {
                    _uiState.update { it.copy(snackbarMessage = "Error al crear recordatorio") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(snackbarMessage = e.message) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun actualizarRecordatorio(recordatorio: Recordatorio) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val response = ApiClient.recordatorioApiService.actualizarRecordatorio(recordatorio.id, recordatorio)
            if (response.isSuccessful) {
                _uiState.update { it.copy(snackbarMessage = "Recordatorio actualizado") }
                obtenerRecordatorios() // Refresh list
            } else {
                _uiState.update { it.copy(snackbarMessage = "Error al actualizar recordatorio") }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(snackbarMessage = e.message) }
        }
        _uiState.update { it.copy(isLoading = false) }
    }

    fun eliminarRecordatorio(id: Long) {
        viewModelScope.launch {
            try {
                val response = ApiClient.recordatorioApiService.eliminarRecordatorio(id)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(snackbarMessage = "Recordatorio eliminado") }
                    obtenerRecordatorios()
                } else {
                    _uiState.update { it.copy(snackbarMessage = "Error al eliminar el recordatorio") }
                }
            } catch (e: Exception) {
                Log.e("HistorialViewModel", "Excepción al eliminar recordatorio", e)
                _uiState.update { it.copy(snackbarMessage = e.message) }
            }
        }
    }

    fun limpiarMensaje() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}