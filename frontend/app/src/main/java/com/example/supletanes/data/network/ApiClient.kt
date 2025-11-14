package com.example.supletanes.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // URL base para conectar desde el emulador de Android al localhost de la máquina
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Interceptor para loggear las peticiones y respuestas (muy útil para debug)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Construcción de la instancia de Retrofit
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Exponer el servicio de la API para ser usado en el resto de la app
    val recordatorioApiService: RecordatorioApiService by lazy {
        retrofit.create(RecordatorioApiService::class.java)
    }
}
