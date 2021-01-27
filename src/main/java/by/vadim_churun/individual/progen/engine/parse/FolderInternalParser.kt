package by.vadim_churun.individual.progen.engine.parse

import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_NODE_NAME
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FILE
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FOLDER
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_PROJECT
import by.vadim_churun.individual.progen.model.entity.*
import java.io.File
import javax.xml.namespace.QName
import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.StartElement


internal class FolderInternalParser(
    private val reader: XMLEventReader ) {

    private val fileParser = FileInternalParser(reader)


    fun parse(
        parent: File,
        startEvent: StartElement
    ): ProjectFolder {

        val builder = ProjectFolder.Builder(parent)
        val folderName = obtainName(parent, startEvent)
        builder.setName(folderName)

        while(reader.hasNext()) {
            val event = reader.nextEvent()

            when {
                event.isStartElement -> {
                    val child = parseChild(
                        File(parent, folderName),
                        event.asStartElement()
                    )
                    builder.addChild(child)
                }

                event.isCharacters -> {
                    ParserUtil.assertNoCharacters(event.asCharacters(), ELEMENT_FOLDER)
                }

                event.isEndElement -> {
                    break
                }
            }
        }

        return builder.build()
    }


    private fun obtainName(
        parent: File,
        startEvent: StartElement
    ): String {

        val qname = QName(ATTR_NODE_NAME)
        val folderName = startEvent.getAttributeByName(qname)?.value
        if(folderName != null) {
            return folderName
        }

        throw IllegalArgumentException(
            "Folder name must be specified. Parent: ${parent.absolutePath}" )
    }

    private fun parseChild(
        currentNode: File,
        event: StartElement
    ): ProjectNode {

        val name = event.name.localPart
        return when(name) {
            ELEMENT_FOLDER -> this.parse(currentNode, event)
            ELEMENT_FILE   -> fileParser.parse(currentNode, event)
            else -> throw IllegalArgumentException(
                "Unexpected element <$name> inside <$ELEMENT_PROJECT>" )
        }
    }
}