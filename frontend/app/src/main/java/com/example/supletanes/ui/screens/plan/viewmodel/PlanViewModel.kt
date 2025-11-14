package com.example.supletanes.ui.screens.plan.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.supletanes.data.model.Recordatorio
import com.example.supletanes.data.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.UUID

class PlanViewModel(application: Application) : AndroidViewModel(application) {

    private val _showCalendarDialog = MutableStateFlow(false)
    val showCalendarDialog = _showCalendarDialog.asStateFlow()

    private val _showTimeDialog = MutableStateFlow(false)
    val showTimeDialog = _showTimeDialog.asStateFlow()

    private val _showMensajeDialog = MutableStateFlow(false)
    val showMensajeDialog = _showMensajeDialog.asStateFlow()

    private var _selectedDate: LocalDate? = null
    private var _selectedDateTime: LocalDateTime? = null

    // Lógica para obtener/crear un ID de dispositivo único
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

    fun onCalendarIconClick() {
        _showCalendarDialog.update { true }
    }

    fun onDismissCalendar() {
        _showCalendarDialog.update { false }
    }

    fun onDismissTimeDialog() {
        _showTimeDialog.update { false }
    }

    fun onDismissMensajeDialog() {
        _showMensajeDialog.update { false }
    }

    fun onDateSelected(dateInMillis: Long) {
        _selectedDate = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        _showCalendarDialog.update { false }
        _showTimeDialog.update { true }
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        _selectedDate?.let {
            _selectedDateTime = LocalDateTime.of(it, LocalTime.of(hour, minute))
        }
        _showTimeDialog.update { false }
        _showMensajeDialog.update { true }
    }

    fun crearRecordatorio(mensaje: String) {
        _showMensajeDialog.update { false }

        _selectedDateTime?.let { fechaHora ->
            viewModelScope.launch {
                try {
                    val nuevoRecordatorio = Recordatorio(
                        id = 0,
                        idUsuario = getDeviceUserId(), // Usamos el ID de dispositivo único
                        mensaje = mensaje,
                        fechaHora = fechaHora
                    )

                    val response = ApiClient.recordatorioApiService.crearRecordatorio(nuevoRecordatorio)

                    if (response.isSuccessful) {
                        Log.d("PlanViewModel", "Recordatorio creado con éxito: ${response.body()}")
                    } else {
                        Log.e("PlanViewModel", "Error al crear recordatorio: ${response.errorBody()?.string()}")
                    }

                } catch (e: Exception) {
                    Log.e("PlanViewModel", "Excepción al crear recordatorio", e)
                }
            }
        }
    }
}