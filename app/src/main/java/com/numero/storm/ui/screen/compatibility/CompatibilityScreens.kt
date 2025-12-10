package com.numero.storm.ui.screen.compatibility

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numero.storm.R
import com.numero.storm.data.model.Profile
import com.numero.storm.domain.calculator.CompatibilityLevel
import com.numero.storm.ui.components.AspectScoreBar
import com.numero.storm.ui.components.CompatibilityScoreDisplay
import com.numero.storm.ui.components.LoadingIndicator
import com.numero.storm.ui.components.NumberDisplay
import com.numero.storm.ui.components.NumberDisplaySize
import com.numero.storm.ui.components.NumeroTopBar
import com.numero.storm.ui.components.SectionHeader
import com.numero.storm.ui.theme.CompatibilityChallenging
import com.numero.storm.ui.theme.CompatibilityExcellent
import com.numero.storm.ui.theme.CompatibilityGood
import com.numero.storm.ui.theme.CompatibilityModerate
import com.numero.storm.ui.viewmodel.CompatibilityResultViewModel
import com.numero.storm.ui.viewmodel.CompatibilitySelectViewModel
import java.time.format.DateTimeFormatter

@Composable
fun CompatibilityScreen(
    onNavigateBack: () -> Unit,
    onNavigateToResult: (Long, Long) -> Unit,
    onNavigateToCreateProfile: () -> Unit,
    viewModel: CompatibilitySelectViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            NumeroTopBar(
                title = stringResource(R.string.compatibility),
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
        } else if (uiState.profiles.size < 2) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.need_two_profiles),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.create_profiles_for_compatibility),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = onNavigateToCreateProfile) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.add_profile))
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Selection Area
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProfileSelector(
                                profile = uiState.selectedProfile1,
                                label = stringResource(R.string.person_1),
                                isSelected = uiState.selectedProfile1 != null
                            )

                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )

                            ProfileSelector(
                                profile = uiState.selectedProfile2,
                                label = stringResource(R.string.person_2),
                                isSelected = uiState.selectedProfile2 != null
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                val p1 = uiState.selectedProfile1
                                val p2 = uiState.selectedProfile2
                                if (p1 != null && p2 != null) {
                                    onNavigateToResult(p1.id, p2.id)
                                }
                            },
                            enabled = viewModel.canCompare(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(stringResource(R.string.check_compatibility))
                        }
                    }
                }

                SectionHeader(title = stringResource(R.string.select_profiles))

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.profiles) { profile ->
                        val isSelected1 = uiState.selectedProfile1?.id == profile.id
                        val isSelected2 = uiState.selectedProfile2?.id == profile.id

                        ProfileSelectionItem(
                            profile = profile,
                            isSelected1 = isSelected1,
                            isSelected2 = isSelected2,
                            onSelect = {
                                if (uiState.selectedProfile1 == null || isSelected1) {
                                    viewModel.selectProfile1(profile)
                                } else {
                                    viewModel.selectProfile2(profile)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileSelector(
    profile: Profile?,
    label: String,
    isSelected: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            if (profile != null) {
                Text(
                    text = profile.firstName.first().toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = profile?.firstName ?: label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun ProfileSelectionItem(
    profile: Profile,
    isSelected1: Boolean,
    isSelected2: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected1 -> MaterialTheme.colorScheme.primaryContainer
                isSelected2 -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.firstName.first().toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = profile.birthDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isSelected1 || isSelected2) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (isSelected1) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun CompatibilityResultScreen(
    profile1Id: Long,
    profile2Id: Long,
    onNavigateBack: () -> Unit,
    viewModel: CompatibilityResultViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(profile1Id, profile2Id) {
        viewModel.loadCompatibility(profile1Id, profile2Id)
    }

    Scaffold(
        topBar = {
            NumeroTopBar(
                title = stringResource(R.string.compatibility_result),
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { viewModel.recalculate() }) {
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
                LoadingIndicator(message = stringResource(R.string.calculating_compatibility))
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
            val compatibility = uiState.compatibility ?: return@Scaffold

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Overall Score Card
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
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ProfileAvatar(
                                    profile = uiState.profile1,
                                    size = 56
                                )

                                CompatibilityScoreDisplay(
                                    score = compatibility.overallScore,
                                    level = compatibility.level
                                )

                                ProfileAvatar(
                                    profile = uiState.profile2,
                                    size = 56
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "${uiState.profile1?.firstName ?: ""} & ${uiState.profile2?.firstName ?: ""}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = getCompatibilityDescription(compatibility.level),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(R.string.relationship_number),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                NumberDisplay(
                                    number = compatibility.relationshipNumber,
                                    size = NumberDisplaySize.SMALL
                                )
                            }
                        }
                    }
                }

                // Aspect Scores
                item {
                    SectionHeader(title = stringResource(R.string.aspect_scores))
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            AspectScoreBar(
                                label = stringResource(R.string.life_path),
                                score = compatibility.lifePathScore,
                                number1 = compatibility.lifePathNumber1,
                                number2 = compatibility.lifePathNumber2
                            )

                            AspectScoreBar(
                                label = stringResource(R.string.expression),
                                score = compatibility.expressionScore,
                                number1 = compatibility.expressionNumber1,
                                number2 = compatibility.expressionNumber2
                            )

                            AspectScoreBar(
                                label = stringResource(R.string.soul_urge),
                                score = compatibility.soulUrgeScore,
                                number1 = compatibility.soulUrgeNumber1,
                                number2 = compatibility.soulUrgeNumber2
                            )

                            AspectScoreBar(
                                label = stringResource(R.string.personality),
                                score = compatibility.personalityScore,
                                number1 = compatibility.personalityNumber1,
                                number2 = compatibility.personalityNumber2
                            )

                            AspectScoreBar(
                                label = stringResource(R.string.birthday),
                                score = compatibility.birthdayScore,
                                number1 = compatibility.birthdayNumber1,
                                number2 = compatibility.birthdayNumber2
                            )
                        }
                    }
                }

                // Shared Numbers
                if (uiState.sharedNumbers.isNotEmpty()) {
                    item {
                        SectionHeader(title = stringResource(R.string.shared_numbers))
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
                                    text = stringResource(R.string.shared_numbers_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    uiState.sharedNumbers.forEach { num ->
                                        NumberDisplay(
                                            number = num,
                                            size = NumberDisplaySize.SMALL
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Complementary Aspects
                if (uiState.complementaryAspects.isNotEmpty()) {
                    item {
                        SectionHeader(title = stringResource(R.string.strengths))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = CompatibilityExcellent.copy(alpha = 0.1f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                uiState.complementaryAspects.forEach { aspect ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = CompatibilityExcellent,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = aspect,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Challenges
                if (uiState.challenges.isNotEmpty()) {
                    item {
                        SectionHeader(title = stringResource(R.string.challenges))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = CompatibilityChallenging.copy(alpha = 0.1f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                uiState.challenges.forEach { challenge ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = CompatibilityChallenging,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = challenge,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileAvatar(
    profile: Profile?,
    size: Int
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = profile?.firstName?.first()?.toString() ?: "?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun getCompatibilityDescription(level: CompatibilityLevel): String {
    return when (level) {
        CompatibilityLevel.EXCELLENT -> stringResource(R.string.compatibility_excellent)
        CompatibilityLevel.GOOD -> stringResource(R.string.compatibility_good)
        CompatibilityLevel.MODERATE -> stringResource(R.string.compatibility_moderate)
        CompatibilityLevel.CHALLENGING -> stringResource(R.string.compatibility_challenging)
    }
}
