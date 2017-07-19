package com.chiepherd.app.plugins

import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile
import java.io.File
import java.io.FileFilter
import java.io.IOException
import java.util.ArrayList


class PluginLoader {
    fun foo(path : String) {
        val klasses = loadClassNames(path)

        val urls = arrayOf(URL("jar:file:$path!/"))
        val klassLoader = URLClassLoader.newInstance(urls)

        println(klasses)
        println(klasses[1])

        println(com.chiepherd.core.annotations.ChiepherdPlugin::class.java.name)
        val annotKlass = klassLoader.loadClass(com.chiepherd.core.annotations.ChiepherdPlugin::class.java.name)

        val klassName = klasses[1]
        val klass = klassLoader.loadClass(klassName)
        println(klass)
        println(klass.declaredAnnotations[0])
        println(klass.declaredAnnotations[0])
        // klass.declaredAnnotations[0].annotationClass
        println("${klass.isAnnotationPresent(annotKlass as Class<out Annotation>)}")

        if(klass.isAnnotationPresent(annotKlass)) {
            val annotData = klass.getDeclaredAnnotation(com.chiepherd.core.annotations.ChiepherdPlugin::class.java)
            println(annotData.name)
            println(annotData.fxml)
        }
    }

    private fun loadClassNames(path : String) : List<String> {
        val jarFile = JarFile(path)
        val entries = jarFile.entries()
        val names = mutableListOf<String>()

        for (entry in entries) {
            if(entry.isDirectory || !entry.name.endsWith(".class")) {
                continue
            }

            val className = entry.name.substring(0, entry.name.length - 6).replace('/', '.')

            names.add(className)
        }

        return names
    }
}


object ModuleLoader {
    private val urls = ArrayList<URL>()

    private val moduleClasses: List<String>
        get() {
            val classes = ArrayList<String>()
            val files = File("plugins").listFiles(ModuleFilter())

            for (f in files) {
                var jarFile: JarFile? = null

                try {
                    jarFile = JarFile(f)
                    val manifest = jarFile.manifest
                    val moduleClassName = manifest.mainAttributes.getValue("Module-Class")

                    classes.add(moduleClassName)

                    urls.add(f.toURI().toURL())
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    if (jarFile != null) jarFile.close()
                }
            }

            return classes
        }

    private class ModuleFilter : FileFilter {
        override fun accept(file: File): Boolean {
            return file.isFile && file.name.toLowerCase().endsWith(".jar")
        }
    }
}
