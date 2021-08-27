package gcubeit.com.enerwireproduccionv12.data.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    context: Context
){
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(name = "my_data_store")

    companion object {
        private val KEY_AUTH = stringPreferencesKey("key_auth")
        private val KEY_OPERATOR_ID = intPreferencesKey("key_operator_id")
        private val KEY_LAST_STOP_DATETIME_START = stringPreferencesKey("key_last_stop_datetime_start")
    }

    val authToken: Flow<String> = dataStore.data.map { it[KEY_AUTH] ?: "" }
    val operatorId: Flow<Int> = dataStore.data.map { it[KEY_OPERATOR_ID] ?: -1 }
    val lastStopDateTimeStart: Flow<String> = dataStore.data.map { it[KEY_LAST_STOP_DATETIME_START] ?: "" }

    suspend fun storeData(
        authToken: String,
        operatorId: Int,
        lastStopDateTimeStart: String
    ){
        dataStore.edit {
            it[KEY_AUTH] = authToken
            it[KEY_OPERATOR_ID] = operatorId
            it[KEY_LAST_STOP_DATETIME_START] = lastStopDateTimeStart
        }
    }

    suspend fun clear(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}