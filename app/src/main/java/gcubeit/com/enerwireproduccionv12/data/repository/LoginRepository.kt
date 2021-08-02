package gcubeit.com.enerwireproduccionv12.data.repository

import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences

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

    suspend fun saveAuthToken(token: String){
        preferences.saveAuthToken(token)
    }
}