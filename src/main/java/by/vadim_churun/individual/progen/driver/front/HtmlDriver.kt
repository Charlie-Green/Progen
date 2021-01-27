package by.vadim_churun.individual.progen.driver.front

import by.vadim_churun.individual.progen.model.engine.Arguments
import by.vadim_churun.individual.progen.model.engine.TemplateDriver
import java.io.PrintWriter


/** Generates an HTML file. **/
class HtmlDriver: TemplateDriver {

    override fun generate(wr: PrintWriter, args: Arguments) {
        val stylesheets = args.getList("stylesheets")
        val scripts = args.getList("scripts")
        val pageTitle = args.getString("pageTitle", "")
        val createReactContainer = args.getBoolean("createReactContainer", false)

        wr.println("<!DOCTYPE html>")
        wr.println("<html>")
        wr.println("<head>")
        wr.println("  <meta charset=\"utf-8\">")
        wr.println("  <title>$pageTitle</title>")

        for(filename in stylesheets) {
            wr.println("  <link rel=\"stylesheet\" href=\"$filename\" />")
        }

        wr.println("</head>")
        wr.println("<body>")

        if(createReactContainer) {
            wr.println()
            wr.println("  <div id=\"root\">")
            wr.println("    <!-- React component will go here -->")
            wr.println("  </div>")
        }

        wr.println()
        for(filename in scripts) {
            wr.println("  <script src=\"$filename\"></script>")
        }

        wr.println("</body>")
        wr.println("</html>")
    }


    private fun Arguments.getList(attrName: String): List<String> {
        return getString(attrName, "")
            .split(" ")
            .filter { !it.isEmpty() }
    }
}