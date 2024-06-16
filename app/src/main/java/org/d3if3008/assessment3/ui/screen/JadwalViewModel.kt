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
import org.d3if3008.assessment3.model.BuktiCreate
import org.d3if3008.assessment3.model.ImageData
import org.d3if3008.assessment3.network.ApiStatus
import org.d3if3008.assessment3.network.ImageApi
import org.d3if3008.assessment3.network.TravelApi
import java.io.ByteArrayOutputStream

class JadwalViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Bukti>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var querySuccess = mutableStateOf(false)
        private set


    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = TravelApi.service.getAllPesanan(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(email: String, nama_pemesan: String, destinasi: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val upload = ImageApi.imgService.uploadImg(
                    image = bitmap.toMultipartBody()
                )

                if (upload.success) {

                    TravelApi.service.addPesanan(
                        BuktiCreate(
                            email,
                            transformImageData(upload.data),
                            nama_pemesan,
                            destinasi,
                            upload.data.deletehash!!
                        )
                    )
                    status.value = ApiStatus.LOADING
                    retrieveData(email)
                    querySuccess.value = true
                }
            } catch (e: Exception) {
                Log.d("MainVM", "${e.message}")
                if (e.message == "HTTP 500 ") {
                    errorMessage.value = "Error: Database Idle, harap masukkan data kembali."
                } else {
                    errorMessage.value = "Error: ${e.message}"
                    Log.d("MainViewModel", "Failure: ${e.message}")
                }
            }
        }
    }

    fun deleteData(email: String, buktiId: Int, deleteHash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val upload = ImageApi.imgService.deleteImg(
                    deleteHash = deleteHash
                )
                if (upload.success) {
                    TravelApi.service.deletePesanan(buktiId, email)
                    querySuccess.value = true
                    retrieveData(email)
                }
            } catch (e: Exception) {
                if (e.message == "HTTP 500 ") {
                    errorMessage.value = "Error: Database Idle, harap eksekusi data kembali."
                } else {
                    errorMessage.value = "Error: ${e.message}"
                    Log.d("MainViewModel", "Failure: ${e.message}")
                }
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
        return MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
    }

    fun transformImageData(imageData: ImageData): String {
        val extension = when (imageData.type) {
            "image/png" -> "png"
            "image/jpeg" -> "jpg"
            "image/gif" -> "gif"
            else -> throw IllegalArgumentException("Unsupported image type")
        }
        return "${imageData.id}.$extension"
    }

    fun clearMessage() {
        errorMessage.value = null
        querySuccess.value = false
    }

}