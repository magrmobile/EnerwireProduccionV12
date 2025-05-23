package gcubeit.com.enerwireproduccionv13.data.network.response.login

data class LoginResponse(
    val success: Boolean,
    val id: Int,
    val supervisor_id: Any,
    val active_user: String,
    val role: String,
    val name: String,
    val username: String,
    val jwt: String,
    val message: String,
    val lastStopDateTimeStart: String
)