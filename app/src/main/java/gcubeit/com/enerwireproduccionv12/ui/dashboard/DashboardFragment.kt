package gcubeit.com.enerwireproduccionv12.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import gcubeit.com.enerwireproduccionv12.databinding.DashboardFragmentBinding
import gcubeit.com.enerwireproduccionv12.ui.base.BaseFragment
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class DashboardFragment : BaseFragment<DashboardViewModel>() {
    companion object {
        fun newInstance() = DashboardFragment()
    }

    private val dashboardViewModelFactory: DashboardViewModelFactory by instance()
    private lateinit var binding: DashboardFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DashboardFragmentBinding.inflate(inflater, container, false)

//        binding.btnStops.setOnClickListener {
//            findNavController().navigate(R.id.action_dashboardFragment_to_stopsFragment)
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {
        val data = viewModel.data.await()
        data.observe(viewLifecycleOwner, Observer{
            binding.tvTitle.text = it.toString()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, dashboardViewModelFactory)
            .get(DashboardViewModel::class.java)
        // TODO: Use the ViewModel
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
//    }
}