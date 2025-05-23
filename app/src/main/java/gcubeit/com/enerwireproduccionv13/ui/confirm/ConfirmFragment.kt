package gcubeit.com.enerwireproduccionv13.ui.confirm

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
//import gcubeit.com.enerwireproduccionv13.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv13.data.network.ConnectionLiveData
//import gcubeit.com.enerwireproduccionv13.data.network.ConnectivityInterceptorImpl
import gcubeit.com.enerwireproduccionv13.databinding.ConfirmFragmentBinding
import gcubeit.com.enerwireproduccionv13.util.visible
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext

@DelicateCoroutinesApi
class ConfirmFragment : Fragment(), CoroutineScope, KodeinAware {
    override val kodein by closestKodein()

    private lateinit var job: Job

    private val application: Application by instance()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    //private val userPreferences: UserPreferences by instance()
    //private lateinit var connectivityInterceptor: ConnectivityInterceptorImpl

    private val args by navArgs<ConfirmFragmentArgs>()

    private lateinit var binding: ConfirmFragmentBinding

    private lateinit var confirmViewModelFactory: ConfirmViewModelFactory

    private lateinit var viewModel: ConfirmViewModel

    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = ConnectionLiveData(application.applicationContext)
        //connectionLiveData = ConnectivityOnlineLiveData(application)
        job = SupervisorJob()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View     {
        binding = ConfirmFragmentBinding.inflate(inflater, container, false)

        // ViewModel
        confirmViewModelFactory = ConfirmViewModelFactory(application)
        viewModel = ViewModelProvider(this, confirmViewModelFactory).get(ConfirmViewModel::class.java)

        binding.confirmProgressBar.visible(false)

        binding.btnBack.setOnClickListener {
            /*if(args.action == 1) {
                val action = ConfirmFragmentDirections.toCreateFragment(args.machineId, args.dbStop.operatorId, args.processId, args.title)
                it.findNavController().navigate(action)
            }*/

            it.findNavController().popBackStack()
        }

        binding.btnConfirmStop.setOnClickListener {
            if(args.action == 1) {
                //saveToLocalStorage(args.dbStop)
                binding.confirmProgressBar.visible(true)
                addStop(args.dbStop)
                //Toast.makeText(requireContext(), args.dbStop.toString(), Toast.LENGTH_LONG).show()
            }
            if(args.action == 2) {
                binding.confirmProgressBar.visible(true)
                updateStop(args.dbStop)
                //Toast.makeText(requireContext(), args.dbStop.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvConfirmCode.text = args.stopConfirm.codeDescription
        binding.tvConfirmStopType.text = args.stopConfirm.codeType
        binding.tvConfirmOperator.text = args.stopConfirm.operatorName

        if(args.dbStop.productId != null && args.dbStop.codeId == 1) {
            if(args.dbStop.productId!! > 0) {
                binding.tvConfirmProduct.text = args.stopConfirm.productName
                binding.layoutProduct.visible(true)
            }
        } else {
            binding.tvConfirmProduct.text = ""
            binding.layoutProduct.visible(false)
        }

        if(args.dbStop.colorId != null) {
            if(args.dbStop.colorId!! > 0) {
                binding.tvConfirmColor.text = args.stopConfirm.colorName
                binding.layoutColor.visible(true)
            }
        } else {
            binding.tvConfirmColor.text = ""
            binding.layoutColor.visible(false)
        }

        /*if(args.dbStop.conversionId != null) {
            if(args.dbStop.conversionId!! > 0) {
                binding.tvConfirmConversion.text = args.stopConfirm.conversionDescription
                binding.layoutConversion.visible(true)
            }
        } else {
            binding.tvConfirmConversion.text = ""
            binding.layoutConversion.visible(false)
        }*/

        when(args.processId) {
            2, 4 -> {
                if(args.dbStop.conversionId != null) {
                    if (args.dbStop.conversionId!! > 0) {
                        binding.tvConfirmConversion.text = args.stopConfirm.conversionDescription
                        binding.layoutConversion.visible(true)
                    } else {
                        binding.tvConfirmConversion.text = ""
                        binding.layoutConversion.visible(false)
                    }
                }

                if(args.stopConfirm.quantity.isNullOrEmpty()) {
                    binding.layoutQuantity.visible(false)
                } else {
                    binding.layoutQuantity.visible(true)
                    binding.tvConfirmQuantity.text = args.stopConfirm.quantity
                }
            }
        }

        if(args.stopConfirm.meters!!.isNotEmpty()) {
            binding.tvConfirmMeters.text = args.stopConfirm.meters
            binding.layoutMeters.visible(true)
        } else {
            binding.tvConfirmMeters.text = ""
            binding.layoutMeters.visible(false)
        }

        if(args.stopConfirm.comment!!.isNotEmpty()) {
            binding.tvConfirmComments.text = args.stopConfirm.comment
            binding.layoutComments.visible(true)
        }

        binding.tvConfirmStartDateStopStep.text = args.stopConfirm.startStopDate
        binding.tvConfirmStartTimeStopStep.text = args.stopConfirm.startStopTime
        binding.tvConfirmEndDateStopStep.text = args.stopConfirm.endStopDate

        return binding.root
    }

    fun addStop(stop: DbStop) {
        runBlocking {
            viewModel.addStop(stop)
            delay(1000L)
        }
        Toast.makeText(requireContext(), "Paro Insertado Satisfactoriamente", Toast.LENGTH_LONG).show()
        val action = ConfirmFragmentDirections.actionConfirmFragmentToStopsFragment(args.machineId, 0, args.processId, args.title)
        findNavController().navigate(action)
    }

    private fun updateStop(stop: DbStop) {
        runBlocking {
            viewModel.updateStop(stop)
            delay(1000L)
        }
        Toast.makeText(requireContext(), "Paro Actualizado Satisfactoriamente", Toast.LENGTH_LONG).show()
        val action = ConfirmFragmentDirections.actionConfirmFragmentToStopsFragment(args.machineId, 0, args.processId, args.title)
        findNavController().navigate(action)
        //Toast.makeText(requireContext(), stop.toString(), Toast.LENGTH_LONG).show()
    }
}