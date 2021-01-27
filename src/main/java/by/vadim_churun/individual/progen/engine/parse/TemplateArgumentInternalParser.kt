package by.vadim_churun.individual.progen.engine.parse

import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_TEMPLATE_PARAM_KEY
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ATTR_TEMPLATE_PARAM_VALUE
import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_TEMPLATE_ARG
import by.vadim_churun.individual.progen.model.entity.ProjectFile
import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.Attribute


internal class TemplateArgumentInternalParser(
    private val reader: XMLEventReader ) {

    fun parseAndAddTo(target: ProjectFile.Builder) {
        var key: String? = null
        var value: String? = null

        while(reader.hasNext()) {
            val event = reader.nextEvent()

            when {
                event.isAttribute -> {
                    val attrEvent = event as Attribute
                    val name = attrEvent.name.localPart

                    when(name) {
                        ATTR_TEMPLATE_PARAM_KEY -> {
                            key = attrEvent.value
                        }

                        ATTR_TEMPLATE_PARAM_VALUE -> {
                            value = attrEvent.value
                        }

                        else -> {
                            throw IllegalArgumentException(
                                "Unknown attribute $name of element <$ELEMENT_TEMPLATE_ARG>" )
                        }
                    }
                }

                event.isStartElement -> {
                    throw IllegalArgumentException("<$ELEMENT_TEMPLATE_ARG> must have no children")
                }

                event.isEndElement -> {
                    break
                }
            }
        }

        key ?: throw IllegalArgumentException(
            "Missing key for a template arg" )
        value ?: throw IllegalArgumentException(
            "Missing value for template arg '$key'" )

        target.addTemplateArgument(key, value)
    }
}