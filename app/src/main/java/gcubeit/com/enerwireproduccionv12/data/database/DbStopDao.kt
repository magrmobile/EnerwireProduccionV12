package gcubeit.com.enerwireproduccionv12.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbCode
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv12.data.database.views.StopsDetail

@Dao
interface DbStopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stops: List<DbStop>)
    
    @Query("SELECT * FROM stop_table")
    fun getAllStops(): LiveData<List<DbStop>>

    @Query("SELECT * FROM stop_table WHERE id = :id")
    fun getStop(id: Int): LiveData<DbStop>

    @Query("SELECT * FROM stop_table WHERE machineId = :machineId")
    fun getStopsByMachine(machineId: Int): LiveData<List<DbStop>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stop: DbStop)

    @Query("DELETE FROM stop_table")
    suspend fun deleteAll()

    /*@Transaction
    @Query("SELECT * FROM v_stops_detail WHERE machineId = :machineId")
    fun vGetStopsByMachine(machineId: Int): LiveData<List<StopsDetail>>*/
}