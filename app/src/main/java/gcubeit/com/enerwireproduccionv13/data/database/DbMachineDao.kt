package gcubeit.com.enerwireproduccionv13.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbMachine
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbProduct

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

    @Query("SELECT lastOperatorId FROM machine_table WHERE id = :id")
    fun getLastOperatorId(id: Int): Int

    @Query("UPDATE machine_table SET lastOperatorId = :operatorId WHERE id = :id")
    fun updateOperatorId(id: Int, operatorId: Int)

    @Query("SELECT p.* FROM machine_table m JOIN product_table p ON p.id = m.lastProductId WHERE m.id = :id")
    fun getLastProductId(id: Int): DbProduct

    @Query("UPDATE machine_table SET lastProductId = :productId WHERE id = :id")
    fun updateProductId(id: Int, productId: Int)
}