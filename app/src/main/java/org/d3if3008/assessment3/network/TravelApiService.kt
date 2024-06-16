package org.d3if3008.assessment3.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3008.assessment3.model.Bukti
import org.d3if3008.assessment3.model.BuktiCreate
import org.d3if3008.assessment3.model.OpStatus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://ass3api.vercel.app/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface TravelApiService {
    @POST("pesanan/")
    suspend fun addPesanan(
        @Body bukti: BuktiCreate
    ): OpStatus

    @GET("pesanan/")
    suspend fun getAllPesanan(
        @Query("user_email") email: String,
    ): List<Bukti>

    @DELETE("pesanan/{pesanan_id}")
    suspend fun deletePesanan(
        @Path("pesanan_id") id: Int,
        @Query("email") email: String
    ): OpStatus
}

object TravelApi {
    val service: TravelApiService by lazy {
        retrofit.create(TravelApiService::class.java)
    }

    fun getTravelUrl(imageId: String): String {
        return "${BASE_URL}image.php?id=$imageId"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }
