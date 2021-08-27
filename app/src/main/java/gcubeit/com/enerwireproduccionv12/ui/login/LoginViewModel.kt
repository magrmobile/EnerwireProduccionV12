package gcubeit.com.enerwireproduccionv12.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gcubeit.com.enerwireproduccionv12.data.Resource
import gcubeit.com.enerwireproduccionv12.data.network.response.login.LoginResponse
import gcubeit.com.enerwireproduccionv12.data.repository.login.LoginRepository
import gcubeit.com.enerwireproduccionv12.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository
) : BaseViewModel(repository) {
    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    fun login(
        username: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(username, password)
    }

    suspend fun storeData(token: String, operatorId: Int, lastStopDateTimeStart: String) {
        repository.storeData(token, operatorId, lastStopDateTimeStart)
    }
}