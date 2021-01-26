package by.vadim_churun.individual.progen.model.engine

import java.io.PrintWriter


/** Generates content of a single file. **/
interface TemplateDriver {
    fun generate(wr: PrintWriter, args: TemplateArguments)
}