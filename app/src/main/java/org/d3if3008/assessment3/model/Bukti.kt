package org.d3if3008.assessment3.model

data class Bukti(
    val pesanan_id: Int,
    val user_email: String,
    val nama_pemesan: String,
    val destinasi: String,
    val image_id: String,
    val delete_hash: String,
    val created_at: String
)

data class BuktiCreate(
    val user_email: String,
    val image_id: String,
    val nama_pemesan: String,
    val destinasi: String,
    val delete_hash: String
)
