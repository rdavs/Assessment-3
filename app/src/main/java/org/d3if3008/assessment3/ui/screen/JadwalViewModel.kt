package org.d3if3008.assessment3.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.d3if3008.assessment3.model.Bukti
import org.d3if3008.assessment3.network.TravelApi

class JadwalViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Bukti>())
        private set

    var status = MutableStateFlow(TravelApi.ApiStatus.LOADING)
        private set

    init {
        retrieveData()
    }
    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = TravelApi.ApiStatus.LOADING
            try {
                data.value = TravelApi.service.getTravel()
                status.value = TravelApi.ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = TravelApi.ApiStatus.FAILED
            }
        }
    }
}