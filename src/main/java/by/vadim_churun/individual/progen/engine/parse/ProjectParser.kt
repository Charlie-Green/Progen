package by.vadim_churun.individual.progen.engine.parse

import by.vadim_churun.individual.progen.model.entity.Project
import java.io.Reader
import javax.xml.stream.XMLInputFactory


class ProjectParser(
    private val stringReader: Reader
): AutoCloseable {

    private val reader = XMLInputFactory
        .newInstance()
        .createXMLEventReader(stringReader)


    fun parse(): Project {
        return RootInternalParser(reader).parse()
    }


    override fun close() {
        reader.close()
        stringReader.close()
    }
}