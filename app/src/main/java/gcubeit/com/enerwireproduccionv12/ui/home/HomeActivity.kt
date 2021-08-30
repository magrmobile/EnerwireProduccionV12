package gcubeit.com.enerwireproduccionv12.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import gcubeit.com.enerwireproduccionv12.NavAppDirections
import gcubeit.com.enerwireproduccionv12.R
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.data.network.ConnectivityInterceptor
import gcubeit.com.enerwireproduccionv12.databinding.ActivityHomeBinding
import gcubeit.com.enerwireproduccionv12.ui.login.LoginActivity
import gcubeit.com.enerwireproduccionv12.util.startNewActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext

@DelicateCoroutinesApi
class HomeActivity : AppCompatActivity(), CoroutineScope, KodeinAware {
    private lateinit var binding: ActivityHomeBinding

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override val kodein by closestKodein()

    private lateinit var job: Job

    private val homeViewModelFactory: HomeViewModelFactory by instance()
    private val userPreferences: UserPreferences by instance()

    private val connectivityInterceptor: ConnectivityInterceptor by instance()

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = SupervisorJob()

        /*launch {
            Toast.makeText(applicationContext, userPreferences.lastStopDateTimeStart.first().toString(), Toast.LENGTH_SHORT).show()
        }*/

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, homeViewModelFactory)
            .get(HomeViewModel::class.java)

        bindUI()

        val navController = findNavController(R.id.nav_home_fragment)
        setupActionBarWithNavController(navController)

        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_home_fragment) as NavHostFragment
        //val navBottomController = navHostFragment.navController
        //binding.bottomNavView.setupWithNavController(navBottomController)

        //binding.bottomNavView.setupWithNavController(navController)

        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                0 -> {
                    navController.navigate(R.id.dashboardFragment)
                    true
                }
                else -> {
                    val action = NavAppDirections.toStopsFragment(menuItem.itemId, menuItem.groupId , menuItem.title.toString())
                    navController.navigate(action)
                    true
                }
            }
        }
    }

    private fun bindUI() = launch {
        val menu = binding.bottomNavView.menu

        val currentMachines = viewModel.machines.await()
        currentMachines.observe(this@HomeActivity, {
            menu.clear()

            menu.add(
                Menu.NONE,
                0,
                Menu.NONE,
                "Home"
            ).setIcon(R.drawable.ic_home)

            if(it.size > 4) {
                Toast.makeText(applicationContext, "Ha excedido la cantidad de maquinas asignadas, consulte a su Administrador", Toast.LENGTH_SHORT).show()
            } else {
                it.forEach {
                    with(menu) {
                        add(
                            it.processId,
                            it.id,
                            it.processId,
                            it.machineName
                        ).setIcon(R.drawable.ic_machine)
                    }
                }
            }
        })
    }

    fun logout() = lifecycleScope.launch {
        val authToken = userPreferences.authToken.first()
        val api = AppApiService(connectivityInterceptor, authToken)
        viewModel.logout(api)
        userPreferences.clear()
        startNewActivity(LoginActivity::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.topbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.tb_homeFragment -> true
            R.id.tb_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_home_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}