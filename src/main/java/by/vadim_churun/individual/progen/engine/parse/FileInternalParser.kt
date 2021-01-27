package by.vadim_churun.individual.progen.engine.parse

import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_FILE_TEMPLATE
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_NODE_NAME
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_FILE
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_TEMPLATE_ARG
import by.vadim_churun.individual.progen.model.entity.ProjectFile
import java.io.File
import javax.xml.namespace.QName
import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.StartElement


internal class FileInternalParser(
    private val reader: XMLEventReader ) {

    private val templateParser = TemplateArgumentInternalParser(reader)


    fun parse(
        parent: File,
        startEvent: StartElement
    ): ProjectFile {

        val builder = ProjectFile.Builder(parent)
        val fileName = obtainName(parent, startEvent)
        builder.setName(fileName)
        obtainTemplateName(startEvent)?.also { builder.defineTemplate(it) }

        while(reader.hasNext()) {
            val event = reader.nextEvent()

            when {
                event.isStartElement -> {
                    val startChildEvent = event.asStartElement()
                    val name = startChildEvent.name.localPart

                    when(name) {
                        ELEMENT_TEMPLATE_ARG -> templateParser.parseAndAddTo(builder, startChildEvent)
                        else -> throw IllegalArgumentException(
                            "Unexpected element <$name> inside <$ELEMENT_FILE>" )
                    }
                }

                event.isCharacters -> {
                    ParserUtil.assertNoCharacters(event.asCharacters(), ELEMENT_FILE)
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
        val fileName = startEvent.getAttributeByName(qname)?.value
        if(fileName != null) {
            return fileName
        }

        throw IllegalArgumentException(
            "File must have a name. Parent: ${parent.absolutePath}" )
    }

    private fun obtainTemplateName(
        startEvent: StartElement
    ): String? {

        val qname = QName(ATTR_FILE_TEMPLATE)
        return startEvent.getAttributeByName(qname)?.value
    }
}