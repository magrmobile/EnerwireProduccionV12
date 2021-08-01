package gcubeit.com.enerwireproduccionv12.ui.confirm

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gcubeit.com.enerwireproduccionv12.R

class ConfirmFragment : Fragment() {

//    companion object {
//        fun newInstance() = ConfirmFragment()
//    }

    private lateinit var viewModel: ConfirmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.confirm_fragment, container, false)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(ConfirmViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(ConfirmViewModel::class.java)
    }
}