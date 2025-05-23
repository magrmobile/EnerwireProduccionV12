package gcubeit.com.enerwireproduccionv13.ui.stops

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import gcubeit.com.enerwireproduccionv13.data.AppApiService
import gcubeit.com.enerwireproduccionv13.data.database.DbStopDao
import gcubeit.com.enerwireproduccionv13.data.network.datasource.stop.StopNetworkDatasourceImpl
import gcubeit.com.enerwireproduccionv13.data.repository.stop.StopRepositoryImpl
import gcubeit.com.enerwireproduccionv13.databinding.StopsFragmentBinding
import gcubeit.com.enerwireproduccionv13.ui.base.BaseFragment
import gcubeit.com.enerwireproduccionv13.util.visible
import kotlinx.coroutines.DelicateCoroutinesApi
//import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance


@DelicateCoroutinesApi
class StopsFragment : BaseFragment<StopsViewModel>()  {
    private val args by navArgs<StopsFragmentArgs>()

    private lateinit var binding: StopsFragmentBinding

    private val api: AppApiService by instance()
    private val dbStopDao : DbStopDao by instance()
    private val stopNetworkDatasource : StopNetworkDatasourceImpl by instance()

    private lateinit var stopRepository: StopRepositoryImpl
    private lateinit var stopsViewModelFactory: StopsViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StopsFragmentBinding.inflate(inflater, container, false)

        // StopsViewModel
        stopRepository = StopRepositoryImpl(api, dbStopDao, stopNetworkDatasource)
        stopsViewModelFactory = StopsViewModelFactory(stopRepository, args.machineId)
        viewModel = ViewModelProvider(this, stopsViewModelFactory).get(StopsViewModel::class.java)

        // RecyclerView
        val adapter = StopsAdapter(requireContext(), viewModel)
        val recyclerView = binding.rvStopsGrid
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val currentStops = viewModel.stopsData.await()
            currentStops.observeForever { stops ->
                adapter.setData(stops!!)
            }
        }

        if(args.processId == 1 || args.processId == 3) {
            binding.tvGridPackingH.visible(false)
            binding.tvGridQuantityH.visible(false)
        }

        binding.fab.setOnClickListener {
            launch {
                val action = StopsFragmentDirections.actionStopsFragmentToCreateFragment(args.machineId, 0, args.processId, args.title )
                it.findNavController().navigate(action)
            }
        }

        return binding.root
    }
}