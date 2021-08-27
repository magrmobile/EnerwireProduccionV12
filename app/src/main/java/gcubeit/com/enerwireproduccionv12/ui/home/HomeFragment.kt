package gcubeit.com.enerwireproduccionv12.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gcubeit.com.enerwireproduccionv12.R
import gcubeit.com.enerwireproduccionv12.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {
    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*binding.topAppBar.setOnMenuItemClickListener { menuItem ->
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
        }*/

        //bindUI()

        /*binding.bottomNavView.setOnItemSelectedListener { menuItem ->
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
        }*/
    }

    /*private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = childFragmentManager
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.nav_app_fragment, fragment)
        }
    }*/


    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, homeViewModelFactory)
            .get(HomeViewModel::class.java)
    }*/

    /*private fun bindUI() = launch {
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
    }*/
}