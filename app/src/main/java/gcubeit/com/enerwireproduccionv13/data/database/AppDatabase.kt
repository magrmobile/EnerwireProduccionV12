package gcubeit.com.enerwireproduccionv13.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gcubeit.com.enerwireproduccionv13.R
import gcubeit.com.enerwireproduccionv13.data.database.entity.*

@Database(
    entities = [
        DbMachine::class,
        DbCode::class,
        DbProduct::class,
        DbOperator::class,
        DbColor::class,
        DbConversion::class,
        DbStop::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dbMachineDao(): DbMachineDao
    abstract fun dbCodeDao(): DbCodeDao
    abstract fun dbProductDao(): DbProductDao
    abstract fun dbOperatorDao(): DbOperatorDao
    abstract fun dbColorDao(): DbColorDao
    abstract fun dbConversionDao(): DbConversionDao
    abstract fun dbStopDao(): DbStopDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(
            context: Context
        ) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                context.getString(R.string.database_name)
            )
                //.addCallback(AppDatabaseCallback(scope, context))
                .fallbackToDestructiveMigration()
                .build()
    }

}