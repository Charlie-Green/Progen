package by.vadim_churun.individual.progen.model.engine


class MutableArguments: Arguments() {

    fun add(key: String, value: String) {
        if(map.containsKey(key)) {
            throw IllegalStateException("'$key' argument duplicated")
        }
        map[key] = value
    }
}