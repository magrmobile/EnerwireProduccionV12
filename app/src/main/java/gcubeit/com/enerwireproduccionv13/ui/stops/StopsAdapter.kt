package gcubeit.com.enerwireproduccionv13.ui.stops

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import gcubeit.com.enerwireproduccionv13.R
import gcubeit.com.enerwireproduccionv13.data.database.DbStopDao
import gcubeit.com.enerwireproduccionv13.data.database.entity.DbStop
import gcubeit.com.enerwireproduccionv13.data.database.entity.relations.StopsDetails
import gcubeit.com.enerwireproduccionv13.data.network.datasource.stop.StopNetworkDatasourceImpl
import gcubeit.com.enerwireproduccionv13.databinding.StopRowBinding
import gcubeit.com.enerwireproduccionv13.util.visible
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext


@DelicateCoroutinesApi
@Suppress("DEPRECATION")
class StopsAdapter(
    private val context: Context,
    private val stopsViewModel: StopsViewModel,
    private val processId: Int? = null
): RecyclerView.Adapter<StopsAdapter.StopViewHolder>(), CoroutineScope, KodeinAware {
    override val kodein by closestKodein(context)

    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val stopNetworkDatasource: StopNetworkDatasourceImpl by instance()
    private val dbStopDao: DbStopDao by instance()
    //private val userPreferences: UserPreferences by instance()

    private var stopsList = emptyList<StopsDetails>()

    init {
        refreshStops()
    }

    class StopViewHolder(val binding: StopRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopViewHolder {
        val binding = StopRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = StopViewHolder(binding)

        binding.btnEdit.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != NO_POSITION } ?: return@setOnClickListener
            val stop = stopsList[position]
            val productName = if(stop.dbProduct?.productName != null) {
                stop.dbProduct.productName
            } else {
                ""
            }

            showEditForm(
                holder = holder,
                stop = stop.dbStop!!,
                machineId = stop.dbStop.machineId,
                operatorId = stop.dbStop.operatorId,
                processId = stop.dbMachine.processId,
                title = stop.dbMachine.machineName,
                productName = productName
            )
            //Toast.makeText(context, stop.toString(), Toast.LENGTH_LONG).show()
        }

        binding.btnDelete.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != NO_POSITION } ?: return@setOnClickListener
            showDeleteDialog(
                holder = holder,
                stopId = stopsList[position].dbStop!!.id.toInt(),
                stopRemoteId = stopsList[position].dbStop!!.idRemote.toInt()
            )
        }
        return holder
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteStop(stopId: Int, stopRemoteId: Int? = null) {
        launch {
            dbStopDao.deleteStop(stopId)
            if (stopRemoteId != null) {
                stopNetworkDatasource.deleteStop(stopRemoteId)
            }
        }
        notifyDataSetChanged()
    }

    private fun refreshStops() {
        launch {
            stopsViewModel.stopsDataAll.await()
            val stops = stopsViewModel.stopsData.await()
            //Toast.makeText(context, stops.value.toString(), Toast.LENGTH_SHORT).show()
            setData(stops.value)
        }
    }

    private fun showDeleteDialog(holder: StopViewHolder, stopId: Int, stopRemoteId: Int? = null) {
        val dialogBuilder = AlertDialog.Builder(holder.binding.root.context)
        dialogBuilder.setTitle("Eliminar")
        dialogBuilder.setMessage("Desea Eliminar?")
        dialogBuilder.setPositiveButton("Eliminar") { _, _ -> deleteStop(stopId, stopRemoteId) }
        dialogBuilder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: StopViewHolder, position: Int) {
        with(holder) {
            with(stopsList[position]) {
                if(this.dbStop?.sync_status == 0) {
                    binding.ivSync.setImageResource(R.drawable.ic_check_circle)
                }

                val sdf: DateFormat = SimpleDateFormat("y-MM-dd HH:mm:ss") // or "hh:mm" for 12 hour format

                val datetimeStart = this.dbStop!!.stopDatetimeStart
                val dateStart: List<String> = datetimeStart.split(" ")

                val datetimeEnd = this.dbStop.stopDatetimeEnd
                val timeEnd: Date? = sdf.parse(datetimeEnd)
                val dateEnd: List<String> = datetimeEnd.split(" ")

                val dateParts: List<String> = dateEnd[0].split("-")

                binding.tvGridDate.text = "${dateParts[2]}/${dateParts[1]}/${dateParts[0]}"

                if(timeEnd!!.hours in 7..17) {
                    binding.tvGridSchedule.text = "D"
                } else {
                    binding.tvGridSchedule.text = "N"
                }

                binding.tvGridOperator.text = this.dbOperator.name
                binding.tvGridStartStop.text = dateStart[1]
                binding.tvGridEndStop.text = dateEnd[1]

                if(this.dbStop.productId != null) {
                    binding.tvGridProduct.text = this.dbProduct?.productName
                } else {
                    binding.tvGridProduct.text = ""
                }

                binding.tvGridCode.text = this.dbCode?.code.toString()

                if(this.dbMachine.processId == 1 || this.dbMachine.processId == 3) {
                    binding.tvGridPacking.visible(false)
                    binding.tvGridQuantity.visible(false)
                } else {
                    if(this.dbStop.conversionId != null) {
                        binding.tvGridPacking.text = this.dbConversion?.description
                    } else {
                        binding.tvGridPacking.text = ""
                    }

                    if(this.dbStop.quantity != null) {
                        binding.tvGridQuantity.text = this.dbStop.quantity.toString()
                    } else {
                        binding.tvGridQuantity.text = ""
                    }
                }

                if(this.dbStop.meters != null) {
                    binding.tvGridMeters.text = "%,.2f".format(this.dbStop.meters.toFloat())
                } else {
                    binding.tvGridMeters.text = ""
                }

                binding.tvGridComment.text = this.dbStop.comment
            }
        }
    }

    override fun getItemCount() = stopsList.size

    fun setData(stops: List<StopsDetails>?) {
        if (stops != null) {
            this.stopsList = stops
        }
        notifyDataSetChanged()
    }

    private fun showEditForm(
        holder: StopViewHolder,
        stop: DbStop,
        machineId: Int,
        operatorId: Int,
        processId: Int,
        title: String,
        productName: String
    ) {
        val action = StopsFragmentDirections.actionStopsFragmentToEditFragment(stop, machineId, operatorId, processId, title, productName/*, stop*/)
        holder.itemView.findNavController().navigate(action)
        //Toast.makeText(context, stop.toString(), Toast.LENGTH_LONG).show()
    }
}