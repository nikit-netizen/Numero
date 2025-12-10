package com.numero.storm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.numero.storm.ui.screen.about.AboutScreen
import com.numero.storm.ui.screen.analysis.AnalysisDetailScreen
import com.numero.storm.ui.screen.analysis.AnalysisScreen
import com.numero.storm.ui.screen.compatibility.CompatibilityResultScreen
import com.numero.storm.ui.screen.compatibility.CompatibilityScreen
import com.numero.storm.ui.screen.cycles.PersonalCyclesScreen
import com.numero.storm.ui.screen.daily.DailyNumbersScreen
import com.numero.storm.ui.screen.home.HomeScreen
import com.numero.storm.ui.screen.onboarding.OnboardingScreen
import com.numero.storm.ui.screen.profile.CreateProfileScreen
import com.numero.storm.ui.screen.profile.EditProfileScreen
import com.numero.storm.ui.screen.profile.ProfileListScreen
import com.numero.storm.ui.screen.settings.SettingsScreen

@Composable
fun NumeroNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavDestination.Onboarding.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavDestination.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(NavDestination.Home.route) {
                        popUpTo(NavDestination.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavDestination.Home.route) {
            HomeScreen(
                onNavigateToProfile = { profileId ->
                    navController.navigate(NavDestination.Analysis.createRoute(profileId))
                },
                onNavigateToProfiles = {
                    navController.navigate(NavDestination.ProfileList.route)
                },
                onNavigateToCompatibility = {
                    navController.navigate(NavDestination.Compatibility.route)
                },
                onNavigateToDaily = {
                    navController.navigate(NavDestination.DailyNumbers.route)
                },
                onNavigateToSettings = {
                    navController.navigate(NavDestination.Settings.route)
                },
                onNavigateToCreateProfile = {
                    navController.navigate(NavDestination.CreateProfile.route)
                }
            )
        }

        composable(NavDestination.ProfileList.route) {
            ProfileListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCreate = {
                    navController.navigate(NavDestination.CreateProfile.route)
                },
                onNavigateToEdit = { profileId ->
                    navController.navigate(NavDestination.EditProfile.createRoute(profileId))
                },
                onNavigateToAnalysis = { profileId ->
                    navController.navigate(NavDestination.Analysis.createRoute(profileId))
                }
            )
        }

        composable(NavDestination.CreateProfile.route) {
            CreateProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onProfileCreated = { profileId ->
                    navController.navigate(NavDestination.Analysis.createRoute(profileId)) {
                        popUpTo(NavDestination.Home.route)
                    }
                }
            )
        }

        composable(
            route = NavDestination.EditProfile.route,
            arguments = NavDestination.EditProfile.arguments
        ) { backStackEntry ->
            val profileId = backStackEntry.arguments?.getLong("profileId") ?: return@composable
            EditProfileScreen(
                profileId = profileId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavDestination.Analysis.route,
            arguments = NavDestination.Analysis.arguments
        ) { backStackEntry ->
            val profileId = backStackEntry.arguments?.getLong("profileId") ?: return@composable
            AnalysisScreen(
                profileId = profileId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { numberType ->
                    navController.navigate(NavDestination.AnalysisDetail.createRoute(profileId, numberType))
                },
                onNavigateToCycles = {
                    navController.navigate(NavDestination.PersonalCycles.createRoute(profileId))
                },
                onNavigateToEdit = {
                    navController.navigate(NavDestination.EditProfile.createRoute(profileId))
                }
            )
        }

        composable(
            route = NavDestination.AnalysisDetail.route,
            arguments = NavDestination.AnalysisDetail.arguments
        ) { backStackEntry ->
            val profileId = backStackEntry.arguments?.getLong("profileId") ?: return@composable
            val numberType = backStackEntry.arguments?.getString("numberType") ?: return@composable
            AnalysisDetailScreen(
                profileId = profileId,
                numberType = numberType,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(NavDestination.Compatibility.route) {
            CompatibilityScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResult = { profile1Id, profile2Id ->
                    navController.navigate(
                        NavDestination.CompatibilityResult.createRoute(profile1Id, profile2Id)
                    )
                },
                onNavigateToCreateProfile = {
                    navController.navigate(NavDestination.CreateProfile.route)
                }
            )
        }

        composable(
            route = NavDestination.CompatibilityResult.route,
            arguments = NavDestination.CompatibilityResult.arguments
        ) { backStackEntry ->
            val profile1Id = backStackEntry.arguments?.getLong("profile1Id") ?: return@composable
            val profile2Id = backStackEntry.arguments?.getLong("profile2Id") ?: return@composable
            CompatibilityResultScreen(
                profile1Id = profile1Id,
                profile2Id = profile2Id,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NavDestination.PersonalCycles.route,
            arguments = NavDestination.PersonalCycles.arguments
        ) { backStackEntry ->
            val profileId = backStackEntry.arguments?.getLong("profileId") ?: return@composable
            PersonalCyclesScreen(
                profileId = profileId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(NavDestination.DailyNumbers.route) {
            DailyNumbersScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(NavDestination.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAbout = {
                    navController.navigate(NavDestination.About.route)
                }
            )
        }

        composable(NavDestination.About.route) {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
