package gcubeit.com.enerwireproduccionv13.ui.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import gcubeit.com.enerwireproduccionv13.data.database.views.StopsDashboard
import gcubeit.com.enerwireproduccionv13.databinding.CardInfoRowBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DashboardAdapter: RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {
    private var dashboardList = emptyList<StopsDashboard>()

    class DashboardViewHolder(val binding: CardInfoRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val binding = CardInfoRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashboardViewHolder(binding)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        with(holder) {
            with(dashboardList[position]) {
                binding.machineId.text = this.machineId.toString()
                binding.machineName.text = this.machineName
                var processName = ""

                when(this.processId) {
                    1 -> { processName = "Cableado" }
                    2 -> { processName = "Extrusion" }
                    3 -> { processName = "Trefilado" }
                    4 -> { processName = "Fraccionado"}
                }

                binding.processId.text = this.processId.toString()
                binding.processName.text = processName

                val startDateTime = this.lastStopDateTimeEnd.split(" ")
                val date = LocalDate.parse(startDateTime[0])
                val formattedDate = date.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy"))
                val time = LocalTime.parse(startDateTime[1])
                val formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss a"))

                binding.lastStopDateTime.text = "$formattedDate $formattedTime"

                binding.quantityStops.text = "${this.quantityStops} Paros"

                binding.cardInfo.setOnClickListener {
                    val machineId = binding.machineId.text.toString().toInt()
                    val processId = binding.processId.text.toString().toInt()
                    val action = DashboardFragmentDirections.toStopsFragment(machineId, 0, processId, this.machineName)
                    it.findNavController().navigate(action)
                }
            }
        }

    }

    override fun getItemCount() = dashboardList.size

    fun setData(data: List<StopsDashboard>) {
        this.dashboardList = data
        notifyDataSetChanged()
    }
}