package gcubeit.com.enerwireproduccionv12.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv12.data.database.entity.relations.StopsDetails
import gcubeit.com.enerwireproduccionv12.data.database.views.StopsDashboard

@Dao
interface DbStopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stops: List<DbStop>)
    
    @Query("SELECT * FROM stop_table")
    fun getAllStops(): LiveData<List<DbStop>>

    @Query("SELECT * FROM stop_table WHERE id = :id")
    fun getStop(id: Int): LiveData<DbStop>

    @Transaction
    @Query("SELECT * FROM stop_table WHERE machineId = :machineId")
    fun getStopsByMachine(machineId: Int): LiveData<List<StopsDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stop: DbStop)

    @Query("DELETE FROM stop_table")
    suspend fun deleteAll()

    @Query("DELETE FROM stop_table WHERE id = :stopId")
    suspend fun deleteStop(stopId: Int)

    @Query("SELECT MAX(stopDatetimeEnd) AS lastStopDateTime from stop_table WHERE machineId = :machineId")
    fun getLastStopDateTime(machineId: Int): LiveData<String>

    @Query("SELECT m.machineName, m.processId, MAX(s.stopDatetimeEnd) AS lastStopDateTimeEnd, COUNT(*) quantityStops FROM stop_table AS s JOIN machine_table AS m ON m.id = s.machineId GROUP BY m.machineName, m.processId")
    fun getStopsDashboard(): LiveData<List<StopsDashboard>>

    //@Query("SELECT * FROM stop_table")
    //suspend fun getStopAndMachine(): List<StopsDetails>

    /*@Transaction
    @Query("SELECT * FROM v_stops_detail WHERE machineId = :machineId")
    fun vGetStopsByMachine(machineId: Int): LiveData<List<StopsDetail>>*/
}