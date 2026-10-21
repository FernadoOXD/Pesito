package com.upchiapas.kt_template.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.upchiapas.kt_template.R
import com.upchiapas.kt_template.data.model.CategoriaGasto
import org.w3c.dom.Text

@Composable
fun FiltroCategorias(
    categorias: List<CategoriaGasto>,
    categoriaSeleccionadaId: String?,
    onCategoriaSelected: (String?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            FilterChip(
                selected = categoriaSeleccionadaId == null,
                onClick = { onCategoriaSelected(null) },
                label = { Text(stringResource(R.string.todas)) }
            )
        }
        items(categorias) { cat ->
            FilterChip(
                selected = categoriaSeleccionadaId == cat.id,
                onClick = { onCategoriaSelected(cat.id) },
                label = { Text(cat.nombre)}
            )
        }
    }
}