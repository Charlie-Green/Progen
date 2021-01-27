package by.vadim_churun.individual.progen.engine.create

import by.vadim_churun.individual.progen.driver.front.ReactPackageJsonDriver
import by.vadim_churun.individual.progen.model.engine.TemplateDriver


object TemplateDriverLoader {
    private val classNames = hashMapOf<String, String>(
        "reactPackage" to ReactPackageJsonDriver::class.java.name
    )

    private val loaded = hashMapOf<String, TemplateDriver>()


    fun get(alias: String): TemplateDriver {
        return loaded[alias]
            ?: load(alias).also { loaded[alias] = it }
    }


    private fun load(alias: String): TemplateDriver {
        val className = if(alias.startsWith("/")) {
            alias.substring(1)
        } else {
            classNames[alias]
                ?: throw IllegalArgumentException(
                    "Unknown find driver classname for template '$alias" )
        }

        val driverInterface = TemplateDriver::class.java
        val driverClass = try {
            Class.forName(className)
        } catch(exc: ClassNotFoundException) {
            throw IllegalArgumentException(
                "Cannot load ${driverInterface.simpleName} " +
                "specified by class name: $className",
                exc
            )
        }

        if(!driverInterface.isAssignableFrom(driverClass)) {
            throw IllegalArgumentException(
                "$className doesn't implement ${driverInterface.simpleName}" )
        }

        val driver = try {
            driverClass.getConstructor().newInstance()
        } catch(exc: ReflectiveOperationException) {
            throw RuntimeException(
                "Cannot instantiate ${driverInterface} " +
                "specified by class name: $className. " +
                "Does it have a default constructor?",
                exc
            )
        }

        return driver as TemplateDriver
    }
}