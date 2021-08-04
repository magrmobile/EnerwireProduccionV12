package gcubeit.com.enerwireproduccionv12.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbOperator
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbProduct

@Dao
interface DbOperatorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(operators: List<DbOperator>)
    
    @Query("SELECT * FROM operator_table")
    fun getAllOperators(): LiveData<List<DbOperator>>

    @Query("SELECT * FROM operator_table WHERE processId = :processId")
    fun getOperatorsByProcess(processId: Int): LiveData<List<DbOperator>>

    @Query("SELECT * FROM operator_table WHERE id = :id")
    fun getOperator(id: Int): LiveData<DbOperator>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(operator: DbOperator)

    @Query("DELETE FROM operator_table")
    suspend fun deleteAll()
}