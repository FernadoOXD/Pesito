package com.upchiapas.kt_template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.upchiapas.kt_template.pesito.presentation.screens.PesitoPage
import com.upchiapas.kt_template.pesito.presentation.viewmodels.PesitoViewModel
import com.upchiapas.kt_template.pesito.presentation.viewmodels.PesitoViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: PesitoViewModel by viewModels {
        val app = application as KtApplication
        PesitoViewModelFactory(app.pesitoModule.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PesitoPage(viewModel = viewModel)
                }
            }
        }
    }
}