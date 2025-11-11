package com.example.supletanes.ui.screens.products.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.supletanes.data.db.AppDatabase
import com.example.supletanes.data.db.entities.Suplemento
import com.example.supletanes.data.repository.SuplementoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SuplementoViewModel(private val repository: SuplementoRepository) : ViewModel() {

    // Exponemos la lista de suplementos como un StateFlow. Compose puede reaccionar a sus cambios.
    val allSupplements: StateFlow<List<Suplemento>> = repository.allSupplements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun findById(id: Long, onResult: (Suplemento?) -> Unit) = viewModelScope.launch {
        val suplemento = repository.findById(id)
        onResult(suplemento)
    }

    // Función para añadir un nuevo suplemento. La llamaremos desde la UI.
    fun insert(supplement: Suplemento) = viewModelScope.launch {
        repository.insert(supplement)
    }

    fun update(suplemento: Suplemento) = viewModelScope.launch {
        repository.update(suplemento)
    }

    // Función para eliminar un suplemento. La llamaremos desde la UI.
    fun delete(id: Int) = viewModelScope.launch {
        repository.delete(id)
    }
}

// ViewModelFactory para poder pasar el SuplementoRepository al crear el ViewModel.
class SuplementoViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SuplementoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Aquí es donde se crea la instancia real de la base de datos y el repositorio.
            val database = AppDatabase.getDatabase(application)
            val repository = SuplementoRepository(database.suplementoDao())
            return SuplementoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}