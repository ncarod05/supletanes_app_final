package com.example.supletanes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supletanes.data.model.Producto
import com.example.supletanes.data.model.ProductoRequest
import com.example.supletanes.repository.ProductoRepository
import kotlinx.coroutines.launch

class ProductoViewModel(private val repository: ProductoRepository) : ViewModel() {

    private val _productoCreado = MutableLiveData<Producto?>()
    val productoCreado: LiveData<Producto?> = _productoCreado

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun crearProducto(request: ProductoRequest) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.crearProducto(request)

            result.onSuccess { producto ->
                _productoCreado.value = producto
                _error.value = null
            }

            result.onFailure { exception ->
                _productoCreado.value = null
                _error.value = exception.message
            }

            _loading.value = false
        }
    }
}