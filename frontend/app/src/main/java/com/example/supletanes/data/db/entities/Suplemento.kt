package com.example.supletanes.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suplemento")
data class Suplemento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int
)