package by.vadim_churun.individual.progen.engine.parse

import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_PROJECT_PATH
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FILE
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FOLDER
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_PROJECT
import by.vadim_churun.individual.progen.model.entity.Project
import by.vadim_churun.individual.progen.model.entity.ProjectNode
import java.io.File
import javax.xml.namespace.QName
import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.StartElement


internal class ProjectInternalParser(
    private val reader: XMLEventReader ) {

    private val folderParser = FolderInternalParser(reader)
    private val fileParser   = FileInternalParser(reader)


    fun parse(parent: File?, startEvent: StartElement): Project {
        val builder = Project.Builder(parent)
        val path = obtainPath(parent, startEvent)
        builder.setPath(path)

        while(reader.hasNext()) {
            val event = reader.nextEvent()

            when {
                event.isStartElement -> {
                    val child = parseChild(
                        if(parent == null) File(path) else File(parent, path),
                        event.asStartElement()
                    )
                    builder.addChild(child)
                }

                event.isCharacters -> {
                    ParserUtil.assertNoCharacters(event.asCharacters(), ELEMENT_PROJECT)
                }

                event.isEndElement -> {
                    break
                }
            }
        }

        return builder.build()
    }


    private fun obtainPath(
        parent: File?,
        startEvent: StartElement
    ): String {

        val qname = QName(ATTR_PROJECT_PATH)
        val path = startEvent.getAttributeByName(qname)?.value
        if(path != null) {
            return path
        }

        val drawbackLocation =
            if(parent == null) "At root project."
            else "Under ${parent.absolutePath}"
        throw IllegalArgumentException(
            "Project path must be specified. - $drawbackLocation")
    }

    private fun parseChild(
        thisNode: File,
        event: StartElement
    ): ProjectNode {

        val name = event.name.localPart
        return when(name) {
            ELEMENT_FOLDER -> folderParser.parse(thisNode, event)
            ELEMENT_FILE   -> fileParser.parse(thisNode, event)
            else -> throw IllegalArgumentException(
                "Unexpected element <$name> inside <$ELEMENT_PROJECT>" )
        }
    }
}