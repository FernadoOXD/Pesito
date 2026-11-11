package com.upchiapas.kt_template

import android.app.Application
import com.upchiapas.kt_template.pesito.di.PesitoModule

class KtApplication : Application() {

    val pesitoModule: PesitoModule by lazy {
        PesitoModule(this)
    }
}
