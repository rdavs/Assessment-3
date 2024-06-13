package org.d3if3008.assessment3.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3008.assessment3.database.TravelDao
import org.d3if3008.assessment3.ui.screen.DetailViewModel
import org.d3if3008.assessment3.ui.screen.MainViewModel

class ViewModelFactory(
    private val dao: TravelDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}