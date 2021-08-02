package gcubeit.com.enerwireproduccionv12.ui.home

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gcubeit.com.enerwireproduccionv12.R
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.repository.HomeRepository
import gcubeit.com.enerwireproduccionv12.data.repository.LoginRepository
import gcubeit.com.enerwireproduccionv12.databinding.HomeFragmentBinding
import gcubeit.com.enerwireproduccionv12.ui.base.BaseFragment
import gcubeit.com.enerwireproduccionv12.ui.login.LoginViewModelFactory

class HomeFragment : BaseFragment<HomeViewModel>() {

//    companion object {
//        fun newInstance() = HomeFragment()
//    }

    //private lateinit var viewModel: HomeViewModel
    private lateinit var factory: HomeViewModelFactory
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

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        factory = HomeViewModelFactory(HomeRepository())
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }
}