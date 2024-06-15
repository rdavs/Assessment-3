package org.d3if3008.assessment3.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if3008.assessment3.model.Bukti
import org.d3if3008.assessment3.network.TravelApi
import java.io.ByteArrayOutputStream

class JadwalViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Bukti>())
        private set

    var status = MutableStateFlow(TravelApi.ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = TravelApi.ApiStatus.LOADING
            try {
                data.value = TravelApi.service.getTravel(userId)
                status.value = TravelApi.ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = TravelApi.ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, nama: String, keberangkatan: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = TravelApi.service.postTravel(
                    userId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    keberangkatan.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )
                if (result.status == "success")
                    retrieveData(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size
        )
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody
        )
    }

    fun clearMessage() {
        errorMessage.value = null
    }
}