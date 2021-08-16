package gcubeit.com.enerwireproduccionv12.ui.home

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import gcubeit.com.enerwireproduccionv12.R
import gcubeit.com.enerwireproduccionv12.databinding.HomeFragmentBinding
import gcubeit.com.enerwireproduccionv12.ui.base.BaseFragment
import gcubeit.com.enerwireproduccionv12.ui.dashboard.DashboardFragment
import gcubeit.com.enerwireproduccionv12.ui.stops.StopsFragment
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class HomeFragment : BaseFragment<HomeViewModel>() {
    private val homeViewModelFactory: HomeViewModelFactory by instance()
    private lateinit var binding: HomeFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        bindUI()

        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                0 -> {
                    val fragment = DashboardFragment.newInstance()
                    replaceFragment(fragment)
                    true
                }
                else -> {
                    val fragment = StopsFragment.newInstance(
                        menuItem.itemId,
                        menuItem.title.toString()
                    )
                    replaceFragment(fragment)
                    true
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = childFragmentManager
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.nav_app_fragment, fragment)
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, homeViewModelFactory)
            .get(HomeViewModel::class.java)
    }

    private fun bindUI() = launch {
        val menu = binding.bottomNavView.menu

        val currentMachines = viewModel.machines.await()
        currentMachines.observe(viewLifecycleOwner, Observer {
            menu.clear()

            val newItem: MenuItem = menu.add(
                Menu.NONE,
                0,
                Menu.NONE,
                "Home"
            ).setIcon(R.drawable.ic_home)

            if(it.size > 4) {
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