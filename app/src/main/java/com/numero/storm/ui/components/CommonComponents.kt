package com.numero.storm.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numero.storm.domain.calculator.CompatibilityLevel
import com.numero.storm.ui.theme.CompatibilityChallenging
import com.numero.storm.ui.theme.CompatibilityExcellent
import com.numero.storm.ui.theme.CompatibilityGood
import com.numero.storm.ui.theme.CompatibilityModerate
import com.numero.storm.ui.theme.KarmicDebtRed
import com.numero.storm.ui.theme.MasterNumberGold
import com.numero.storm.ui.theme.NumberEight
import com.numero.storm.ui.theme.NumberFive
import com.numero.storm.ui.theme.NumberFour
import com.numero.storm.ui.theme.NumberNine
import com.numero.storm.ui.theme.NumberOne
import com.numero.storm.ui.theme.NumberSeven
import com.numero.storm.ui.theme.NumberSix
import com.numero.storm.ui.theme.NumberThree
import com.numero.storm.ui.theme.NumberTwo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumeroTopBar(
    title: String,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (onNavigateBack != null) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun NumberDisplay(
    number: Int,
    modifier: Modifier = Modifier,
    size: NumberDisplaySize = NumberDisplaySize.MEDIUM,
    isMasterNumber: Boolean = false,
    isKarmicDebt: Boolean = false,
    showBadge: Boolean = true
) {
    val backgroundColor = when {
        isMasterNumber -> MasterNumberGold.copy(alpha = 0.15f)
        isKarmicDebt -> KarmicDebtRed.copy(alpha = 0.15f)
        else -> getNumberColor(number).copy(alpha = 0.15f)
    }

    val borderColor = when {
        isMasterNumber -> MasterNumberGold
        isKarmicDebt -> KarmicDebtRed
        else -> getNumberColor(number)
    }

    val textColor = when {
        isMasterNumber -> MasterNumberGold
        isKarmicDebt -> KarmicDebtRed
        else -> getNumberColor(number)
    }

    val boxSize = when (size) {
        NumberDisplaySize.SMALL -> 40.dp
        NumberDisplaySize.MEDIUM -> 56.dp
        NumberDisplaySize.LARGE -> 80.dp
        NumberDisplaySize.EXTRA_LARGE -> 100.dp
    }

    val fontSize = when (size) {
        NumberDisplaySize.SMALL -> 16.sp
        NumberDisplaySize.MEDIUM -> 24.sp
        NumberDisplaySize.LARGE -> 36.sp
        NumberDisplaySize.EXTRA_LARGE -> 48.sp
    }

    Box(
        modifier = modifier
            .size(boxSize)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(2.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        if (showBadge && (isMasterNumber || isKarmicDebt)) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(if (size == NumberDisplaySize.SMALL) 12.dp else 16.dp)
                    .clip(CircleShape)
                    .background(if (isMasterNumber) MasterNumberGold else KarmicDebtRed),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = if (isMasterNumber) "Master Number" else "Karmic Debt",
                    tint = Color.White,
                    modifier = Modifier.size(if (size == NumberDisplaySize.SMALL) 8.dp else 10.dp)
                )
            }
        }
    }
}

enum class NumberDisplaySize {
    SMALL, MEDIUM, LARGE, EXTRA_LARGE
}

@Composable
fun NumberCard(
    title: String,
    number: Int,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    isMasterNumber: Boolean = false,
    isKarmicDebt: Boolean = false,
    karmicDebtNumber: Int? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumberDisplay(
                number = number,
                size = NumberDisplaySize.MEDIUM,
                isMasterNumber = isMasterNumber,
                isKarmicDebt = isKarmicDebt
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (isMasterNumber) {
                    Text(
                        text = "Master Number",
                        style = MaterialTheme.typography.labelSmall,
                        color = MasterNumberGold,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (karmicDebtNumber != null) {
                    Text(
                        text = "Karmic Debt $karmicDebtNumber",
                        style = MaterialTheme.typography.labelSmall,
                        color = KarmicDebtRed,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun CompatibilityScoreDisplay(
    score: Int,
    level: CompatibilityLevel,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true
) {
    val color = when (level) {
        CompatibilityLevel.EXCELLENT -> CompatibilityExcellent
        CompatibilityLevel.GOOD -> CompatibilityGood
        CompatibilityLevel.MODERATE -> CompatibilityModerate
        CompatibilityLevel.CHALLENGING -> CompatibilityChallenging
    }

    val animatedProgress by animateFloatAsState(
        targetValue = score / 100f,
        label = "score_progress"
    )

    val animatedColor by animateColorAsState(
        targetValue = color,
        label = "score_color"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.size(120.dp),
                color = animatedColor,
                strokeWidth = 8.dp,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$score%",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = animatedColor
                )

                if (showLabel) {
                    Text(
                        text = level.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun AspectScoreBar(
    label: String,
    score: Int,
    number1: Int,
    number2: Int,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = score / 100f,
        label = "aspect_progress"
    )

    val color = when {
        score >= 85 -> CompatibilityExcellent
        score >= 70 -> CompatibilityGood
        score >= 50 -> CompatibilityModerate
        else -> CompatibilityChallenging
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                NumberDisplay(number = number1, size = NumberDisplaySize.SMALL, showBadge = false)
                Text(
                    text = " + ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                NumberDisplay(number = number2, size = NumberDisplaySize.SMALL, showBadge = false)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = color,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "$score%",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )

        if (message != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )

        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material3.TextButton(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        action?.invoke()
    }
}

fun getNumberColor(number: Int): Color {
    return when (number % 9) {
        1 -> NumberOne
        2 -> NumberTwo
        3 -> NumberThree
        4 -> NumberFour
        5 -> NumberFive
        6 -> NumberSix
        7 -> NumberSeven
        8 -> NumberEight
        0 -> NumberNine
        else -> NumberOne
    }
}
