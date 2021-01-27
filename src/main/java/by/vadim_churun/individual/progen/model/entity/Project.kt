package by.vadim_churun.individual.progen.model.entity

import java.io.File
import java.util.LinkedList


/** It's like [ProjectFolder], but in future versions
  * some other common attributes except the project create path may be defined here.
  * The root element must be [Project]. **/
class Project private constructor(
    file: File,
    val children: List<ProjectNode>
): ProjectNode(file) {

    override fun create(): CreateStatus {
        if(file.exists()) {
            return CreateStatus.EXISTS
        }

        val success = file.mkdirs()
        return if(success) CreateStatus.SUCCESS else CreateStatus.FAILED
    }


    class Builder(private val parent: File?) {
        private var path: String? = null
        private val children = LinkedList<ProjectNode>()

        fun setPath(value: String) {
            path = value
        }

        fun addChild(child: ProjectNode) {
            children.add(child)
        }

        fun build(): Project {
            if(path == null) {
                throw IllegalStateException(
                    "Project name must be specified." +
                    (if(parent == null) "" else " Parent: ${parent.absolutePath}")
                )
            }

            return Project(
                if(parent == null) File(path) else File(parent, path),
                children
            )
        }
    }
}