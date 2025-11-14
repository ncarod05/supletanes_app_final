package com.example.supletanes.data.network

import com.example.supletanes.data.model.Recordatorio
import retrofit2.Response
import retrofit2.http.*

interface RecordatorioApiService {

    @POST("api/v1/recordatorios")
    suspend fun crearRecordatorio(@Body recordatorio: Recordatorio): Response<Recordatorio>

    @GET("api/v1/recordatorios/usuario/{idUsuario}")
    suspend fun obtenerRecordatoriosPorUsuario(@Path("idUsuario") idUsuario: String): Response<List<Recordatorio>>

    @DELETE("api/v1/recordatorios/{id}")
    suspend fun eliminarRecordatorio(@Path("id") id: Long): Response<Void>
}
