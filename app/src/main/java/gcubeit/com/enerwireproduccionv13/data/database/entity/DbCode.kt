package gcubeit.com.enerwireproduccionv13.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import gcubeit.com.enerwireproduccionv13.data.network.response.code.Code

@Entity(tableName = "code_table")
data class DbCode(
    @PrimaryKey(autoGenerate = false)
    val code: Int,
    val description: String,
    val id: Int,
    val type: String
)

fun List<DbCode>.asDomainModel(): List<Code> {
    return map {
        Code (
            id = it.id,
            code = it.code,
            description = it.description,
            type = it.type
        )
    }
}