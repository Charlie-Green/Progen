package by.vadim_churun.individual.progen.model.entity

import java.io.File
import java.util.LinkedList


/** A folder. May contain child [ProjectNode]s. **/
class ProjectFolder private constructor(
    val file: File,
    val children: List<ProjectNode>
): ProjectNode() {

    override fun create(): CreateStatus {
        if(file.exists()) {
            return CreateStatus.EXISTS
        }

        val success = file.mkdir()
        return if(success) CreateStatus.SUCCESS else CreateStatus.FAILED
    }


    class Builder {
        private var name = ""
        private val children = LinkedList<ProjectNode>()


        fun setName(value: String) {
            EntityUtil.validateFileName(value)
            name = value
        }

        fun addChild(child: ProjectNode) {
            children.add(child)
        }

        fun build(): ProjectFolder {
            if(name.isEmpty()) {
                throw IllegalStateException("Name not set")
            }

            return ProjectFolder(
                File(name),
                children
            )
        }
    }
}