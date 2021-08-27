package gcubeit.com.enerwireproduccionv12.ui.create

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import gcubeit.com.enerwireproduccionv12.R
import gcubeit.com.enerwireproduccionv12.data.database.UserPreferences
import gcubeit.com.enerwireproduccionv12.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv12.data.database.entity.asDomainModel
import gcubeit.com.enerwireproduccionv12.data.database.views.StopConfirm
import gcubeit.com.enerwireproduccionv12.data.network.response.code.Code
import gcubeit.com.enerwireproduccionv12.data.network.response.color.Color
import gcubeit.com.enerwireproduccionv12.data.network.response.conversion.Conversion
import gcubeit.com.enerwireproduccionv12.data.network.response.operator.Operator
import gcubeit.com.enerwireproduccionv12.data.network.response.product.Product
import gcubeit.com.enerwireproduccionv12.databinding.CreateFragmentBinding
import gcubeit.com.enerwireproduccionv12.util.ColorAdapter
import gcubeit.com.enerwireproduccionv12.util.SearchableSpinner.NO_ITEM_SELECTED
import gcubeit.com.enerwireproduccionv12.util.visible
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

@DelicateCoroutinesApi
class CreateFragment : Fragment(), CoroutineScope, KodeinAware {
    override val kodein by closestKodein()

    private lateinit var job: Job

    private val application: Application by instance()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val userPreferences: UserPreferences by instance()

    private val args by navArgs<CreateFragmentArgs>()

    private lateinit var binding: CreateFragmentBinding

    private lateinit var createViewModelFactory: CreateViewModelFactory

    private lateinit var viewModel: CreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = SupervisorJob()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreateFragmentBinding.inflate(inflater, container, false)

        //userPreferences = UserPreferences(application)

        // ViewModel
        createViewModelFactory = CreateViewModelFactory(application, args.machineId)
        viewModel = ViewModelProvider(this, createViewModelFactory).get(CreateViewModel::class.java)

        bindUI()

        //Toast.makeText(requireContext(), "OperatorId: ${args.operatorId.toString()}", Toast.LENGTH_LONG).show()

