package gcubeit.com.enerwireproduccionv13.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import gcubeit.com.enerwireproduccionv13.NavAppDirections
import gcubeit.com.enerwireproduccionv13.R
import gcubeit.com.enerwireproduccionv13.data.AppApiService
import gcubeit.com.enerwireproduccionv13.data.database.DbStopDao
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv13.data.network.ConnectionLiveData
import gcubeit.com.enerwireproduccionv13.data.network.ConnectivityInterceptor
import gcubeit.com.enerwireproduccionv13.data.network.response.SimpleResponse
import gcubeit.com.enerwireproduccionv13.databinding.ActivityHomeBinding
import gcubeit.com.enerwireproduccionv13.util.visible
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext
import android.content.ComponentName

@DelicateCoroutinesApi
class HomeActivity : AppCompatActivity(), CoroutineScope, KodeinAware {
    private lateinit var binding: ActivityHomeBinding

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override val kodein by closestKodein()

    private lateinit var job: Job

    private val homeViewModelFactory: HomeViewModelFactory by instance()
    //private val userPreferences: UserPreferences by instance()
    private val dbStopDao: DbStopDao by instance()
    //private val stopRepository: StopRepositoryImpl by instance()

    private val connectivityInterceptor: ConnectivityInterceptor by instance()

    private lateinit var viewModel: HomeViewModel

    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        connectionLiveData = ConnectionLiveData(this)
        super.onCreate(savedInstanceState)

        job = SupervisorJob()

        syncStops()

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
                    //Toast.makeText(this, menuItem.itemId.toString(), Toast.LENGTH_SHORT).show()
                    val action = NavAppDirections.toStopsFragment(menuItem.itemId, 0, menuItem.groupId , menuItem.title.toString())
                    navController.navigate(action)
                    true
                }
            }
        }
    }

    private fun syncStops() {
        connectionLiveData.observe(this, Observer{ isNetworkAvailable ->
            if(!isNetworkAvailable) {
                binding.tvConnectionStatus.visible(true)
                binding.tvConnectionStatus.text = getString(R.string.ErrorConnectionMessage)
            } else {
                binding.tvConnectionStatus.visible(false)
                launch(Dispatchers.IO) {
                    val stops = dbStopDao.getAllStopsUnSync()
                    stops.forEach { stop ->
                        when(stop.sync_status) {
                            1 -> {
                                addStop(stop)
                            }
                            2 -> {
                                updateStop(stop)
                            }
                        }

                    }
                }
            }
        })
    }

    @Suppress("DEPRECATION", "COMPATIBILITY_WARNING")
    private fun bindUI() = launch {
        val menu = binding.bottomNavView.menu

        val currentMachines = viewModel.machines.await()
        currentMachines.observe(this@HomeActivity) {
            menu.clear()

            menu.add(
                Menu.NONE,
                0,
                Menu.NONE,
                "Home"
            ).setIcon(R.drawable.ic_home)

            if (it.size > 4) {
                Toast.makeText(
                    applicationContext,
                    "Ha excedido la cantidad de maquinas asignadas, consulte a su Administrador",
                    Toast.LENGTH_SHORT
                ).show()
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
        }
    }

    /*private fun logout() = lifecycleScope.launch {
        val authToken = userPreferences.authToken.first()
        val api = AppApiService(connectivityInterceptor, authToken)
        viewModel.logout(api)
        userPreferences.clear()
        startNewActivity(LoginActivity::class.java)
    }*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.topbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.calculator -> {
                val intent = Intent()
                intent.action = Intent.ACTION_MAIN
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.component = ComponentName(
                    "com.android.calculator2",
                    "com.android.calculator2.Calculator"
                )
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this@HomeActivity,getString(R.string.calc_error),Toast.LENGTH_LONG).show()
                }
                true
            }
            R.id.tb_Sync -> {
                syncStops()
                true
            }
            R.id.tb_logout -> {
                finish()
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

    private suspend fun addStop(stop: DbStop) {
        coroutineScope {
            val api = AppApiService(connectivityInterceptor)
            val response = api.addStop(
                operatorId = stop.operatorId,
                machineId = stop.machineId,
                productId = stop.productId,
                colorId = stop.colorId,
                codeId = stop.codeId,
                conversionId = stop.conversionId,
                quantity = stop.quantity,
                meters = stop.meters,
                comment = stop.comment,
                stopDateTimeStart = stop.stopDatetimeStart,
                stopDateTimeEnd = stop.stopDatetimeEnd
            )
            if (response.isSuccessful) {
                stop.idRemote = response.body()?.id!!.toLong()
                launch(Dispatchers.IO) {
                    stop.idRemote.let { dbStopDao.updateSyncStatus(stop.id, it) }
                }
            }
        }
    }

    private fun updateStop(stop: DbStop) {
        val api = AppApiService(connectivityInterceptor)
        val call = api.updateStop(
            stopId = stop.idRemote,
            operatorId = stop.operatorId,
            machineId = stop.machineId,
            productId = stop.productId,
            colorId = stop.colorId,
            codeId = stop.codeId,
            conversionId = stop.conversionId,
            quantity = stop.quantity,
            meters = stop.meters,
            comment = stop.comment
        )
        call.enqueue(object : Callback<SimpleResponse> {
            override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                if(response.isSuccessful)  {
                    //stop.idRemote = response.body()?.id!!
                    launch(Dispatchers.IO) {
                        stop.idRemote.let { dbStopDao.updateSyncStatus(stop.id, it) }
                    }
                }
            }
        })
    }
}