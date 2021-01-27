package by.vadim_churun.individual.progen.engine.parse

import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_FILE_TEMPLATE
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FILE
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_TEMPLATE_ARG
import by.vadim_churun.individual.progen.model.entity.ProjectFile
import java.io.File
import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.Attribute


internal class FileInternalParser(
    private val reader: XMLEventReader ) {

    private val templateParser = TemplateArgumentInternalParser(reader)


    fun parse(parent: File): ProjectFile {
        val builder = ProjectFile.Builder(parent)

        while(reader.hasNext()) {
            val event = reader.nextEvent()

            when {
                event.isStartElement -> {
                    val startEvent = event.asStartElement()
                    val name = startEvent.name.localPart

                    when(name) {
                        ELEMENT_TEMPLATE_ARG -> templateParser.parseAndAddTo(builder)
                        else -> throw IllegalArgumentException(
                            "Unexpected element <$name> inside <$ELEMENT_FILE>" )
                    }
                }

                event.isAttribute -> {
                    val attrEvent = event as Attribute
                    val name = attrEvent.name.localPart
                    when(name) {
                        ATTR_FILE_TEMPLATE -> builder.defineTemplate(attrEvent.value)
                        else -> throw IllegalArgumentException(
                            "Unknown attribute $name of element <$ELEMENT_FILE>" )
                    }
                }

                event.isCharacters -> {
                    throw IllegalArgumentException(
                        "Did not expect characters inside <$ELEMENT_FILE>" )
                }

                event.isEndElement -> {
                    break
                }
            }
        }

        return builder.build()
    }
}