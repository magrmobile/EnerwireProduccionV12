package gcubeit.com.enerwireproduccionv12.data.database.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import gcubeit.com.enerwireproduccionv12.data.database.entity.*

data class StopsDetails (
    @Embedded val dbStop: DbStop? = null,
    @Relation(parentColumn = "operatorId", entityColumn = "id")
    val dbOperator: DbOperator,
    @Relation(parentColumn = "productId", entityColumn = "id")
    val dbProduct: DbProduct? = null,
    @Relation(parentColumn = "conversionId", entityColumn = "id")
    val dbConversion: DbConversion? = null,
    @Relation(parentColumn = "machineId", entityColumn = "id")
    val dbMachine: DbMachine,
    @Relation(parentColumn = "colorId", entityColumn = "id")
    val dbColor: DbColor? = null,
    @Relation(parentColumn = "codeId", entityColumn = "id")
    val dbCode: DbCode? = null
)