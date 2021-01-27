package by.vadim_churun.individual.progen.engine.parse

import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_NODE_NAME
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FILE
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FOLDER
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_PROJECT
import by.vadim_churun.individual.progen.model.entity.*
import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.StartElement


internal class FolderInternalParser(
    private val reader: XMLEventReader ) {

    private val fileParser = FileInternalParser(reader)


    fun parse(): ProjectFolder {
        val builder = ProjectFolder.Builder()

        while(reader.hasNext()) {
            val event = reader.nextEvent()

            when {
                event.isStartElement -> {
                    val child = parseChild(event.asStartElement())
                    builder.addChild(child)
                }

                event.isAttribute -> {
                    addAttribute(builder, event as Attribute)
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


    private fun parseChild(event: StartElement): ProjectNode {
        val name = event.name.localPart
        return when(name) {
            ELEMENT_FOLDER -> this.parse()
            ELEMENT_FILE   -> fileParser.parse()
            else -> throw IllegalArgumentException(
                "Unexpected element <$name> inside <$ELEMENT_PROJECT>" )
        }
    }


    private fun addAttribute(
        builder: ProjectFolder.Builder,
        event: Attribute) {

        val key = event.name.localPart
        when(key) {
            ATTR_NODE_NAME -> builder.setName(event.value)
            else -> throw IllegalArgumentException(
                "Unknown attribute $key of element <$ELEMENT_FOLDER>" )
        }
    }
}