        return binding.root
    }

    private fun loadCodes() = lifecycleScope.launch {
        val codes = viewModel.codes.await()
        codes.observe(viewLifecycleOwner, { data ->
            //Toast.makeText(requireContext(), data.size.toString(), Toast.LENGTH_LONG).show()
            val codesArray: ArrayList<Code> = arrayListOf()
            codesArray.add(Code(-1, -1, getString(R.string.hint_select_code), ""))
            for(arrCode in data.asDomainModel()) {
                codesArray.add(arrCode)
            }

            val codesAdapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_base,
                codesArray
            )

            binding.spinnerCode.adapter = codesAdapter
        })
    }

    private fun listenCodeChanges() {
        binding.spinnerCode.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val code = adapter?.getItemAtPosition(position) as Code
                binding.etStopType.setText(code.type)// = code.type.toString()
                when (code.code) {
                    0 -> {
                        binding.tvProduct.visible(true)
                        binding.spinnerProduct.visible(true)
                        /*if(productId!! > 0) {
                            s1.spinnerProduct.setSelectionP(productId, productName)
                        }*/
                        binding.tvColor.visible(true)
                        binding.spinnerColor.visible(true)
                        //spinnerColor.setSelection(-1)
                        when (args.processId) {
                            2, 4 -> {
                                binding.tvConversion.visible(true)
                                binding.spinnerConversion.visible(true)
                                binding.tvQuantity.visible(true)
                                binding.etQuantity.visible(true)
                                binding.etMeters.keyListener = null
                            }
                        }
                        binding.tvMeters.visible(true)
                        binding.etMeters.visible(true)
                    }
                    else -> {
                        binding.tvProduct.visible(false)
                        binding.spinnerProduct.visible(false)
                        //s1.spinnerProduct.setSelection(NO_ITEM_SELECTED)
                        binding.tvColor.visible(false)
                        binding.spinnerColor.visible(false)
                        binding.spinnerColor.setSelection(NO_ITEM_SELECTED)
                        binding.tvConversion.visible(false)
                        binding.spinnerConversion.visible(false)
                        binding.spinnerConversion.setSelection(NO_ITEM_SELECTED)
                        binding.tvQuantity.visible(false)
                        binding.etQuantity.visible(false)
                        binding.etQuantity.text = null
                        binding.tvMeters.visible(false)
                        binding.etMeters.visible(false)
                        binding.etMeters.text = null
                    }
                }
            }
        }
    }

    private fun loadOperators() = lifecycleScope.launch {
        val operators = viewModel.operatorsByProcess.await()

        operators.observe(viewLifecycleOwner, { data ->
            val operatorsArray: ArrayList<Operator> = arrayListOf()
            operatorsArray.add(Operator(-1, getString(R.string.hint_select_operator), "", "", "", 0, 0))

            for(arrOperator in data.asDomainModel()) {
                operatorsArray.add(arrOperator)
            }

            val operatorsAdapter = context?.let {
                ArrayAdapter(
                    it,
                    R.layout.spinner_item_base,
                    operatorsArray
                )
            }

            binding.spinnerOperator.adapter = operatorsAdapter

            if(args.operatorId > 0) {
                var index = 0
                for(i in 0 until binding.spinnerOperator.count) {
                    val operator = binding.spinnerOperator.getItemAtPosition(i) as Operator
                    if(operator.id == args.operatorId) {
                        index = i
                        break
                    }
                }
                binding.spinnerOperator.setSelection(index)
            }
        })
    }

    private fun loadProducts() = lifecycleScope.launch {
        val products = viewModel.productsByProcess.await()

        products.observe(viewLifecycleOwner, { data ->
            val productsArray: ArrayList<Product> = arrayListOf()
            for(arrProduct in data.asDomainModel()) {
                productsArray.add(arrProduct)
            }

            val productsAdapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_base,
                productsArray
            )

            binding.spinnerProduct.adapter = productsAdapter
        })
    }

    private fun loadColors() = lifecycleScope.launch {
        val colors = viewModel.colors.await()

        colors.observe(viewLifecycleOwner, { data ->
            val colorsArray: ArrayList<Color> = arrayListOf()
            colorsArray.add(Color(-1, getString(R.string.hint_select_color), ""))
            for(arrColor in data.asDomainModel()) {
                colorsArray.add(arrColor)
            }

            val colorsAdapter = ColorAdapter(
                requireContext(),
                colorsArray
            )

            binding.spinnerColor.adapter = colorsAdapter
        })
    }

    private fun loadConversions() = lifecycleScope.launch {
        val conversions = viewModel.conversions.await()

        conversions.observe(viewLifecycleOwner, { data ->
            val conversionsArray: ArrayList<Conversion> = arrayListOf()
            conversionsArray.add(Conversion(-1, getString(R.string.hint_select_conversion), 1f, ""))
            for(arrConversion in data.asDomainModel()) {
                conversionsArray.add(arrConversion)
            }

            val conversionsAdapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_base,
                conversionsArray
            )

            binding.spinnerConversion.adapter = conversionsAdapter
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getLastStopDateTime() = lifecycleScope.launch {
        var lastLogin = ""

        val lastDateTime = viewModel.lastStopDateTime

        userPreferences.lastStopDateTimeStart.asLiveData().observe(viewLifecycleOwner) {
            lastLogin = it
        }

        lastDateTime.observe(viewLifecycleOwner, { data ->
            if(data != null) {
                val startDateTime = data.split(" ")
                val date = LocalDate.parse(startDateTime[0])
                val formattedDate = date.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy "))
                val time = LocalTime.parse(startDateTime[1])
                val formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss a"))
                binding.tvConfirmStartDateStop.text = formattedDate
                binding.tvConfirmStartTimeStop.text = formattedTime
                //Toast.makeText(requireContext(), formattedDate + " " + formattedTime, Toast.LENGTH_LONG).show()
            } else {
                val startDateTime = lastLogin.split(" ")
                val date = LocalDate.parse(startDateTime[0])
                val formattedDate = date.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy "))
                val time = LocalTime.parse(startDateTime[1])
                val formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss a"))
                binding.tvConfirmStartDateStop.text = formattedDate
                binding.tvConfirmStartTimeStop.text = formattedTime
                //Toast.makeText(requireContext(), lastLogin, Toast.LENGTH_LONG).show()
            }
        })

        binding.tvConfirmEndDateStop.text = SimpleDateFormat("dd MMMM, y ").format(Date())
    }

    private fun bindUI() {
        loadCodes()
        listenCodeChanges()
        getLastStopDateTime()
        loadOperators()
        loadProducts()
        loadColors()
        loadConversions()

        binding.spinnerOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val operator = adapter?.getItemAtPosition(position) as Operator
                this@CreateFragment.arguments?.putInt("operatorId", operator.id)
                //Toast.makeText(requireContext(), args.operatorId.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancel.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.dialog_create_stop_exit_title))
            builder.setMessage(getString(R.string.dialog_create_stop_exit_message))
            builder.setPositiveButton(getString(R.string.dialog_create_stop_exit_positive_btn)) { _, _ ->
                val action = CreateFragmentDirections.toStopsFragment(args.machineId, args.processId, args.title)
                it.findNavController().navigate(action)
            }
            builder.setNegativeButton(getString(R.string.dialog_create_stop_exit_negative_btn)) { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        binding.btnNext.setOnClickListener {
            val operator = binding.spinnerOperator.selectedItem as Operator
            if(operator.id == -1) {
                Toast.makeText(context, R.string.require_operator_text, Toast.LENGTH_SHORT).show()
            }else {

                val code = binding.spinnerCode.selectedItem as Code

                when (code.code) {
                    -1 -> {
                        Toast.makeText(context, R.string.require_code_text, Toast.LENGTH_SHORT).show()
                    }
                    0 -> {
                        if (binding.spinnerProduct.selectedItem == null) {
                            Toast.makeText(context, R.string.require_product_text, Toast.LENGTH_SHORT).show()
                        } else {
                            //val product = binding.spinnerProduct.selectedItem as Product
                            val color = binding.spinnerColor.selectedItem as Color
                            if (color.id == -1) {
                                Toast.makeText(context, R.string.require_color_text, Toast.LENGTH_SHORT).show()
                            } else {
                                val dbStop = performStoreDbStop()
                                val confirmStop = performStoreStopConfirm()
                                val action = CreateFragmentDirections.actionCreateFragmentToConfirmFragment(confirmStop, dbStop, args.machineId, args.processId, args.title, /*product.productName,*/1)
                                it.findNavController().navigate(action)
                            }
                        }
                    }
                    6, 7, 8, 9, 10, 11 -> {
                        if (binding.etComments.text!!.isEmpty()) {
                            Toast.makeText(context, R.string.require_comment_text, Toast.LENGTH_SHORT).show()
                        } else {
                            val dbStop = performStoreDbStop()
                            val confirmStop = performStoreStopConfirm()
                            val action = CreateFragmentDirections.actionCreateFragmentToConfirmFragment(confirmStop, dbStop, args.machineId, args.processId, args.title, /*"",*/ 1)
                            it.findNavController().navigate(action)
                        }
                    }
                    else -> {
                        val dbStop = performStoreDbStop()
                        val confirmStop = performStoreStopConfirm()
                        val action = CreateFragmentDirections.actionCreateFragmentToConfirmFragment(confirmStop, dbStop, args.machineId, args.processId, args.title, /*"",*/ 1)
                        it.findNavController().navigate(action)
                    }
                }
            }
        }

        binding.spinnerConversion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val conversion = adapter?.getItemAtPosition(position) as Conversion
                val quantity: Int

                if(binding.etQuantity.text.isNullOrEmpty()) {
                    quantity = 0
                    binding.etQuantity.setText("")
                } else {
                    quantity = binding.etQuantity.text.toString().toInt()
                }

                val value = calculateMeters(quantity, conversion.factor)

                if(value > 0F) {
                    binding.etMeters.setText(String.format("%.2f", value))
                }
            }
        }

        binding.etQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    val conversion = binding.spinnerConversion.selectedItem as Conversion
                    val quantity: Int = if (s.isNullOrEmpty()) {
                        0
                    } else {
                        s.toString().toInt()
                    }
                    val value = calculateMeters(quantity, conversion.factor)

                    if (value > 0F) {
                        binding.etMeters.setText(String.format("%.2f", value))
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun performStoreDbStop(): DbStop {
        val codeId: Int
        val codeVal: Int
        val productId: Int?
        val colorId : Int?
        var conversionId : Int? = null
        var quantity : Int? = null
        val meters : Float?
        val comment : String?

        val operatorId: Int
        val operator = binding.spinnerOperator.selectedItem as Operator
        operatorId = operator.id

        val code = binding.spinnerCode.selectedItem as Code
        codeId = code.id
        codeVal = code.code

        if(codeVal == 0) {
            val product = binding.spinnerProduct.selectedItem as Product
            productId = product.id

            val color = binding.spinnerColor.selectedItem as Color
            colorId = color.id
            //colorId = rgColor.checkedRadioButtonId
        } else {
            productId = null
            colorId = null
        }

        when(args.processId){
            2, 4 -> {
                if(binding.spinnerConversion.selectedItem != null) {
                    val conversion = binding.spinnerConversion.selectedItem as Conversion
                    if (conversion.id > 0) {
                        conversionId = conversion.id
                    }
                }
                quantity = binding.etQuantity.text.toString().toIntOrNull() ?: 0
            }
            else -> {
                conversionId = null
                quantity = null
            }
        }

        meters = binding.etMeters.text.toString().toFloatOrNull()?: 0F

        comment = if(!binding.etComments.text.isNullOrEmpty()) {
            binding.etComments.text.toString().uppercase(Locale.ROOT)
        } else {
            null
        }

        return DbStop(
            id = 0,
            machineId = args.machineId,
            operatorId = operatorId,
            productId = productId,
            colorId = colorId,
            codeId = codeId,
            conversionId = conversionId,
            quantity = quantity,
            meters = meters,
            comment = comment,
            "",
            "",
            "",
            ""
        )
    }

    private fun performStoreStopConfirm(): StopConfirm {
        val codeDescription: String
        val codeType: String
        val operatorName: String

        var productName = ""
        var colorName = ""
        var conversionDescription = ""

        val code = binding.spinnerCode.selectedItem as Code
        codeDescription = if(code.id > 0) {
            code.toString()
        } else {
            ""
        }

        codeType = if(code.id > 0) {
            code.type
        } else {
            ""
        }

        val operator = binding.spinnerOperator.selectedItem as Operator
        operatorName = if(operator.id > 0) {
            operator.toString()
        } else {
            ""
        }

        if(binding.spinnerProduct.selectedItem != null) {
            val product = binding.spinnerProduct.selectedItem as Product
            productName = if(code.code == 0) {
                product.productName
            } else {
                ""
            }
        }

        if(binding.spinnerColor.selectedItem != null) {
            val color = binding.spinnerColor.selectedItem as Color
            colorName = if(color.id > 0) {
                color.name
            } else {
                ""
            }
        }

        if(binding.spinnerConversion.selectedItem != null) {
            val conversion = binding.spinnerConversion.selectedItem as Conversion
            conversionDescription = if(conversion.id > 0) {
                conversion.description
            } else {
                ""
            }
        }

        return StopConfirm(
            codeDescription,
            codeType,
            operatorName,
            productName,
            colorName,
            conversionDescription,
            binding.etQuantity.text.toString(),
            binding.etMeters.text.toString(),
            binding.etComments.text.toString().uppercase(Locale.ROOT),
            binding.tvConfirmStartDateStop.text.toString(),
            binding.tvConfirmStartTimeStop.text.toString(),
            binding.tvConfirmEndDateStop.text.toString()
        )
    }

    private fun calculateMeters(quantity: Int, factor: Float): Float {
        return  quantity * factor
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}