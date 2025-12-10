package com.numero.storm.ui.screen.analysis

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.numero.storm.data.model.ChallengeEntity
import com.numero.storm.data.model.LifePeriodEntity
import com.numero.storm.data.model.NumerologyAnalysis
import com.numero.storm.data.model.PinnacleEntity
import com.numero.storm.data.model.getKarmicLessonsList
import com.numero.storm.ui.components.LoadingIndicator
import com.numero.storm.ui.components.NumberCard
import com.numero.storm.ui.components.NumberDisplay
import com.numero.storm.ui.components.NumberDisplaySize
import com.numero.storm.ui.components.NumeroTopBar
import com.numero.storm.ui.components.SectionHeader
import com.numero.storm.ui.navigation.NumberType
import com.numero.storm.ui.viewmodel.AnalysisUiState
import com.numero.storm.ui.viewmodel.AnalysisViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    profileId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCycles: () -> Unit,
    onNavigateToEdit: () -> Unit,
    viewModel: AnalysisViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(profileId) {
        viewModel.loadAnalysis(profileId)
    }

    Scaffold(
        topBar = {
            NumeroTopBar(
                title = uiState.profile?.displayName ?: stringResource(R.string.analysis),
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = onNavigateToEdit) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
                    }
                    IconButton(onClick = { viewModel.recalculateAnalysis() }) {
                        Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.recalculate))
                    }
                }
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
                LoadingIndicator(message = stringResource(R.string.calculating))
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            AnalysisContent(
                uiState = uiState,
                modifier = Modifier.padding(padding),
                onNavigateToDetail = onNavigateToDetail,
                onNavigateToCycles = onNavigateToCycles
            )
        }
    }
}

@Composable
private fun AnalysisContent(
    uiState: AnalysisUiState,
    modifier: Modifier = Modifier,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCycles: () -> Unit
) {
    val analysis = uiState.analysis ?: return

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Core Numbers Section
        item {
            SectionHeader(title = stringResource(R.string.core_numbers))
        }

        item {
            CoreNumbersGrid(
                analysis = analysis,
                onNumberClick = onNavigateToDetail
            )
        }

        // Special Numbers Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(title = stringResource(R.string.special_numbers))
        }

        item {
            SpecialNumbersSection(
                analysis = analysis,
                onNumberClick = onNavigateToDetail
            )
        }

        // Personal Cycles Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = stringResource(R.string.life_cycles),
                action = {
                    TextButton(onClick = onNavigateToCycles) {
                        Icon(Icons.Default.Timeline, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                        Text(stringResource(R.string.view_cycles))
                    }
                }
            )
        }

        // Pinnacles
        item {
            PinnaclesSection(
                pinnacles = uiState.pinnacles,
                currentIndex = uiState.currentPinnacleIndex,
                currentAge = uiState.currentAge
            )
        }

        // Challenges
        item {
            Spacer(modifier = Modifier.height(16.dp))
            ChallengesSection(
                challenges = uiState.challenges,
                currentIndex = uiState.currentChallengeIndex,
                currentAge = uiState.currentAge
            )
        }

        // Life Periods
        item {
            Spacer(modifier = Modifier.height(16.dp))
            LifePeriodsSection(
                lifePeriods = uiState.lifePeriods,
                currentIndex = uiState.currentLifePeriodIndex,
                currentAge = uiState.currentAge
            )
        }

        // Karmic Lessons
        val karmicLessons = analysis.getKarmicLessonsList()
        if (karmicLessons.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(title = stringResource(R.string.karmic_lessons))
                KarmicLessonsSection(lessons = karmicLessons.toList())
            }
        }
    }
}

@Composable
private fun CoreNumbersGrid(
    analysis: NumerologyAnalysis,
    onNumberClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NumberCard(
            title = stringResource(R.string.life_path),
            number = analysis.lifePathNumber,
            subtitle = stringResource(R.string.life_path_desc),
            isMasterNumber = analysis.lifePathMasterNumber,
            karmicDebtNumber = analysis.lifePathKarmicDebt,
            onClick = { onNumberClick(NumberType.LIFE_PATH.name) }
        )

        NumberCard(
            title = stringResource(R.string.expression),
            number = analysis.expressionNumber,
            subtitle = stringResource(R.string.expression_desc),
            isMasterNumber = analysis.expressionMasterNumber,
            karmicDebtNumber = analysis.expressionKarmicDebt,
            onClick = { onNumberClick(NumberType.EXPRESSION.name) }
        )

        NumberCard(
            title = stringResource(R.string.soul_urge),
            number = analysis.soulUrgeNumber,
            subtitle = stringResource(R.string.soul_urge_desc),
            isMasterNumber = analysis.soulUrgeMasterNumber,
            karmicDebtNumber = analysis.soulUrgeKarmicDebt,
            onClick = { onNumberClick(NumberType.SOUL_URGE.name) }
        )

        NumberCard(
            title = stringResource(R.string.personality),
            number = analysis.personalityNumber,
            subtitle = stringResource(R.string.personality_desc),
            isMasterNumber = analysis.personalityMasterNumber,
            karmicDebtNumber = analysis.personalityKarmicDebt,
            onClick = { onNumberClick(NumberType.PERSONALITY.name) }
        )

        NumberCard(
            title = stringResource(R.string.birthday),
            number = analysis.birthdayNumber,
            subtitle = stringResource(R.string.birthday_desc),
            isMasterNumber = analysis.birthdayMasterNumber,
            onClick = { onNumberClick(NumberType.BIRTHDAY.name) }
        )
    }
}

