package gcubeit.com.enerwireproduccionv12.ui.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gcubeit.com.enerwireproduccionv12.data.database.views.StopsDashboard
import gcubeit.com.enerwireproduccionv12.databinding.CardInfoRowBinding
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
                binding.machineName.text = this.machineName
                var processName = ""

                when(this.processId) {
                    1 -> { processName = "Cableado" }
                    2 -> { processName = "Extrusion" }
                    3 -> { processName = "Fraccionado" }
                    4 -> { processName = "Trefilado"}
                }

                binding.processName.text = processName

                val startDateTime = this.lastStopDateTimeEnd.split(" ")
                val date = LocalDate.parse(startDateTime[0])
                val formattedDate = date.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy"))
                val time = LocalTime.parse(startDateTime[1])
                val formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss a"))

                binding.lastStopDateTime.text = "$formattedDate $formattedTime"

                binding.quantityStops.text = "${this.quantityStops} Paros"
            }
        }
    }

    override fun getItemCount() = dashboardList.size

    fun setData(data: List<StopsDashboard>) {
        this.dashboardList = data
        notifyDataSetChanged()
    }
}