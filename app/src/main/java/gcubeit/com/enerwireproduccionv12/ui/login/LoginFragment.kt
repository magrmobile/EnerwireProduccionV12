package gcubeit.com.enerwireproduccionv12.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import gcubeit.com.enerwireproduccionv12.data.AppApiService
import gcubeit.com.enerwireproduccionv12.data.Resource
import gcubeit.com.enerwireproduccionv12.data.repository.login.LoginRepository
import gcubeit.com.enerwireproduccionv12.databinding.LoginFragmentBinding
import gcubeit.com.enerwireproduccionv12.ui.base.BaseFragment
import gcubeit.com.enerwireproduccionv12.ui.home.HomeActivity
import gcubeit.com.enerwireproduccionv12.util.enable
import gcubeit.com.enerwireproduccionv12.util.handleApiError
import gcubeit.com.enerwireproduccionv12.util.startNewActivity
import gcubeit.com.enerwireproduccionv12.util.visible
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<LoginViewModel>() {

//    companion object {
//        fun newInstance() = LoginFragment()
//    }

    private lateinit var binding: LoginFragmentBinding
    private lateinit var factory: LoginViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun login(){
        val username = binding.etUsername.editText?.text.toString().trim()
        val password = binding.etPassword.editText?.text.toString().trim()

        binding.loginProgressBar.visible(true)
        viewModel.login(username, password)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        factory = LoginViewModelFactory(LoginRepository(AppApiService(connectivityInterceptor), userPreferences))
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        binding.loginProgressBar.visible(false)
        binding.btnLogin.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, {
            binding.loginProgressBar.visible(it is Resource.Loading)
            when(it){
                is Resource.Success -> {
                    //Toast.makeText(requireContext(), it.value.id.toString(), Toast.LENGTH_LONG).show()
                    //val deviceId: String = getIMEIDeviceId(requireContext())
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.jwt)
                        viewModel.saveOperatorId(it.value.id)
                        requireActivity().startNewActivity(HomeActivity::class.java)
                    }

                }
                is Resource.Failure -> handleApiError(it){ login() }
            }
        })

        binding.etPassword.editText!!.addTextChangedListener {
            val username = binding.etUsername.editText?.text.toString().trim()
            binding.btnLogin.enable(username.isNotEmpty() && it.toString().isNotEmpty())
        }

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
//    }
}