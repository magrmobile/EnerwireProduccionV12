package gcubeit.com.enerwireproduccionv12.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import gcubeit.com.enerwireproduccionv12.R
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.ui.home.HomeActivity
import gcubeit.com.enerwireproduccionv12.util.startNewActivity
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val userPreferences = UserPreferences(this)

        userPreferences.authToken.asLiveData().observe(this, {
            //Toast.makeText(this, "Login Auth Token: $it", Toast.LENGTH_SHORT).show()
            if(it != null) {
                if (it.isNotEmpty()) {
                    startNewActivity(HomeActivity::class.java)
                }
            }
        })
    }
}