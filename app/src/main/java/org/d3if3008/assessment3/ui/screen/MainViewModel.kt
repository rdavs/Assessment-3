package org.d3if3008.assessment3.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3008.assessment3.database.TravelDao
import org.d3if3008.assessment3.model.Travel

class MainViewModel(dao: TravelDao) : ViewModel() {
    val data: StateFlow<List<Travel>> = dao.getTravel().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}