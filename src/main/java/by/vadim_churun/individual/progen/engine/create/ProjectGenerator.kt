package by.vadim_churun.individual.progen.engine.create

import by.vadim_churun.individual.progen.model.entity.*
import java.io.FileOutputStream
import java.io.PrintWriter


class ProjectGenerator {

    fun generate(proj: Project) {
        val root = proj.file
        try {
            generateProject(proj)
        } catch(thr: Throwable) {
            root.deleteRecursively()
            throw thr
        }
    }


    private fun generateProject(proj: Project) {
        proj.create()
        createNodes(proj.children)
    }

    private fun generateFolder(fold: ProjectFolder) {
        fold.create()
        createNodes(fold.children)
    }

    private fun generateFile(file: ProjectFile) {
        file.create()
        generateFileContent(file)
    }


    private fun createNodes(nodes: Collection<ProjectNode>) {
        for(node in nodes) {
            when(node) {
                is Project       -> generateProject(node)
                is ProjectFolder -> generateFolder(node)
                is ProjectFile   -> generateFile(node)
                else -> throw IllegalArgumentException(
                    "Don't know how to generate a ${node.javaClass.name}" )
            }
        }
    }

    private fun generateFileContent(file: ProjectFile) {
        val template = file.templateName ?: return
        val driver = TemplateDriverLoader.get(template)

        FileOutputStream(file.file).use { ostream ->
            PrintWriter(ostream).use { writer ->
                driver.generate(writer, file.templateArgs)
            }
        }
    }
}