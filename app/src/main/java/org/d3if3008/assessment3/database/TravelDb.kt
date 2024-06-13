package org.d3if3008.assessment3.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3008.assessment3.model.Travel

@Database(entities = [Travel::class], version = 1, exportSchema = false)
abstract class TravelDb : RoomDatabase() {
    abstract val dao: TravelDao

    companion object {
        @Volatile
        private var INSTANCE: TravelDb? = null

        fun getInstance(context: Context): TravelDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TravelDb::class.java,
                        "travel.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}