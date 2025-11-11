package com.example.supletanes.ui.screens.products

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supletanes.data.db.entities.Suplemento
import com.example.supletanes.ui.screens.products.components.AddSupplementDialog
import com.example.supletanes.ui.screens.products.components.SuplementoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScaffold(
    state: ProductsScreenState,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onAddSupplement: (Suplemento) -> Unit,
    onUpdateSupplement: (Suplemento) -> Unit,
    onDeleteSupplement: (Suplemento) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Suplementos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = true,
                enter = scaleIn(initialScale = 0.8f, animationSpec = tween(300)),
                exit = scaleOut(targetScale = 0.8f, animationSpec = tween(200))
            ) {
                FloatingActionButton(onClick = { state.onShowDialogChange(true) }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Suplemento")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (state.suplementos.isEmpty()) {
                item {
                    Text(
                        text = "No hay suplementos en el catálogo. ¡Añade uno con el botón '+'!",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(state.suplementos, key = { it.id }) { suplemento ->
                    val isVisible = state.visibleItems[suplemento.id] ?: true
                    val isEdited = state.editedItemId.value == suplemento.id

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = if (isEdited) {
                            scaleIn(tween(300)) + fadeIn(tween(300))
                        } else {
                            fadeIn(tween(300)) + slideInVertically(initialOffsetY = { it / 4 })
                        },
                        exit = fadeOut(tween(300)) + slideOutVertically(targetOffsetY = { it / 4 })
                    ) {
                        SuplementoItem(
                            suplemento = suplemento,
                            onEdit = { state.onEditChange(suplemento) },
                            onDelete = {
                                state.visibleItems[suplemento.id] = false
                                scope.launch {
                                    delay(300)
                                    onDeleteSupplement(suplemento)
                                    state.mostrarSnackbarConDeshacer(suplemento)
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Diálogo para añadir suplemento
        if (state.showDialog) {
            AddSupplementDialog(
                onDismissRequest = { state.onShowDialogChange(false) },
                onConfirm = { newSupplement ->
                    onAddSupplement(newSupplement)
                    state.onShowDialogChange(false)
                }
            )
        }

        // Diálogo para editar suplemento
        AnimatedVisibility(
            visible = state.suplementoAEditar != null,
            enter = fadeIn(tween(300)) + scaleIn(initialScale = 0.9f),
            exit = fadeOut(tween(200)) + scaleOut(targetScale = 0.9f)
        ) {
            state.suplementoAEditar?.let { suplemento ->
                AddSupplementDialog(
                    suplementoInicial = suplemento,
                    onDismissRequest = { state.onEditChange(null) },
                    onConfirm = { suplementoEditado ->
                        onUpdateSupplement(suplementoEditado)
                        state.onEditedItem(suplementoEditado.id)
                        state.onEditChange(null)

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "${suplementoEditado.nombre} actualizado correctamente",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                )
            }
        }
    }
}