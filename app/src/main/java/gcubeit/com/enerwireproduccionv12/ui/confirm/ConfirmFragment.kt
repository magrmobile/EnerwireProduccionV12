package gcubeit.com.enerwireproduccionv12.ui.confirm

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import gcubeit.com.enerwireproduccionv12.NavAppDirections
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv12.databinding.ConfirmFragmentBinding
import gcubeit.com.enerwireproduccionv12.databinding.StopsFragmentBinding
import gcubeit.com.enerwireproduccionv12.ui.stops.StopsFragment
import gcubeit.com.enerwireproduccionv12.ui.stops.StopsViewModel
import gcubeit.com.enerwireproduccionv12.util.visible
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

    private val userPreferences: UserPreferences by instance()

    private val args by navArgs<ConfirmFragmentArgs>()

    private lateinit var binding: ConfirmFragmentBinding

    private lateinit var confirmViewModelFactory: ConfirmViewModelFactory

    private lateinit var viewModel: ConfirmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        binding.btnBack.setOnClickListener {
            /*if(args.action == 1) {
                val action = NavAppDirections.toCreateFragment(args.machineId, args.dbStop.operatorId, args.processId, args.title, args.dbStop)
            }

            if(args.action == 2) {
                val action = NavAppDirections.toEditFragment(args.dbStop, args.machineId, args.dbStop.operatorId, args.processId, args.title, args.productName, args.dbStop)
            }*/
            if(args.action == 1) {
                val action = ConfirmFragmentDirections.toCreateFragment(args.machineId, args.dbStop.operatorId, args.processId, args.title)
                it.findNavController().navigate(action)
            }

            //it.findNavController().popBackStack()
        }

        binding.btnConfirmStop.setOnClickListener {
            if(args.action == 1) {
                addStop(args.dbStop)
            }
            if(args.action == 2) {
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

    private fun addStop(stop: DbStop) {
        viewModel.addStop(stop)
        Toast.makeText(requireContext(), "Paro Insertado Satisfactoriamente", Toast.LENGTH_LONG).show()

        val action = ConfirmFragmentDirections.actionConfirmFragmentToStopsFragment(args.machineId, args.processId, args.title)
        findNavController().navigate(action)
    }

    private fun updateStop(stop: DbStop) {
        viewModel.updateStop(stop)
        Toast.makeText(requireContext(), "Paro Actualizado Satisfactoriamente", Toast.LENGTH_LONG).show()

        val action = ConfirmFragmentDirections.actionConfirmFragmentToStopsFragment(args.machineId, args.processId, args.title)
        findNavController().navigate(action)
    }
}