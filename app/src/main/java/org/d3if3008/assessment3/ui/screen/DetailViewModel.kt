package org.d3if3008.assessment3.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3008.assessment3.database.TravelDao
import org.d3if3008.assessment3.model.Travel

class DetailViewModel(private val dao: TravelDao) : ViewModel() {

    fun insert(nama: String, keberangkatan: String, tujuan: String, jam: String) {
        val travel = Travel(
            nama = nama,
            keberangkatan = keberangkatan,
            tujuan = tujuan,
            jam = jam
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(travel)
        }
    }

    suspend fun getTravel(id: Long): Travel? {
        return dao.getTravelById(id)
    }

    fun update(id: Long, nama: String, keberangkatan: String, tujuan: String, jam: String) {
        val travel = Travel(
            id = id,
            nama = nama,
            keberangkatan = keberangkatan,
            tujuan = tujuan,
            jam = jam
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(travel)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}