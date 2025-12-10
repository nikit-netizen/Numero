package com.numero.storm.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numero.storm.R
import com.numero.storm.data.model.AppLanguage
import com.numero.storm.data.model.ThemeMode
import com.numero.storm.domain.calculator.NumerologySystem
import com.numero.storm.ui.components.NumeroTopBar
import com.numero.storm.ui.components.SectionHeader
import com.numero.storm.ui.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAbout: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showSystemDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NumeroTopBar(
                title = stringResource(R.string.settings),
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Appearance Section
            SectionHeader(title = stringResource(R.string.appearance))

            SettingsCard {
                SettingsItem(
                    icon = Icons.Default.DarkMode,
                    title = stringResource(R.string.theme),
                    subtitle = when (settings.themeMode) {
                        ThemeMode.LIGHT -> stringResource(R.string.theme_light)
                        ThemeMode.DARK -> stringResource(R.string.theme_dark)
                        ThemeMode.SYSTEM -> stringResource(R.string.theme_system)
                    },
                    onClick = { showThemeDialog = true }
                )

                SettingsItem(
                    icon = Icons.Default.Language,
                    title = stringResource(R.string.language),
                    subtitle = settings.language.nativeName,
                    onClick = { showLanguageDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Numerology Section
            SectionHeader(title = stringResource(R.string.numerology))

            SettingsCard {
                SettingsItem(
                    icon = Icons.Default.Calculate,
                    title = stringResource(R.string.calculation_system),
                    subtitle = when (settings.numerologySystem) {
                        NumerologySystem.PYTHAGOREAN -> stringResource(R.string.system_pythagorean)
                        NumerologySystem.CHALDEAN -> stringResource(R.string.system_chaldean)
                    },
                    onClick = { showSystemDialog = true }
                )

                SettingsSwitchItem(
                    icon = Icons.Default.Star,
                    title = stringResource(R.string.show_master_numbers),
                    subtitle = stringResource(R.string.show_master_numbers_desc),
                    isChecked = settings.showMasterNumbers,
                    onCheckedChange = viewModel::setShowMasterNumbers
                )

                SettingsSwitchItem(
                    icon = Icons.Default.Warning,
                    title = stringResource(R.string.show_karmic_debt),
                    subtitle = stringResource(R.string.show_karmic_debt_desc),
                    isChecked = settings.showKarmicDebt,
                    onCheckedChange = viewModel::setShowKarmicDebt
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // About Section
            SectionHeader(title = stringResource(R.string.about_section))

            SettingsCard {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = stringResource(R.string.about_app),
                    subtitle = stringResource(R.string.version_info),
                    onClick = onNavigateToAbout
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Theme Dialog
    if (showThemeDialog) {
        SettingsDialog(
            title = stringResource(R.string.theme),
            onDismiss = { showThemeDialog = false }
        ) {
            ThemeMode.entries.forEach { mode ->
                RadioOptionItem(
                    text = when (mode) {
                        ThemeMode.LIGHT -> stringResource(R.string.theme_light)
                        ThemeMode.DARK -> stringResource(R.string.theme_dark)
                        ThemeMode.SYSTEM -> stringResource(R.string.theme_system)
                    },
                    isSelected = settings.themeMode == mode,
                    onClick = {
                        viewModel.setThemeMode(mode)
                        showThemeDialog = false
                    }
                )
            }
        }
    }

    // Language Dialog
    if (showLanguageDialog) {
        SettingsDialog(
            title = stringResource(R.string.language),
            onDismiss = { showLanguageDialog = false }
        ) {
            AppLanguage.entries.forEach { language ->
                RadioOptionItem(
                    text = "${language.nativeName} (${language.displayName})",
                    isSelected = settings.language == language,
                    onClick = {
                        viewModel.setLanguage(language)
                        showLanguageDialog = false
                    }
                )
            }
        }
    }

    // System Dialog
    if (showSystemDialog) {
        SettingsDialog(
            title = stringResource(R.string.calculation_system),
            onDismiss = { showSystemDialog = false }
        ) {
            NumerologySystem.entries.forEach { system ->
                RadioOptionItem(
                    text = when (system) {
                        NumerologySystem.PYTHAGOREAN -> stringResource(R.string.system_pythagorean)
                        NumerologySystem.CHALDEAN -> stringResource(R.string.system_chaldean)
                    },
                    description = when (system) {
                        NumerologySystem.PYTHAGOREAN -> stringResource(R.string.system_pythagorean_desc)
                        NumerologySystem.CHALDEAN -> stringResource(R.string.system_chaldean_desc)
                    },
                    isSelected = settings.numerologySystem == system,
                    onClick = {
                        viewModel.setNumerologySystem(system)
                        showSystemDialog = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsCard(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            content()
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingsDialog(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                content()
            }
        },
        confirmButton = {}
    )
}

@Composable
private fun RadioOptionItem(
    text: String,
    isSelected: Boolean,
    description: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
            description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
