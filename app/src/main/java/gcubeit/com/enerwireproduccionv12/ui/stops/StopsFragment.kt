package gcubeit.com.enerwireproduccionv12.ui.stops

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import gcubeit.com.enerwireproduccionv12.data.database.AppDatabase
import gcubeit.com.enerwireproduccionv12.data.database.DbStopDao
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.data.network.datasource.stop.StopNetworkDatasource
import gcubeit.com.enerwireproduccionv12.data.network.datasource.stop.StopNetworkDatasourceImpl
import gcubeit.com.enerwireproduccionv12.data.repository.dashboard.DashboardRepository
import gcubeit.com.enerwireproduccionv12.data.repository.stop.StopRepositoryImpl
import gcubeit.com.enerwireproduccionv12.databinding.StopsFragmentBinding
import gcubeit.com.enerwireproduccionv12.ui.base.BaseFragment
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

private const val ARG_MACHINE_ID = "machine_id"
private const val ARG_MACHINE_NAME = "machine_name"

class StopsFragment : BaseFragment<StopsViewModel>()  {

    private var param1: Int? = null
    private var param2: String? = null

    companion object {
        fun newInstance(param1: Int, param2: String?) =
            StopsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_MACHINE_ID, param1)
                    putString(ARG_MACHINE_NAME, param2)
                }
            }
    }

    private val dbStopDao : DbStopDao by instance()
    private val stopNetworkDatasource : StopNetworkDatasource by instance()
    //private val userPreferences : UserPreferences by instance()

    private lateinit var stopRepository: StopRepositoryImpl
    private lateinit var stopsViewModelFactory: StopsViewModelFactory // by instance()
    private lateinit var binding: StopsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_MACHINE_ID)
            param2 = it.getString(ARG_MACHINE_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = StopsFragmentBinding.inflate(inflater, container, false)

        requireActivity().title = param2

        binding.tvTitle.text = param2

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {
        val data = viewModel.machineData.await()
        data.observe(viewLifecycleOwner, Observer{
            binding.tvTitle.text = it.toString()
        })
        //val userId = userPreferences.operatorId.first()
        //Toast.makeText(requireContext(), userId.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        stopRepository = StopRepositoryImpl(dbStopDao, stopNetworkDatasource, userPreferences)
        stopsViewModelFactory = StopsViewModelFactory(stopRepository, param1)
        viewModel = ViewModelProvider(this, stopsViewModelFactory)
            .get(StopsViewModel::class.java)
    }
}