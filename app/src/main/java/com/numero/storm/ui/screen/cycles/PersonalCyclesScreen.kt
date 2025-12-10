package com.numero.storm.ui.screen.cycles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numero.storm.R
import com.numero.storm.ui.components.LoadingIndicator
import com.numero.storm.ui.components.NumberDisplay
import com.numero.storm.ui.components.NumberDisplaySize
import com.numero.storm.ui.components.NumeroTopBar
import com.numero.storm.ui.components.SectionHeader
import com.numero.storm.ui.viewmodel.PersonalCycleData
import com.numero.storm.ui.viewmodel.PersonalCyclesViewModel

@Composable
fun PersonalCyclesScreen(
    profileId: Long,
    onNavigateBack: () -> Unit,
    viewModel: PersonalCyclesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(profileId) {
        viewModel.loadCycles(profileId)
    }

    Scaffold(
        topBar = {
            NumeroTopBar(
                title = stringResource(R.string.personal_cycles),
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Current Cycles Overview
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.current_cycles),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                uiState.personalDay?.let {
                                    CycleItem(
                                        label = stringResource(R.string.personal_day),
                                        number = it.number
                                    )
                                }
                                uiState.personalMonth?.let {
                                    CycleItem(
                                        label = stringResource(R.string.personal_month),
                                        number = it.number
                                    )
                                }
                                uiState.personalYear?.let {
                                    CycleItem(
                                        label = stringResource(R.string.personal_year),
                                        number = it.number
                                    )
                                }
                            }
                        }
                    }
                }

                // Personal Year Details
                uiState.personalYear?.let { py ->
                    item {
                        SectionHeader(title = stringResource(R.string.personal_year_insight))
                        CycleDetailCard(
                            title = stringResource(R.string.personal_year_title, py.number),
                            interpretation = py.interpretation
                        )
                    }
                }

                // Personal Month Details
                uiState.personalMonth?.let { pm ->
                    item {
                        SectionHeader(title = stringResource(R.string.personal_month_insight))
                        CycleDetailCard(
                            title = stringResource(R.string.personal_month_title, pm.number),
                            interpretation = pm.interpretation
                        )
                    }
                }

                // Personal Day Details
                uiState.personalDay?.let { pd ->
                    item {
                        SectionHeader(title = stringResource(R.string.personal_day_insight))
                        CycleDetailCard(
                            title = stringResource(R.string.personal_day_title, pd.number),
                            interpretation = pd.interpretation
                        )
                    }
                }

                // Universal Cycles
                item {
                    SectionHeader(title = stringResource(R.string.universal_cycles))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                uiState.universalDay?.let {
                                    CycleItem(
                                        label = stringResource(R.string.universal_day),
                                        number = it.number
                                    )
                                }
                                uiState.universalMonth?.let {
                                    CycleItem(
                                        label = stringResource(R.string.universal_month),
                                        number = it.number
                                    )
                                }
                                uiState.universalYear?.let {
                                    CycleItem(
                                        label = stringResource(R.string.universal_year),
                                        number = it.number
                                    )
                                }
                            }
                        }
                    }
                }

                // 9-Year Forecast
                if (uiState.yearlyForecast.isNotEmpty()) {
                    item {
                        SectionHeader(title = stringResource(R.string.nine_year_forecast))
                    }

                    items(uiState.yearlyForecast) { forecast ->
                        ForecastCard(data = forecast)
                    }
                }
            }
        }
    }
}

@Composable
private fun CycleItem(
    label: String,
    number: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        NumberDisplay(
            number = number,
            size = NumberDisplaySize.MEDIUM
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CycleDetailCard(
    title: String,
    interpretation: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = interpretation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ForecastCard(data: PersonalCycleData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumberDisplay(
                number = data.number,
                size = NumberDisplaySize.SMALL
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = data.interpretation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
