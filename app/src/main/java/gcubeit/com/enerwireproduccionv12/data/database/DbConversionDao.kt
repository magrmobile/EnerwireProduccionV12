package gcubeit.com.enerwireproduccionv12.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbConversion

@Dao
interface DbConversionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(conversions: List<DbConversion>)
    
    @Query("SELECT * FROM conversion_table")
    fun getAllConversions(): LiveData<List<DbConversion>>

    @Query("SELECT * FROM conversion_table WHERE id = :id")
    fun getConversion(id: Int): LiveData<DbConversion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversion: DbConversion)

    @Query("DELETE FROM conversion_table")
    suspend fun deleteAll()
}