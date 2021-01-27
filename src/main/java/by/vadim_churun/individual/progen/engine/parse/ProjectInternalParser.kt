package by.vadim_churun.individual.progen.engine.parse

import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_PROJECT_PATH
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FILE
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FOLDER
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_PROJECT
import by.vadim_churun.individual.progen.model.entity.Project
import by.vadim_churun.individual.progen.model.entity.ProjectNode
import java.io.File
import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.StartElement


internal class ProjectInternalParser(
    private val reader: XMLEventReader ) {

    private val folderParser = FolderInternalParser(reader)
    private val fileParser   = FileInternalParser(reader)


    fun parse(parent: File?): Project {
        val builder = Project.Builder(parent)
        var path: String? = null

        while(reader.hasNext()) {
            val event = reader.nextEvent()

            when {
                event.isStartElement -> {
                    if(path == null) {
                        val drawbackLocation =
                            if(parent == null) "At root project."
                            else "Under ${parent.absolutePath}"
                        throw IllegalArgumentException(
                            "Project path must be specified. - $drawbackLocation")
                    }

                    val child = parseChild(
                        if(parent == null) File(path) else File(parent, path),
                        event.asStartElement()
                    )
                    builder.addChild(child)
                }

                event.isCharacters -> {
                    throw IllegalArgumentException(
                        "Characters directly inside <$ELEMENT_PROJECT> are not expected" )
                }

                event.isAttribute -> {
                    val attrEvent = event as Attribute
                    val key = attrEvent.name.localPart
                    when(key) {
                        ATTR_PROJECT_PATH -> {
                            builder.setPath(attrEvent.value)
                            path = attrEvent.value
                        }

                        else ->  {
                            throw IllegalArgumentException(
                                "Unknown parameter $key of element <$ELEMENT_PROJECT>" )
                        }
                    }
                }

                event.isEndElement -> {
                    break
                }
            }
        }

        return builder.build()
    }


    private fun parseChild(
        thisNode: File,
        event: StartElement
    ): ProjectNode {

        val name = event.name.localPart
        return when(name) {
            ELEMENT_FOLDER -> folderParser.parse(thisNode)
            ELEMENT_FILE   -> fileParser.parse(thisNode)
            else -> throw IllegalArgumentException(
                "Unexpected element <$name> inside <$ELEMENT_PROJECT>" )
        }
    }
}