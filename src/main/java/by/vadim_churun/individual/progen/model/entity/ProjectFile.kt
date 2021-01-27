package by.vadim_churun.individual.progen.model.entity

import by.vadim_churun.individual.progen.model.engine.MutableArguments
import by.vadim_churun.individual.progen.model.engine.Arguments
import java.io.File


/** A file.
  * As well as create files, the program may generate its contents using a template,
  * so a template and its parameters may be defined in a [ProjectFile]. **/
class ProjectFile private constructor(
    file: File,
    val templateName: String?,
    val templateArgs: Arguments
): ProjectNode(file) {

    override fun create(): CreateStatus {
        if(file.exists()) {
            return CreateStatus.EXISTS
        }

        val success = file.createNewFile()
        return if(success) CreateStatus.SUCCESS else CreateStatus.FAILED
    }


    class Builder(private val parent: File) {
        private var fileName = ""
        private var templateName: String? = null
        private val templateArgs = MutableArguments()


        fun setName(value: String) {
            EntityUtil.validateFileName(value)
            fileName = value
        }

        fun defineTemplate(name: String) {
            templateName = name
        }

        fun addTemplateArgument(key: String, value: String) {
            templateName ?: throw IllegalStateException("Template is not defined")
            templateArgs.add(key, value)
        }

        fun build(): ProjectFile {
            if(fileName.isEmpty()) {
                throw IllegalStateException("Name was not set")
            }

            return ProjectFile(
                File(parent, fileName),
                templateName,
                templateArgs
            )
        }
    }
}