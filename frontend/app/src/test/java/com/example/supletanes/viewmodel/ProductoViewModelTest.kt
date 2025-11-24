package com.example.supletanes.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.supletanes.data.model.Producto
import com.example.supletanes.data.model.ProductoRequest
import com.example.supletanes.repository.ProductoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: ProductoRepository
    private lateinit var viewModel: ProductoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = ProductoViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando se crea producto exitosamente entonces productoCreado debe contener el producto`() = runTest {
        val request = ProductoRequest(
            nombre = "Manzana",
            descripcion = "Fruta fresca",
            precio = 1.5,
            stock = 100,
            categoria = "Frutas"
        )
        val productoEsperado = Producto(
            id = 1L,
            nombre = "Manzana",
            descripcion = "Fruta fresca",
            precio = 1.5,
            stock = 100,
            categoria = "Frutas"
        )

        coEvery { repository.crearProducto(request) } returns Result.success(productoEsperado)

        viewModel.crearProducto(request)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(productoEsperado, viewModel.productoCreado.value)
        assertNull(viewModel.error.value)
        assertEquals(false, viewModel.loading.value)
        coVerify(exactly = 1) { repository.crearProducto(request) }
    }

    @Test
    fun `cuando falla crear producto entonces error debe contener mensaje`() = runTest {
        val request = ProductoRequest(
            nombre = "Manzana",
            descripcion = "Fruta fresca",
            precio = 1.5,
            stock = 100,
            categoria = "Frutas"
        )
        val errorMsg = "Error de red"

        coEvery { repository.crearProducto(request) } returns Result.failure(Exception(errorMsg))

        viewModel.crearProducto(request)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.productoCreado.value)
        assertEquals(errorMsg, viewModel.error.value)
        assertEquals(false, viewModel.loading.value)
    }
}