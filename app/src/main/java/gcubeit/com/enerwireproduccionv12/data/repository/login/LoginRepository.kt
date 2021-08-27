package gcubeit.com.enerwireproduccionv12.data.repository.login

import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.data.repository.BaseRepository

class LoginRepository(
    private val api: AppApiService,
    private val preferences: UserPreferences
): BaseRepository() {
    suspend fun login(
        username: String,
        password: String
    ) = safeApiCall {
        api.login(username, password)
    }

    suspend fun storeData(token: String, operatorId: Int, lastStopDateTimeStart: String){
        preferences.storeData(token, operatorId, lastStopDateTimeStart)
    }
}