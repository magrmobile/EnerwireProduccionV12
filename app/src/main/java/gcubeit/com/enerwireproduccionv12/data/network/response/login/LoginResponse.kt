package gcubeit.com.enerwireproduccionv12.data.network.response.login

data class LoginResponse(
    val active_user: String,
    val id: Int,
    val jwt: String,
    val message: String,
    val name: String,
    val role: String,
    val success: Boolean,
    val supervisor_id: Any,
    val username: String
)