package gcubeit.com.enerwireproduccionv12.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import gcubeit.com.enerwireproduccionv12.R
import gcubeit.com.enerwireproduccionv12.databinding.HomeFragmentBinding
import gcubeit.com.enerwireproduccionv12.ui.base.BaseFragment
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class HomeFragment : BaseFragment<HomeViewModel>() {
    private val homeViewModelFactory: HomeViewModelFactory by instance()
    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.tb_homeFragment -> {
                    true
                }
                R.id.tb_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }

        binding.bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                else -> {
                    //val machineId = bundleOf(menuItem.id of machine_id)
                    true
                }
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this, homeViewModelFactory)
            .get(HomeViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {
        val menu = binding.bottomNavView.menu

        val currentMachines = viewModel.machines.await()
        currentMachines.observe(viewLifecycleOwner, Observer {
            menu.clear()
            if(it.size >= 5) {
                Toast.makeText(requireContext(), "Ha excedido la cantidad de maquinas asignadas, consulte a su Administrador", Toast.LENGTH_SHORT).show()
            } else {
                it.forEach {
                    with(menu) {
                        add(
                            Menu.NONE,
                            it.id,
                            Menu.NONE,
                            it.machineName
                        ).setIcon(R.drawable.ic_machine)
                    }
                }
            }
        })
    }
}