package by.vadim_churun.individual.progen.model.engine


/** A set of key-value pairs wrapped to parse strings into something more useful. **/
open class Arguments {
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
            "on"    -> true
            "false" -> false
            "no"    -> false
            "off"   -> false

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

        val str = map[key]?.replace(numberNoiseRegex, "")
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


    fun getDouble(
        key: String,
        default: Double? = null
    ): Double {

        val str = map[key]
            ?.replace(numberNoiseRegex, "")
            ?.replace(",", ".")

        if(str == null) {
            return default
                ?: throw IllegalStateException("Missing parameter '$key' of type 'float'")
        }

        try {
            return str.toDouble()
        } catch(exc: NumberFormatException) {
            throw IllegalArgumentException(
                "Cannot interpret value '${map[key]}' " +
                "of parameter '$key' " +
                "as a float",
                exc
            )
        }
    }

    fun getFloat(
        key: String,
        default: Float? = null
    ): Float {

        val double = getDouble(key, default?.toDouble())
        if(double < -Float.MAX_VALUE || double > Float.MAX_VALUE) {
            throw IllegalArgumentException(
                "Value $double of parameter '$key' exceeds the range of a 4-byte float" )
        }

        return double.toFloat()
    }


    fun <E: Enum<E>> getEnumConstant(
        key: String,
        enumClass: Class<E>,
        default: E? = null
    ): E {

        val value = map[key]
        if(value == null) {
            return default
                ?: throw IllegalStateException("Missing enumerated type parameter '$key'")
        }

        var processedValue = value
            .toLowerCase()
            .replace(enumDelimeterRegex, "_")
            .replace(underscoreSequenceRegex, "_")

        val index1 = processedValue
            .indexOfFirst { it != '_' }
            .let { if(it < 0) 0 else it }
        val index2 = processedValue
            .indexOfLast { it != '_' }
            .let { if(it < 0) processedValue.lastIndex else it }
            .plus(1)
        processedValue = processedValue.substring(index1, index2)

        val consts = enumClass.enumConstants
        for(c in consts) {
            val actual = c.name.toLowerCase()
            if(actual == processedValue) {
                return c
            }
        }

        val constsStr = listEnumConstants(consts)
        throw IllegalArgumentException(
            "Cannot represent value '${map[key]}' " +
            "of parameter '$key' " +
            "as either of $constsStr"
        )
    }


    private fun listEnumConstants(
        consts: Array< out Enum<*> >
    ): String {

        val sb = StringBuilder("[")
        for(c in consts) {
            if(sb.length > 1) {
                sb.append(", ")
            }
            sb.append(c.name)
        }
        sb.append("]")

        return sb.toString()
    }


    companion object {
        private val numberNoiseRegex = Regex("[\\s_]")
        private val enumDelimeterRegex = Regex("[\\s-]")
        private val underscoreSequenceRegex = Regex("_+")
    }
}