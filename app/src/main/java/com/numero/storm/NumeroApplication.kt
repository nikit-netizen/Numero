package com.numero.storm

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class for Numero.
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection.
 */
@HiltAndroidApp
class NumeroApplication : Application()
