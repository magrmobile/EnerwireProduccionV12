package gcubeit.com.enerwireproduccionv12.data.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    context: Context
){
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(name = "my_data_store")

    val authToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH]
        }

    suspend fun saveAuthToken(authToken: String){
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
        }
    }

    val imei: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_IMEI]
        }

    suspend fun saveImei(imei: String){
        dataStore.edit { preferences ->
            preferences[KEY_IMEI] = imei
        }
    }

    suspend fun clear(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val KEY_AUTH = stringPreferencesKey("key_auth")
        private val KEY_IMEI = stringPreferencesKey("key_imei")
    }
}