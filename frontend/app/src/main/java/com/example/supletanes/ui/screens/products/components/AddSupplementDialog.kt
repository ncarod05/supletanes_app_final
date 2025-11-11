package com.example.supletanes.ui.screens.products.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.supletanes.data.db.entities.Suplemento
import com.example.supletanes.utils.validarSuplemento

@Composable
fun AddSupplementDialog(
    suplementoInicial: Suplemento? = null,
    onDismissRequest: () -> Unit,
    onConfirm: (Suplemento) -> Unit
) {
    var nombre by remember { mutableStateOf(suplementoInicial?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(suplementoInicial?.descripcion ?: "") }
    var precio by remember { mutableStateOf(suplementoInicial?.precio?.toString() ?: "") }
    var stock by remember { mutableStateOf(suplementoInicial?.stock?.toString() ?: "") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var precioError by remember { mutableStateOf<String?>(null) }
    var stockError by remember { mutableStateOf<String?>(null) }

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(tween(300)) + scaleIn(initialScale = 0.95f),
        exit = fadeOut(tween(200)) + scaleOut(targetScale = 0.95f)
    ) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = if (suplementoInicial != null) "Editar Suplemento" else "Añadir Nuevo Suplemento",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    TextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            nombreError = when {
                                nombre.isBlank() -> "El nombre no puede estar vacío"
                                nombre.length < 3 -> "El nombre debe tener al menos 3 caracteres"
                                else -> null
                            }
                        },
                        label = { Text("Nombre del suplemento") },
                        isError = nombreError != null,
                        singleLine = true
                    )
                    if (nombreError != null) {
                        Text(
                            text = nombreError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = precio,
                        onValueChange = {
                            precio = it
                            val precioDouble = precio.replace(',', '.').toDoubleOrNull()
                            precioError = when {
                                precioDouble == null -> "El precio debe ser un número válido"
                                precioDouble <= 0.0 -> "El precio debe ser mayor a 0"
                                else -> null
                            }
                        },
                        label = { Text("Precio") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = precioError != null,
                        singleLine = true
                    )
                    if (precioError != null) {
                        Text(
                            text = precioError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = stock,
                        onValueChange = {
                            stock = it
                            val stockInt = stock.toIntOrNull()
                            stockError = when {
                                stockInt == null -> "El stock debe ser un número entero"
                                stockInt < 0 -> "El stock no puede ser negativo"
                                else -> null
                            }
                        },
                        label = { Text("Stock inicial") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = stockError != null,
                        singleLine = true
                    )
                    if (stockError != null) {
                        Text(
                            text = stockError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val result = validarSuplemento(nombre, precio, stock)

                        nombreError = result.nombreError
                        precioError = result.precioError
                        stockError = result.stockError

                        if (result.isValid) {
                            val suplemento = Suplemento(
                                id = suplementoInicial?.id ?: 0,
                                nombre = nombre,
                                descripcion = descripcion,
                                precio = precio.replace(',', '.').toDouble(),
                                stock = stock.toInt()
                            )
                            onConfirm(suplemento)
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancelar")
                }
            }
        )
    }
}
