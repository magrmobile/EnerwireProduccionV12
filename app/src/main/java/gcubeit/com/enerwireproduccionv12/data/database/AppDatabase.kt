package gcubeit.com.enerwireproduccionv12.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gcubeit.com.enerwireproduccionv12.R
import gcubeit.com.enerwireproduccionv12.data.database.entity.*
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [
        DbMachine::class,
        DbCode::class,
        DbProduct::class,
        DbOperator::class,
        DbColor::class,
        DbConversion::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dbMachineDao(): DbMachineDao
    abstract fun dbCodeDao(): DbCodeDao
    abstract fun dbProductDao(): DbProductDao
    abstract fun dbOperatorDao(): DbOperatorDao
    abstract fun dbColorDao(): DbColorDao
    abstract fun dbConversionDao(): DbConversionDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context, scope: CoroutineScope) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context, scope).also { instance = it }
        }


        private fun buildDatabase(
            context: Context,
            scope: CoroutineScope
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