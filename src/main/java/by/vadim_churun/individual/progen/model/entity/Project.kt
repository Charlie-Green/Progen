package by.vadim_churun.individual.progen.model.entity

import java.io.File
import java.util.LinkedList


/** It's like [ProjectFolder], but in future versions
  * some other common attributes except the project create path may be defined here.
  * The root element must be [Project]. **/
class Project private constructor(
    val file: File,
    val children: List<ProjectNode>
): ProjectNode() {

    override fun create(): CreateStatus {
        if(file.exists()) {
            return CreateStatus.EXISTS
        }

        val success = file.mkdirs()
        return if(success) CreateStatus.SUCCESS else CreateStatus.FAILED
    }


    class Builder {
        private var path = "./"
        private val children = LinkedList<ProjectNode>()

        fun setPath(value: String) {
            path = value
        }

        fun addChild(child: ProjectNode) {
            children.add(child)
        }

        fun build(): Project {
            return Project(
                File(path),
                children
            )
        }
    }
}