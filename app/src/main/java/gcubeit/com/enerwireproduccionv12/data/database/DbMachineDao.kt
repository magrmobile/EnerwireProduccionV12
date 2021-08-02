package gcubeit.com.enerwireproduccionv12.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbMachine

@Dao
interface DbMachineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(machines: List<DbMachine>)
    
    @Query("SELECT * FROM machine_table")
    fun getAllMachines(): LiveData<List<DbMachine>>

    @Query("SELECT * FROM machine_table WHERE id = :id")
    fun getMachine(id: Int): LiveData<DbMachine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(machine: DbMachine)

    @Query("DELETE FROM machine_table")
    suspend fun deleteAll()
}