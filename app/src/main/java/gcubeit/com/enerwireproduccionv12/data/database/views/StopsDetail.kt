package gcubeit.com.enerwireproduccionv12.data.database.views

import androidx.room.DatabaseView

@DatabaseView(
    value = "SELECT s.id, s.createdAt AS date, o.username AS operator, s.machineId FROM stop_table AS s JOIN operator_table o ON o.id = s.operatorId",
    viewName = "v_stops_detail"
)
data class StopsDetail(
    val id: Int,
    val date: String,
    val operator: String,
    val machineId: Int
)