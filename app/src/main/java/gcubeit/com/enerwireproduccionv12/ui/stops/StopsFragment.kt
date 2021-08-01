package gcubeit.com.enerwireproduccionv12.ui.stops

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gcubeit.com.enerwireproduccionv12.R

class StopsFragment : Fragment() {

//    companion object {
//        fun newInstance() = StopsFragment()
//    }

    private lateinit var viewModel: StopsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stops_fragment, container, false)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(StopsViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(StopsViewModel::class.java)
    }
}