package by.vadim_churun.individual.progen.app

import by.vadim_churun.individual.progen.engine.create.ProjectGenerator
import by.vadim_churun.individual.progen.engine.parse.ProjectParser
import by.vadim_churun.individual.progen.model.entity.Project
import java.io.FileInputStream
import java.io.InputStreamReader


class ProgenApplication(
    private val projectFileName: String ) {

    fun main() {
        val proj = parseProject()
        generateProject(proj)
        println("Done!")
    }


    private fun parseProject(): Project {
        return FileInputStream(projectFileName).use { istream ->
            InputStreamReader(istream, Charsets.UTF_8).use { reader ->
                ProjectParser(reader).use { parser ->
                    parser.parse()
                }
            }
        }
    }


    private fun generateProject(proj: Project) {
        ProjectGenerator().generate(proj)
    }
}