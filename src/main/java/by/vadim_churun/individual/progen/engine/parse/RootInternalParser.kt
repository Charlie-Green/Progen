package by.vadim_churun.individual.progen.engine.parse

import by.vadim_churun.individual.progen.engine.parse.ProjectXmlContract.ELEMENT_PROJECT
import by.vadim_churun.individual.progen.model.entity.Project
import javax.xml.stream.XMLEventReader


internal class RootInternalParser(
    private val reader: XMLEventReader ) {

    private val projectParser = ProjectInternalParser(reader)


    fun parse(): Project {
        while(reader.hasNext()) {
            val event = reader.nextEvent()

            when {
                event.isStartElement -> {
                    val startEvent = event.asStartElement()
                    val elementName = startEvent.name.localPart
                    if(elementName != ELEMENT_PROJECT) {
                        throw IllegalArgumentException("Root element must be <$ELEMENT_PROJECT>")
                    }

                    return projectParser.parse(null)
                }
            }
        }

        throw IllegalArgumentException("XML has no element")
    }
}