package org.d3if3008.assessment3.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3008.assessment3.model.Travel

@Dao
interface TravelDao {
    @Insert
    suspend fun insert(travel: Travel)

    @Update
    suspend fun update(travel: Travel)

    @Query("SELECT * FROM travel ORDER BY keberangkatan DESC")
    fun getTravel(): Flow<List<Travel>>

    @Query("SELECT * FROM travel WHERE id = :id")
    suspend fun getTravelById(id: Long): Travel?

    @Query("DELETE FROM travel WHERE id = :id")
    suspend fun deleteById(id: Long)
}