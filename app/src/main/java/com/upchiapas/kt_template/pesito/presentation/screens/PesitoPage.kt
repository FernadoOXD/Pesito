package com.upchiapas.kt_template.pesito.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upchiapas.kt_template.R
import com.upchiapas.kt_template.pesito.data.model.Gasto
import com.upchiapas.kt_template.pesito.presentation.components.*
import com.upchiapas.kt_template.pesito.presentation.viewmodels.PesitoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PesitoPage(viewModel: PesitoViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showFormulario by remember { mutableStateOf(false) }
    var showEditarLimiteDialog by remember { mutableStateOf(false) }
    var gastoAEditar by remember { mutableStateOf<Gasto?>(null) }

    val gastosFiltrados = remember(uiState.listaGastos, uiState.categoriaSeleccionadaId) {
        if (uiState.categoriaSeleccionadaId == null) {
            uiState.listaGastos
        } else {
            uiState.listaGastos.filter { it.categoriaId == uiState.categoriaSeleccionadaId }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.dashboard_title)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                gastoAEditar = null
                showFormulario = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar gasto")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ResumenCard(
                limite = uiState.limiteMensualGlobal,
                totalGastado = uiState.totalGastado,
                saldoDisponible = uiState.saldoDisponible,
                porcentaje = uiState.porcentajeConsumido,
                gastoMayorCat = uiState.gastoMayorConsumo,
                onEditarLimiteClick = { showEditarLimiteDialog = true },
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = stringResource(R.string.categorias_gasto),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            FiltroCategorias(
                categorias = uiState.categorias,
                categoriaSeleccionadaId = uiState.categoriaSeleccionadaId,
                onCategoriaSelected = { viewModel.seleccionarCategoriaFiltro(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.movimientos_recientes),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(gastosFiltrados, key = { it.id }) { gasto ->
                    val catNombre = uiState.categorias.find { it.id == gasto.categoriaId }?.nombre ?: ""
                    GastoItemCard(
                        gasto = gasto,
                        nombreCategoria = catNombre,
                        onEditarClick = {
                            gastoAEditar = it
                            showFormulario = true
                        },
                        onEliminarClick = { viewModel.eliminarGasto(it) }
                    )
                }
            }
        }

        if (showFormulario) {
            FormularioGastoBottomSheet(
                categorias = uiState.categorias,
                gastoAEditar = gastoAEditar,
                presupuestoRestante = uiState.saldoDisponible,
                onGuardar = { concepto, monto, catId, metodo, id ->
                    viewModel.registrarOGuardarGasto(concepto, monto, catId, metodo, id)
                    showFormulario = false
                },
                onDismiss = { showFormulario = false }
            )
        }

        if (showEditarLimiteDialog) {
            EditarLimiteDialog(
                limiteActual = uiState.limiteMensualGlobal,
                onGuardar = { nuevoLimite ->
                    viewModel.actualizarLimite(nuevoLimite)
                    showEditarLimiteDialog = false
                },
                onDismiss = { showEditarLimiteDialog = false }
            )
        }
    }
}
