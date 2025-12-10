package com.numero.storm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.numero.storm.data.model.ThemeMode
import com.numero.storm.ui.navigation.NumeroNavHost
import com.numero.storm.ui.theme.NumeroTheme
import com.numero.storm.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NumeroApp()
        }
    }
}

@Composable
fun NumeroApp() {
    val mainViewModel: MainViewModel = hiltViewModel()
    val settings by mainViewModel.settings.collectAsState()

    val isDarkTheme = when (settings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    NumeroTheme(darkTheme = isDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NumeroNavHost(
                startDestination = if (settings.hasCompletedOnboarding) {
                    "home"
                } else {
                    "onboarding"
                }
            )
        }
    }
}
