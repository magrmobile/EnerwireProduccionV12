package gcubeit.com.enerwireproduccionv13.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbCode

@Dao
interface DbCodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(codes: List<DbCode>)
    
    @Query("SELECT * FROM code_table")
    fun getAllCodes(): LiveData<List<DbCode>>

    @Query("SELECT * FROM code_table WHERE id = :id")
    fun getCode(id: Int): LiveData<DbCode>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(code: DbCode)

    @Query("DELETE FROM code_table")
    suspend fun deleteAll()
}