package gcubeit.com.enerwireproduccionv12.data.network.response.code


import gcubeit.com.enerwireproduccionv12.data.database.entity.DbCode

data class Code(
    val id: Int,
    val code: Int,
    val description: String,
    val type: String
) {
    override fun toString(): String {
        return if(this.id == -1) {
            description
        } else {
            "$code - $description"
        }
    }
}

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