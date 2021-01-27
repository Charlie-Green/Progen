package by.vadim_churun.individual.progen.driver.front

import by.vadim_churun.individual.progen.model.engine.Arguments
import by.vadim_churun.individual.progen.model.engine.TemplateDriver
import java.io.PrintWriter


/** Generates React's package.json file. **/
class ReactPackageJsonDriver: TemplateDriver {

    override fun generate(wr: PrintWriter, args: Arguments) {
        val projectName = args.getString("projectName")
        val projectVersion = args.getString("projectVersion", "1.0")
        val reactVersion = args.getString("reactVersion", "17.0.1")
        val scriptsVersion = args.getString("scriptsVersion", "4.0.1")
        val isPrivate = args.getBoolean("isPrivate", true)

        wr.println("{")
        wr.println("  \"name\": \"$projectName\",")
        wr.println("  \"version\": \"$projectVersion\",")
        wr.println("  \"private\": $isPrivate,")
        wr.println()
        wr.println("  \"dependencies\": {")
        wr.println("    \"react\": \"^$reactVersion\",")
        wr.println("    \"react-dom\": \"^$reactVersion\",")
        wr.println("    \"react-scripts\": \"^$scriptsVersion\"")
        wr.println("  },")
        wr.println()
        wr.println("  \"browsersList\": {")
        wr.println("    \"development\": [")
        wr.println("      \"last 1 chrome version\",")
        wr.println("      \"last 1 firefox version\",")
        wr.println("      \"last 1 safari version\"")
        wr.println("    ]")
        wr.println("  }")
        wr.println("}")
    }
}