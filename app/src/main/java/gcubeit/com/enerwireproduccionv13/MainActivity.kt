package gcubeit.com.enerwireproduccionv13

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gcubeit.com.enerwireproduccionv13.ui.home.HomeActivity
import gcubeit.com.enerwireproduccionv13.util.startNewActivity
import kotlinx.coroutines.DelicateCoroutinesApi

class MainActivity : AppCompatActivity() {
    @Suppress("OPT_IN_IS_NOT_ENABLED")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val userPreferences = UserPreferences(this)

        /*userPreferences.authToken.asLiveData().observe(this, {
            val activity = if(it == null) LoginActivity::class.java else HomeActivity::class.java
            startNewActivity(activity)
        })*/
        startNewActivity(HomeActivity::class.java)
    }
}