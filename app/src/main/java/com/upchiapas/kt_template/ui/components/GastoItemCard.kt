package com.upchiapas.kt_template.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.upchiapas.kt_template.R
import com.upchiapas.kt_template.data.model.Gasto

@Composable
fun GastoItemCard(
    gasto: Gasto,
    nombreCategoria: String,
    onEditarClick: (Gasto) -> Unit,
    onEliminarClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = gasto.concepto,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$nombreCategoria • ${gasto.metodoPago}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    text = "-$${String.format("%.2f", gasto.monto)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onEditarClick(gasto) }) {
                    Text(stringResource(R.string.btn_editar))
                }
                Button(
                    onClick = { onEliminarClick(gasto.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.btn_eliminar))
                }
            }
        }
    }
}