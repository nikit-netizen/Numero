package com.numero.storm.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numero.storm.R
import com.numero.storm.data.model.Profile
import com.numero.storm.ui.components.LoadingIndicator
import com.numero.storm.ui.components.NumberCard
import com.numero.storm.ui.components.NumberDisplay
import com.numero.storm.ui.components.NumberDisplaySize
import com.numero.storm.ui.components.NumeroTopBar
import com.numero.storm.ui.components.SectionHeader
import com.numero.storm.ui.viewmodel.HomeUiState
import com.numero.storm.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProfile: (Long) -> Unit,
    onNavigateToProfiles: () -> Unit,
    onNavigateToCompatibility: () -> Unit,
    onNavigateToDaily: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToCreateProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            NumeroTopBar(
                title = stringResource(R.string.app_name),
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateProfile,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_profile))
            }
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
            HomeContent(
                uiState = uiState,
                modifier = Modifier.padding(padding),
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToProfiles = onNavigateToProfiles,
                onNavigateToCompatibility = onNavigateToCompatibility,
                onNavigateToDaily = onNavigateToDaily,
                onNavigateToCreateProfile = onNavigateToCreateProfile
            )
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onNavigateToProfile: (Long) -> Unit,
    onNavigateToProfiles: () -> Unit,
    onNavigateToCompatibility: () -> Unit,
    onNavigateToDaily: () -> Unit,
    onNavigateToCreateProfile: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 88.dp)
    ) {
        // Today's Numbers Section
        item {
            TodaySection(
                universalDay = uiState.universalDay,
                personalDay = uiState.personalDay,
                personalMonth = uiState.personalMonth,
                personalYear = uiState.personalYear,
                dailyInsight = uiState.dailyInsight,
                onNavigateToDaily = onNavigateToDaily
            )
        }

        // Primary Profile Section
        item {
            if (uiState.primaryProfile != null && uiState.primaryAnalysis != null) {
                SectionHeader(title = stringResource(R.string.your_numbers))
                PrimaryProfileCard(
                    profile = uiState.primaryProfile,
                    lifePathNumber = uiState.primaryAnalysis.lifePathNumber,
                    expressionNumber = uiState.primaryAnalysis.expressionNumber,
                    soulUrgeNumber = uiState.primaryAnalysis.soulUrgeNumber,
                    isMasterLifePath = uiState.primaryAnalysis.lifePathMasterNumber,
                    onClick = { onNavigateToProfile(uiState.primaryProfile.id) }
                )
            } else if (uiState.profiles.isEmpty()) {
                EmptyProfilesCard(onCreateProfile = onNavigateToCreateProfile)
            }
        }

        // Quick Actions
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(title = stringResource(R.string.quick_actions))
            QuickActionsRow(
                onCompatibility = onNavigateToCompatibility,
                onDaily = onNavigateToDaily,
                onProfiles = onNavigateToProfiles
            )
        }

        // All Profiles
        if (uiState.profiles.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = stringResource(R.string.profiles),
                    action = {
                        Text(
                            text = stringResource(R.string.view_all),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable(onClick = onNavigateToProfiles)
                        )
                    }
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.profiles.take(5)) { profile ->
                        ProfileChip(
                            profile = profile,
                            isPrimary = profile.isPrimary,
                            onClick = { onNavigateToProfile(profile.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TodaySection(
    universalDay: Int,
    personalDay: Int?,
    personalMonth: Int?,
    personalYear: Int?,
    dailyInsight: String,
    onNavigateToDaily: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onNavigateToDaily),
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
                text = stringResource(R.string.today),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    NumberDisplay(
                        number = universalDay,
                        size = NumberDisplaySize.MEDIUM
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.universal_day),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                if (personalDay != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        NumberDisplay(
                            number = personalDay,
                            size = NumberDisplaySize.MEDIUM
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.personal_day),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }

                if (personalYear != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        NumberDisplay(
                            number = personalYear,
                            size = NumberDisplaySize.MEDIUM
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.personal_year),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            if (dailyInsight.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = dailyInsight.take(150) + if (dailyInsight.length > 150) "..." else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun PrimaryProfileCard(
    profile: Profile,
    lifePathNumber: Int,
    expressionNumber: Int,
    soulUrgeNumber: Int,
    isMasterLifePath: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.firstName.first().toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = profile.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(R.string.primary_profile),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NumberColumn(
                    label = stringResource(R.string.life_path),
                    number = lifePathNumber,
                    isMaster = isMasterLifePath
                )
                NumberColumn(
                    label = stringResource(R.string.expression),
                    number = expressionNumber
                )
                NumberColumn(
                    label = stringResource(R.string.soul_urge),
                    number = soulUrgeNumber
                )
            }
        }
    }
}

@Composable
private fun NumberColumn(
    label: String,
    number: Int,
    isMaster: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        NumberDisplay(
            number = number,
            size = NumberDisplaySize.MEDIUM,
            isMasterNumber = isMaster
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
private fun QuickActionsRow(
    onCompatibility: () -> Unit,
    onDaily: () -> Unit,
    onProfiles: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionCard(
            icon = Icons.Default.Favorite,
            label = stringResource(R.string.compatibility),
            modifier = Modifier.weight(1f),
            onClick = onCompatibility
        )
        QuickActionCard(
            icon = Icons.Default.Today,
            label = stringResource(R.string.daily),
            modifier = Modifier.weight(1f),
            onClick = onDaily
        )
        QuickActionCard(
            icon = Icons.Default.Person,
            label = stringResource(R.string.profiles),
            modifier = Modifier.weight(1f),
            onClick = onProfiles
        )
    }
}

@Composable
private fun QuickActionCard(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProfileChip(
    profile: Profile,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPrimary) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (isPrimary) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.firstName.first().toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isPrimary) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSecondary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = profile.firstName,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun EmptyProfilesCard(onCreateProfile: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onCreateProfile),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.no_profiles_yet),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.create_first_profile),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
