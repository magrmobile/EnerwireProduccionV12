package gcubeit.com.enerwireproduccionv12.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import gcubeit.com.enerwireproduccionv12.R
import gcubeit.com.enerwireproduccionv12.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setupActionBarWithNavController(findNavController(R.id.nav_app_fragment))
    }
}