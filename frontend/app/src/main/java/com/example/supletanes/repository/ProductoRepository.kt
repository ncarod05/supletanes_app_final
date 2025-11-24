package com.example.supletanes.repository

import com.example.supletanes.data.api.ProductoApiService
import com.example.supletanes.data.model.Producto
import com.example.supletanes.data.model.ProductoRequest

class ProductoRepository(private val apiService: ProductoApiService) {

    suspend fun crearProducto(request: ProductoRequest): Result<Producto> {
        return try {
            val response = apiService.crearProducto(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear producto: código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}