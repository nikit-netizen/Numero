package com.numero.storm.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavDestination(val route: String) {
    data object Onboarding : NavDestination("onboarding")
    data object Home : NavDestination("home")
    data object ProfileList : NavDestination("profiles")
    data object CreateProfile : NavDestination("profile/create")

    data object EditProfile : NavDestination("profile/edit/{profileId}") {
        fun createRoute(profileId: Long) = "profile/edit/$profileId"
        val arguments = listOf(
            navArgument("profileId") { type = NavType.LongType }
        )
    }

    data object Analysis : NavDestination("analysis/{profileId}") {
        fun createRoute(profileId: Long) = "analysis/$profileId"
        val arguments = listOf(
            navArgument("profileId") { type = NavType.LongType }
        )
    }

    data object AnalysisDetail : NavDestination("analysis/{profileId}/detail/{numberType}") {
        fun createRoute(profileId: Long, numberType: String) = "analysis/$profileId/detail/$numberType"
        val arguments = listOf(
            navArgument("profileId") { type = NavType.LongType },
            navArgument("numberType") { type = NavType.StringType }
        )
    }

    data object Compatibility : NavDestination("compatibility")

    data object CompatibilityResult : NavDestination("compatibility/{profile1Id}/{profile2Id}") {
        fun createRoute(profile1Id: Long, profile2Id: Long) = "compatibility/$profile1Id/$profile2Id"
        val arguments = listOf(
            navArgument("profile1Id") { type = NavType.LongType },
            navArgument("profile2Id") { type = NavType.LongType }
        )
    }

    data object PersonalCycles : NavDestination("cycles/{profileId}") {
        fun createRoute(profileId: Long) = "cycles/$profileId"
        val arguments = listOf(
            navArgument("profileId") { type = NavType.LongType }
        )
    }

    data object DailyNumbers : NavDestination("daily")

    data object Settings : NavDestination("settings")

    data object About : NavDestination("about")
}

enum class NumberType(val displayName: String) {
    LIFE_PATH("Life Path"),
    EXPRESSION("Expression"),
    SOUL_URGE("Soul Urge"),
    PERSONALITY("Personality"),
    BIRTHDAY("Birthday"),
    MATURITY("Maturity"),
    BALANCE("Balance"),
    HIDDEN_PASSION("Hidden Passion"),
    KARMIC_LESSONS("Karmic Lessons"),
    PINNACLES("Pinnacles"),
    CHALLENGES("Challenges"),
    LIFE_PERIODS("Life Periods")
}
