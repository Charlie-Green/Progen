package by.vadim_churun.individual.progen.engine.parse


internal object ProjectXmlContract {

    const val ELEMENT_PROJECT      = "project"
    const val ELEMENT_FOLDER       = "folder"
    const val ELEMENT_FILE         = "file"
    const val ELEMENT_TEMPLATE_ARG = "arg"

    const val ATTR_PROJECT_PATH         = "path"
    const val ATTR_NODE_NAME            = "name"
    const val ATTR_FILE_TEMPLATE        = "template"
    const val ATTR_TEMPLATE_PARAM_KEY   = "key"
    const val ATTR_TEMPLATE_PARAM_VALUE = "value"
}