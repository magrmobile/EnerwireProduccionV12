package gcubeit.com.enerwireproduccionv13.data.database.views

data class StopsDashboard(
    val machineId: Int,
    val machineName: String,
    val processId: Int,
    val lastStopDateTimeEnd: String,
    val quantityStops: Int
)
