package by.vadim_churun.individual.progen.engine.parse

import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_PROJECT_PATH
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FILE
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FOLDER
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_PROJECT
import by.vadim_churun.individual.progen.model.entity.Project
import by.vadim_churun.individual.progen.model.entity.ProjectNode
import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.StartElement


class ProjectInternalParser(
    private val reader: XMLEventReader ) {

    private val folderParser = FolderInternalParser(reader)
    private val fileParser   = FileInternalParser(reader)


    fun parse(): Project {
        val builder = Project.Builder()

        while(reader.hasNext()) {
            val event = reader.nextEvent()

            when {
                event.isStartElement -> {
                    val child = parseChild(event.asStartElement())
                    builder.addChild(child)
                }

                event.isCharacters -> {
                    throw IllegalArgumentException(
                        "Characters directly inside <$ELEMENT_PROJECT> are not expected" )
                }

                event.isAttribute -> {
                    addAttribute(builder, event as Attribute)
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
            ELEMENT_FOLDER -> folderParser.parse()
            ELEMENT_FILE   -> fileParser.parse()
            else -> throw IllegalArgumentException(
                "Unexpected element <$name> inside <$ELEMENT_PROJECT>" )
        }
    }


    private fun addAttribute(
        builder: Project.Builder,
        event: Attribute ) {

        val key = event.name.localPart
        when(key) {
            ATTR_PROJECT_PATH -> builder.setPath(event.value)
            else ->  throw IllegalArgumentException(
                "Unknown parameter $key of element <$ELEMENT_PROJECT>" )
        }
    }
}