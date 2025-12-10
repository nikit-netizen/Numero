package com.numero.storm.ui.screen.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numero.storm.R
import com.numero.storm.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val description: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val pages = listOf(
        OnboardingPage(
            icon = Icons.Default.Calculate,
            title = stringResource(R.string.onboarding_title_1),
            description = stringResource(R.string.onboarding_desc_1)
        ),
        OnboardingPage(
            icon = Icons.Default.Person,
            title = stringResource(R.string.onboarding_title_2),
            description = stringResource(R.string.onboarding_desc_2)
        ),
        OnboardingPage(
            icon = Icons.Default.Favorite,
            title = stringResource(R.string.onboarding_title_3),
            description = stringResource(R.string.onboarding_desc_3)
        ),
        OnboardingPage(
            icon = Icons.Default.TrendingUp,
            title = stringResource(R.string.onboarding_title_4),
            description = stringResource(R.string.onboarding_desc_4)
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        // Skip button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            AnimatedVisibility(
                visible = pagerState.currentPage < pages.size - 1,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TextButton(onClick = {
                    viewModel.completeOnboarding()
                    onComplete()
                }) {
                    Text(stringResource(R.string.skip))
                }
            }
        }

        // Pager content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            OnboardingPageContent(page = pages[page])
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Page indicators with animated width
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                val isSelected = pagerState.currentPage == index
                val indicatorWidth by animateFloatAsState(
                    targetValue = if (isSelected) 24f else 8f,
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    label = "indicator_width"
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(8.dp)
                        .width(indicatorWidth.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = pagerState.currentPage > 0,
                enter = fadeIn() + slideInHorizontally { -it },
                exit = fadeOut() + slideOutHorizontally { -it }
            ) {
                TextButton(onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }) {
                    Text(
                        text = stringResource(R.string.back),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (pagerState.currentPage == 0) {
                Spacer(modifier = Modifier.width(1.dp))
            }

            val isLastPage = pagerState.currentPage == pages.size - 1

            Button(
                onClick = {
                    if (isLastPage) {
                        viewModel.completeOnboarding()
                        onComplete()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(52.dp)
                    .then(
                        if (isLastPage) Modifier.fillMaxWidth(0.7f)
                        else Modifier
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLastPage) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (isLastPage) 8.dp else 4.dp
                )
            ) {
                if (isLastPage) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (isLastPage) {
                        stringResource(R.string.get_started)
                    } else {
                        stringResource(R.string.next)
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    // Floating animation for the icon
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_offset"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Outer glow ring
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationY = floatOffset
                }
                .size(140.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glow effect
            Box(
                modifier = Modifier
                    .scale(pulseScale)
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            )
            // Main icon container
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = CircleShape,
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 32.dp),
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2f
        )
    }
}
