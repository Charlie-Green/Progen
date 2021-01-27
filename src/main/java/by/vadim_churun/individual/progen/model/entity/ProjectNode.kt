package by.vadim_churun.individual.progen.model.entity

import java.io.File


/** An element of project's file hierarchy. **/
abstract class ProjectNode(val file: File) {

    /** Return type for [create] method. **/
    enum class CreateStatus {
        EXISTS,
        FAILED,
        SUCCESS
    }


    /** Creates the [ProjectNode] itself.
      * (Children nor content are not created). **/
    abstract fun create(): CreateStatus
}