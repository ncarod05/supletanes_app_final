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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class PlanViewModel : ViewModel() {

    private val _showCalendarDialog = MutableStateFlow(false)
    val showCalendarDialog = _showCalendarDialog.asStateFlow()

    private val _showTimeDialog = MutableStateFlow(false)
    val showTimeDialog = _showTimeDialog.asStateFlow()

    private var _selectedDate: LocalDate? = null

    fun onCalendarIconClick() {
        _showCalendarDialog.update { true }
    }

    fun onDismissCalendar() {
        _showCalendarDialog.update { false }
    }

    fun onDismissTimeDialog() {
        _showTimeDialog.update { false }
    }

    fun onDateSelected(dateInMillis: Long) {
        // Guardamos la fecha seleccionada y mostramos el diálogo de la hora
        _selectedDate = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        _showCalendarDialog.update { false }
        _showTimeDialog.update { true }
    }

    // Nueva función para crear el recordatorio llamando a la API
    fun crearRecordatorio(hour: Int, minute: Int) {
        _showTimeDialog.update { false }

        _selectedDate?.let { date ->
            val fechaHora = LocalDateTime.of(date, LocalTime.of(hour, minute))

            viewModelScope.launch {
                try {
                    val nuevoRecordatorio = Recordatorio(
                        id = 0, // El ID será generado por el backend
                        idUsuario = "user123", // TODO: Reemplazar con el ID del usuario real
                        mensaje = "Recordatorio de tu plan.",
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