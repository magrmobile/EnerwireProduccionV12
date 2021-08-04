package gcubeit.com.enerwireproduccionv12.data.network.response.code


import gcubeit.com.enerwireproduccionv12.data.database.entity.DbCode

data class Code(
    val code: Int,
    val description: String,
    val id: Int,
    val type: String
)

fun List<Code>.asDatabaseModel(): List<DbCode> {
    return map {
        DbCode (
            id = it.id,
            code = it.code,
            description = it.description,
            type = it.type
        )
    }
}