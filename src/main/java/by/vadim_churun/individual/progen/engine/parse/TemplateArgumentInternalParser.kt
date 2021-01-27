package by.vadim_churun.individual.progen.engine.parse

import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_TEMPLATE_PARAM_KEY
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_TEMPLATE_PARAM_VALUE
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_TEMPLATE_ARG
import by.vadim_churun.individual.progen.model.entity.ProjectFile
import javax.xml.namespace.QName
import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.StartElement


internal class TemplateArgumentInternalParser(
    private val reader: XMLEventReader ) {

    fun parseAndAddTo(
        target: ProjectFile.Builder,
        startEvent: StartElement ) {

        val key = obtainKey(startEvent)
        val value = obtainValue(key, startEvent)

        while(reader.hasNext()) {
            val event = reader.nextEvent()

            when {
                event.isStartElement -> {
                    throw IllegalArgumentException("<$ELEMENT_TEMPLATE_ARG> must have no children")
                }

                event.isEndElement -> {
                    break
                }
            }
        }

        target.addTemplateArgument(key, value)
    }


    private fun obtainKey(startEvent: StartElement): String {
        val qname = QName(ATTR_TEMPLATE_PARAM_KEY)
        return startEvent
            .getAttributeByName(qname)
            ?.value
            ?: throw IllegalArgumentException("Missing key for a template arg")
    }

    private fun obtainValue(
        key: String,
        startEvent: StartElement
    ): String {

        val qname = QName(ATTR_TEMPLATE_PARAM_VALUE)
        return startEvent
            .getAttributeByName(qname)
            ?.value
            ?: throw IllegalArgumentException("Missing value for template arg '$key'")
    }
}