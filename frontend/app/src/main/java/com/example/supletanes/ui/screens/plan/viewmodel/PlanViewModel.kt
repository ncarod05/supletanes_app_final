package com.example.supletanes.ui.screens.plan.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supletanes.data.model.Recordatorio
import com.example.supletanes.data.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class PlanViewModel : ViewModel() {

    private val _showCalendarDialog = MutableStateFlow(false)
    val showCalendarDialog = _showCalendarDialog.asStateFlow()

    fun onCalendarIconClick() {
        _showCalendarDialog.update { true }
    }

    fun onDismissCalendar() {
        _showCalendarDialog.update { false }
    }

    // Nueva función para crear el recordatorio llamando a la API
    fun crearRecordatorio(dateInMillis: Long) {
        viewModelScope.launch {
            try {
                // Convertir los milisegundos a LocalDateTime
                val fechaHora = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(dateInMillis),
                    ZoneId.systemDefault()
                )

                // Crear el objeto Recordatorio (usamos un ID de usuario de ejemplo)
                val nuevoRecordatorio = Recordatorio(
                    id = 0, // El ID será generado por el backend
                    idUsuario = "user123", // TODO: Reemplazar con el ID del usuario real
                    mensaje = "Recordatorio de tu plan.",
                    fechaHora = fechaHora
                )

                // Llamar a la API
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
        // Ocultar el diálogo después de la selección
        _showCalendarDialog.update { false }
    }
}