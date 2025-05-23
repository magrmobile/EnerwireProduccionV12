package gcubeit.com.enerwireproduccionv13.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbColor

@Dao
interface DbColorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(colors: List<DbColor>)
    
    @Query("SELECT * FROM color_table")
    fun getAllColors(): LiveData<List<DbColor>>

    @Query("SELECT * FROM color_table WHERE id = :id")
    fun getColor(id: Int): LiveData<DbColor>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(color: DbColor)

    @Query("DELETE FROM color_table")
    suspend fun deleteAll()
}