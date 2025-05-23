package gcubeit.com.enerwireproduccionv13.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import gcubeit.com.enerwireproduccionv13.databinding.DashboardFragmentBinding
import gcubeit.com.enerwireproduccionv13.ui.base.BaseFragment
import gcubeit.com.enerwireproduccionv13.util.visible
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

@DelicateCoroutinesApi
class DashboardFragment : BaseFragment<DashboardViewModel>() {
    private val dashboardViewModelFactory: DashboardViewModelFactory by instance()
    private lateinit var binding: DashboardFragmentBinding

    private val dashboardAdapter = DashboardAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DashboardFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, dashboardViewModelFactory).get(DashboardViewModel::class.java)

        // RecyclerView
        val recyclerView = binding.rvDashboard
        recyclerView.adapter = dashboardAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            //viewModel.data.await()
            val dashboardData = viewModel.dashboardData.await()
            dashboardData.observeForever { data ->
                dashboardAdapter.setData(data!!)
            }
            binding.dashboardProgress.visible(false)
        }

        return binding.root
    }
}