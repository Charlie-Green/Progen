package by.vadim_churun.individual.progen.model.entity

import java.io.File


/** A file.
  * As well as create files, the program may generate its contents using a template,
  * so a template and its parameters may be defined in a [ProjectFile]. **/
class ProjectFile private constructor(
    val file: File,
    val templateName: String?,
    val templateArgs: Map<String, String>
): ProjectNode() {

    override fun create(): CreateStatus {
        if(file.exists()) {
            return CreateStatus.EXISTS
        }

        val success = file.createNewFile()
        return if(success) CreateStatus.SUCCESS else CreateStatus.FAILED
    }


    class Builder {
        private var fileName = ""
        private var templateName: String? = null
        private val templateArgs = hashMapOf<String, String>()


        fun setName(value: String) {
            EntityUtil.validateFileName(value)
            fileName = value
        }

        fun defineTemplate(name: String) {
            templateName = name
        }

        fun addTemplateArgument(key: String, value: String) {
            templateName ?: throw IllegalStateException("Template is not defined")
            templateArgs[key] = value
        }

        fun build(): ProjectFile {
            if(fileName.isEmpty()) {
                throw IllegalStateException("Name was not set")
            }

            return ProjectFile(
                File(fileName),
                templateName,
                templateArgs
            )
        }
    }
}