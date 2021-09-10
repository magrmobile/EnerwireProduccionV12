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

    @Query("SELECT * FROM stop_table WHERE sync_status IN (1, 2)")
    fun getAllStopsUnSync(): List<DbStop>

    @Query("SELECT * FROM stop_table WHERE id = :id")
    fun getStop(id: Int): LiveData<DbStop>

    @Transaction
    @Query("SELECT * FROM stop_table WHERE machineId = :machineId ORDER BY id ASC")
    fun getStopsByMachine(machineId: Int): LiveData<List<StopsDetails>>

    @Insert
    suspend fun insert(stop: DbStop)

    @Query("DELETE FROM stop_table")
    suspend fun deleteAll()

    @Query("DELETE FROM stop_table WHERE id = :stopId")
    suspend fun deleteStop(stopId: Int)

    @Query("SELECT MAX(stopDatetimeEnd) AS lastStopDateTime from stop_table WHERE machineId = :machineId")
    fun getLastStopDateTime(machineId: Int): LiveData<String>

    @Query("SELECT m.id AS machineId, m.machineName, m.processId, MAX(s.stopDatetimeEnd) AS lastStopDateTimeEnd, COUNT(*) quantityStops, SUM(CASE WHEN sync_status = 0 THEN 1 ELSE 0 END) quantityLocal FROM stop_table AS s JOIN machine_table AS m ON m.id = s.machineId GROUP BY m.id, m.machineName, m.processId")
    fun getStopsDashboard(): LiveData<List<StopsDashboard>>

    @Query("UPDATE stop_table SET sync_status = 0, idRemote = :idRemote WHERE id = :stopId")
    suspend fun updateSyncStatus(stopId: Int, idRemote: Int)

    @Update
    suspend fun updateStop(vararg stop: DbStop)

    //@Query("SELECT * FROM stop_table")
    //suspend fun getStopAndMachine(): List<StopsDetails>

    /*@Transaction
    @Query("SELECT * FROM v_stops_detail WHERE machineId = :machineId")
    fun vGetStopsByMachine(machineId: Int): LiveData<List<StopsDetail>>*/
}