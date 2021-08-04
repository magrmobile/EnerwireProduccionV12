package gcubeit.com.enerwireproduccionv12.ui.stops

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import gcubeit.com.enerwireproduccionv12.databinding.StopsFragmentBinding

private const val ARG_MACHINE_ID = "machine_id"
private const val ARG_MACHINE_NAME = "machine_name"

class StopsFragment : Fragment() {

    private var param1: Int? = null
    private var param2: String? = null

    companion object {
        fun newInstance(param1: Int, param2: String?) =
            StopsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_MACHINE_ID, param1)
                    putString(ARG_MACHINE_NAME, param2)
                }
            }
    }

    private lateinit var viewModel: StopsViewModel
    private lateinit var binding: StopsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_MACHINE_ID)
            param2 = it.getString(ARG_MACHINE_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StopsFragmentBinding.inflate(inflater, container, false)

        requireActivity().title = param2

        binding.tvTitle.text = param2

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StopsViewModel::class.java)
        // TODO: Use the ViewModel
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        viewModel = ViewModelProvider(this).get(StopsViewModel::class.java)
//    }
}