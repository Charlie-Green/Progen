package by.vadim_churun.individual.progen.engine.parse

import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_NODE_NAME
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FILE
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FOLDER
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_PROJECT
import by.vadim_churun.individual.progen.model.entity.*
import java.io.File
import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.StartElement


internal class FolderInternalParser(
    private val reader: XMLEventReader ) {

    private val fileParser = FileInternalParser(reader)


    fun parse(parent: File): ProjectFolder {
        val builder = ProjectFolder.Builder(parent)
        var folderName: String? = null

        while(reader.hasNext()) {
            val event = reader.nextEvent()

            when {
                event.isStartElement -> {
                    if(folderName == null) {
                        throw IllegalArgumentException(
                            "Folder name must be specified. Parent: ${parent.absolutePath}" )
                    }

                    val child = parseChild(
                        File(parent, folderName),
                        event.asStartElement()
                    )
                    builder.addChild(child)
                }

                event.isAttribute -> {
                    val attrEvent = event as Attribute
                    val key = attrEvent.name.localPart

                    when(key) {
                        ATTR_NODE_NAME -> {
                            folderName = attrEvent.value
                            builder.setName(attrEvent.value)
                        }

                        else -> {
                            throw IllegalArgumentException(
                                "Unknown attribute $key of element <$ELEMENT_FOLDER>" )
                        }
                    }
                }

                event.isCharacters -> {
                    throw IllegalArgumentException(
                        "Did not expect characters inside <$ELEMENT_FOLDER>")
                }

                event.isEndElement -> {
                    break
                }
            }
        }

        return builder.build()
    }


    private fun parseChild(
        currentNode: File,
        event: StartElement
    ): ProjectNode {

        val name = event.name.localPart
        return when(name) {
            ELEMENT_FOLDER -> this.parse(currentNode)
            ELEMENT_FILE   -> fileParser.parse(currentNode)
            else -> throw IllegalArgumentException(
                "Unexpected element <$name> inside <$ELEMENT_PROJECT>" )
        }
    }
}