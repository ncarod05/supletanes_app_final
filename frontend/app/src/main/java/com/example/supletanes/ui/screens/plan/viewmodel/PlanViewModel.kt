package com.example.supletanes.ui.screens.plan.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlanViewModel : ViewModel() {

    private val _showCalendarDialog = MutableStateFlow(false)
    val showCalendarDialog = _showCalendarDialog.asStateFlow()

    fun onCalendarIconClick() {
        _showCalendarDialog.update { true }
    }

    fun onDismissCalendar() {
        _showCalendarDialog.update { false }
    }

    fun onDateSelected(dateInMillis: Long) {
        // Aquí irá la lógica para guardar la fecha o crear el evento
        _showCalendarDialog.update { false }
    }
}