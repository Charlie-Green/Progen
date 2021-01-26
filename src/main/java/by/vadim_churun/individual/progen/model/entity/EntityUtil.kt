package by.vadim_churun.individual.progen.model.entity


internal object EntityUtil {
    private val forbiddenCharsRegex = Regex("[/\\\\?%:]")


    fun validateFileName(name: String) {
        if(name.isEmpty()) {
            throw IllegalArgumentException("Folder name cannot be empty")
        }

        if(name.contains(forbiddenCharsRegex)) {
            throw IllegalArgumentException(
                "Forbidden characters in folder name $name" )
        }
    }
}