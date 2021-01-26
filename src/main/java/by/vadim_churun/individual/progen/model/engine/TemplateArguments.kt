package by.vadim_churun.individual.progen.model.engine


/** A set of key-value pairs wrapped to parse strings into something more useful. **/
open class TemplateArguments {
    protected val map = hashMapOf<String, String>()


    fun getString(
        key: String,
        default: String? = null
    ): String {
        return map[key]
            ?: default
            ?: throw IllegalStateException("Missing parameter '$key' of type 'string'")
    }

    fun getBoolean(
        key: String,
        default: Boolean? = null
    ): Boolean {

        val str = map[key]
            ?.toLowerCase()
            ?.trim()

        return when(str) {
            "true"  -> true
            "yes"   -> true
            "false" -> false
            "no"    -> false

            null    -> default
                ?: throw IllegalArgumentException("Missing parameter '$key' of type 'boolean'")

            else -> throw IllegalArgumentException(
                "Cannot interpret value '${map[key]} of parameter '$key' as a boolean" )
        }
    }

    fun getLong(
        key: String,
        default: Long? = null
    ): Long {

        val str = map[key]?.trim()
        if(str == null) {
            return default
                ?: throw IllegalStateException("Missing parameter '$key' of type 'integer'")
        }

        try {
            return str.toLong()
        } catch(exc: NumberFormatException) {
            throw IllegalArgumentException(
                "Cannot represent value '${map[key]}' of parameter '$key' as an integer",
                exc
            )
        }
    }

    fun getInt(
        key: String,
        default: Int? = null
    ): Int {

        val long = getLong(key, default?.toLong())
        if(long > Int.MAX_VALUE || long < Int.MIN_VALUE) {
            throw IllegalArgumentException(
                "Value $long of parameter '$key' exceeds the bounds of a 4-byte integer" )
        }

        return long.toInt()
    }
}