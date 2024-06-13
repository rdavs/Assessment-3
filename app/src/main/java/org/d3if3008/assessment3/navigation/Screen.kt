package org.d3if3008.assessment3.navigation

import org.d3if3008.assessment3.ui.screen.KEY_ID_TRAVEL

sealed class Screen(val route: String) {
    data object Home : Screen("mainScreen")
    data object About : Screen("aboutScreen")
    data object FormTravel : Screen("travelScreen")
    data object bukti : Screen("buktiScreen")
    data object FormUbah : Screen("travelScreen/{$KEY_ID_TRAVEL}") {
        fun withId(id: Long) = "TravelScreen/$id"
    }
}