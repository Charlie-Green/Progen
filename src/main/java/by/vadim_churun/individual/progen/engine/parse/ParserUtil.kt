package by.vadim_churun.individual.progen.engine.parse

import javax.xml.stream.events.Characters


internal object ParserUtil {

    fun assertNoCharacters(event: Characters, tagName: String) {
        if(!event.data.isBlank()) {
            throw IllegalArgumentException(
                "Did not expect characters directly inside <$tagName>. " +
                "Found \"${event.data.trim()}\""
            )
        }
    }
}