package com.example.supletanes.ui.screens.plan.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.supletanes.data.api.FoodApi
import com.example.supletanes.data.model.FoodDTO
import com.example.supletanes.data.model.Recordatorio
import com.example.supletanes.data.model.RecordatorioRequest
import com.example.supletanes.data.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    // Estados para mensajes
    private val _mensajeExito = MutableStateFlow<String?>(null)
    val mensajeExito: StateFlow<String?> = _mensajeExito.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    private val _foodInfo = MutableStateFlow<FoodDTO?>(null)
    val foodInfo: StateFlow<FoodDTO?> = _foodInfo

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://supletanesappfinal-production.up.railway.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val foodApi = retrofit.create(FoodApi::class.java)

    fun buscarAlimento(barcode: String) {
        viewModelScope.launch {
            try {
                Log.d("PlanViewModel", "Buscando alimento con código: $barcode")
                val result = foodApi.getFood(barcode)
                _foodInfo.value = result
                Log.d("PlanViewModel", "Resultado: $result")
            } catch (e: Exception) {
                Log.e("PlanViewModel", "Error al buscar alimento", e)
            }
        }
    }
    private val _searchResults = MutableStateFlow<List<FoodDTO>>(emptyList())
    val searchResults: StateFlow<List<FoodDTO>> = _searchResults

    fun buscarPorNombre(name: String) {
        viewModelScope.launch {
            try {
                val result = foodApi.searchFood(name)
                _searchResults.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


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

    @RequiresApi(Build.VERSION_CODES.O)
    fun onDateSelected(dateInMillis: Long) {
        _selectedDate = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        _showCalendarDialog.update { false }
        _showTimeDialog.update { true }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                    val nuevoRecordatorio = RecordatorioRequest(
                        idUsuario = getDeviceUserId(),
                        mensaje = mensaje,
                        fechaHora = fechaHora
                    )

                    val response = ApiClient.recordatorioApiService.crearRecordatorio(nuevoRecordatorio)

                    if (response.isSuccessful) {
                        val recordatorio = response.body()
                        Log.d("PlanViewModel", "Recordatorio creado con éxito: $recordatorio")

                        // mostrar mensaje de éxito al usuario
                        _mensajeExito.update { "Recordatorio creado exitosamente" }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("PlanViewModel", "Error al crear recordatorio: $errorBody")

                        _mensajeError.update { "Error al crear recordatorio: ${response.code()}" }
                    }

                } catch (e: Exception) {
                    Log.e("PlanViewModel", "Excepción al crear recordatorio", e)
                    _mensajeError.update { "Error de conexión: ${e.message}" }
                }
            }
        }
    }
}