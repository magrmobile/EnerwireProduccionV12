package gcubeit.com.enerwireproduccionv12.data.database.views

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StopConfirm (
    val codeDescription: String,
    val codeType: String,
    val operatorName: String,
    val productName: String? = null,
    val colorName: String? = null,
    val conversionDescription: String? = null,
    val quantity: String? = null,
    val meters: String? = null,
    val comment: String? = null,
    val startStopDate: String,
    val startStopTime: String,
    val endStopDate: String
): Parcelable