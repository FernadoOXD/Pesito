package com.upchiapas.kt_template.pesito.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.upchiapas.kt_template.R

@Composable
fun EditarLimiteDialog(
    limiteActual: Double,
    onGuardar: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var limiteText by remember { mutableStateOf(limiteActual.toInt().toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.editar_limite_titulo)) },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.editar_limite_subtitulo),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = limiteText,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.all { it.isDigit() || it == '.' }) {
                            limiteText = input
                        }
                    },
                    label = { Text(stringResource(R.string.nuevo_limite_gasto)) },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val nuevo = limiteText.toDoubleOrNull()
                    if (nuevo != null && nuevo > 0) {
                        onGuardar(nuevo)
                    }
                }
            ) {
                Text(stringResource(R.string.guardar_limite))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}
