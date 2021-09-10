package gcubeit.com.enerwireproduccionv12.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbProduct

@Dao
interface DbProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<DbProduct>)
    
    @Query("SELECT * FROM product_table")
    fun getAllProducts(): LiveData<List<DbProduct>>

    //@Query("SELECT p.* FROM product_table AS p JOIN machine_table m ON m.id = :machineId WHERE p.processId = m.processId")
    @Query("SELECT * FROM product_table WHERE processId = :processId")
    fun getProductsByProcess(processId: Int): List<DbProduct>

    @Query("SELECT * FROM product_table WHERE id = :id")
    fun getProduct(id: Int): LiveData<DbProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: DbProduct)

    @Query("DELETE FROM product_table")
    suspend fun deleteAll()
}