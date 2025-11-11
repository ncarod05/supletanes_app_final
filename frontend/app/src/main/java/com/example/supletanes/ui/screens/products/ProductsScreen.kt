@file:OptIn(ExperimentalAnimationApi::class)

package com.example.supletanes.ui.screens.products

import android.app.Application
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supletanes.ui.screens.products.viewmodel.SuplementoViewModel
import com.example.supletanes.ui.screens.products.viewmodel.SuplementoViewModelFactory

@Composable
fun ProductsScreen() {
    val viewModel: SuplementoViewModel = viewModel(
        factory = SuplementoViewModelFactory(LocalContext.current.applicationContext as Application)
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val state = rememberProductsScreenState(
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
        scope = scope
    )

    ProductsScaffold(
        state = state,
        snackbarHostState = snackbarHostState,
        scope = scope,
        onAddSupplement = { viewModel.insert(it) },
        onUpdateSupplement = { viewModel.update(it) },
        onDeleteSupplement = { viewModel.delete(it.id) }
    )
}