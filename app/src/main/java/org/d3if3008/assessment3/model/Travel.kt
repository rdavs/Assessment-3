package org.d3if3008.assessment3.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "travel")
data class Travel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val keberangkatan: String,
    val tujuan: String,
    val jam: String
)