@Composable
private fun SpecialNumbersSection(
    analysis: NumerologyAnalysis,
    onNumberClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NumberCard(
            title = stringResource(R.string.maturity),
            number = analysis.maturityNumber,
            subtitle = stringResource(R.string.maturity_desc),
            isMasterNumber = analysis.maturityMasterNumber,
            onClick = { onNumberClick(NumberType.MATURITY.name) }
        )

        NumberCard(
            title = stringResource(R.string.balance),
            number = analysis.balanceNumber,
            subtitle = stringResource(R.string.balance_desc),
            onClick = { onNumberClick(NumberType.BALANCE.name) }
        )

        analysis.hiddenPassionNumber?.let { hp ->
            NumberCard(
                title = stringResource(R.string.hidden_passion),
                number = hp,
                subtitle = stringResource(R.string.hidden_passion_desc),
                onClick = { onNumberClick(NumberType.HIDDEN_PASSION.name) }
            )
        }
    }
}

@Composable
private fun PinnaclesSection(
    pinnacles: List<PinnacleEntity>,
    currentIndex: Int,
    currentAge: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.pinnacles),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pinnacles) { pinnacle ->
                    val isCurrent = pinnacle.periodIndex - 1 == currentIndex
                    PeriodCard(
                        number = pinnacle.number,
                        label = "Pinnacle ${pinnacle.periodIndex}",
                        ageRange = if (pinnacle.endAge != null) {
                            "${pinnacle.startAge}-${pinnacle.endAge}"
                        } else {
                            "${pinnacle.startAge}+"
                        },
                        isCurrent = isCurrent,
                        isMaster = pinnacle.isMasterNumber
                    )
                }
            }
        }
    }
}

@Composable
private fun ChallengesSection(
    challenges: List<ChallengeEntity>,
    currentIndex: Int,
    currentAge: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.challenges),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(challenges) { challenge ->
                    val isCurrent = challenge.periodIndex - 1 == currentIndex
                    PeriodCard(
                        number = challenge.number,
                        label = "Challenge ${challenge.periodIndex}",
                        ageRange = if (challenge.endAge != null) {
                            "${challenge.startAge}-${challenge.endAge}"
                        } else {
                            "${challenge.startAge}+"
                        },
                        isCurrent = isCurrent,
                        isMaster = false
                    )
                }
            }
        }
    }
}

@Composable
private fun LifePeriodsSection(
    lifePeriods: List<LifePeriodEntity>,
    currentIndex: Int,
    currentAge: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.life_periods),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lifePeriods) { period ->
                    val isCurrent = period.periodIndex - 1 == currentIndex
                    PeriodCard(
                        number = period.number,
                        label = period.source,
                        ageRange = if (period.endAge != null) {
                            "${period.startAge}-${period.endAge}"
                        } else {
                            "${period.startAge}+"
                        },
                        isCurrent = isCurrent,
                        isMaster = period.isMasterNumber
                    )
                }
            }
        }
    }
}

@Composable
private fun PeriodCard(
    number: Int,
    label: String,
    ageRange: String,
    isCurrent: Boolean,
    isMaster: Boolean
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NumberDisplay(
                number = number,
                size = NumberDisplaySize.SMALL,
                isMasterNumber = isMaster
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
            )

            Text(
                text = ageRange,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (isCurrent) {
                Text(
                    text = stringResource(R.string.current),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun KarmicLessonsSection(lessons: List<Int>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.missing_numbers),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                lessons.forEach { lesson ->
                    NumberDisplay(
                        number = lesson,
                        size = NumberDisplaySize.SMALL,
                        isKarmicDebt = true,
                        showBadge = false
                    )
                }
            }
        }
    }
}
