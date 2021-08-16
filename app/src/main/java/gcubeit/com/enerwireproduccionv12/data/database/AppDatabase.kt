package gcubeit.com.enerwireproduccionv12.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import gcubeit.com.enerwireproduccionv12.R
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.database.entity.*
import gcubeit.com.enerwireproduccionv12.data.database.views.StopsDetail
import gcubeit.com.enerwireproduccionv12.data.network.ConnectivityInterceptorImpl
import gcubeit.com.enerwireproduccionv12.util.getIMEIDeviceId
import gcubeit.com.enerwireproduccionv12.util.ioThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

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
    abstract fun dbStopDao(): DbStopDao

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