package gcubeit.com.enerwireproduccionv12

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.ui.home.HomeActivity
import gcubeit.com.enerwireproduccionv12.ui.login.LoginActivity
import gcubeit.com.enerwireproduccionv12.util.startNewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val userPreferences = UserPreferences(this)

        userPreferences.authToken.asLiveData().observe(this, {
            val activity = if(it == null) LoginActivity::class.java else HomeActivity::class.java
            startNewActivity(activity)
        })
    }
}