package com.example.supletanes.ui.screens.products

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.supletanes.data.db.entities.Suplemento
import com.example.supletanes.ui.screens.products.viewmodel.SuplementoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class ProductsScreenState(
    val suplementos: List<Suplemento>,
    val visibleItems: MutableMap<Int, Boolean>,
    val editedItemId: MutableState<Int?>,
    val showDialog: Boolean,
    val suplementoAEditar: Suplemento?,
    val onShowDialogChange: (Boolean) -> Unit,
    val onEditChange: (Suplemento?) -> Unit,
    val onEditedItem: (Int) -> Unit,
    val mostrarSnackbarConDeshacer: (Suplemento) -> Unit
)

@Composable
fun rememberProductsScreenState(
    viewModel: SuplementoViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
): ProductsScreenState {
    val suplementos by viewModel.allSupplements.collectAsState(initial = emptyList())
    val visibleItems = remember { mutableStateMapOf<Int, Boolean>() }
    val editedItemId = remember { mutableStateOf<Int?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var suplementoAEditar by remember { mutableStateOf<Suplemento?>(null) }

    LaunchedEffect(suplementos) {
        suplementos.forEach { suplemento ->
            visibleItems[suplemento.id] = true
        }
    }

    val mostrarSnackbarConDeshacer: (Suplemento) -> Unit = { suplemento ->
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = "${suplemento.nombre} eliminado",
                actionLabel = "Deshacer"
            )
            if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                viewModel.insert(suplemento)
            }
        }
    }

    return ProductsScreenState(
        suplementos = suplementos,
        visibleItems = visibleItems,
        editedItemId = editedItemId,
        showDialog = showDialog,
        suplementoAEditar = suplementoAEditar,
        onShowDialogChange = { showDialog = it },
        onEditChange = { suplementoAEditar = it },
        onEditedItem = { editedItemId.value = it },
        mostrarSnackbarConDeshacer = mostrarSnackbarConDeshacer
    )
}