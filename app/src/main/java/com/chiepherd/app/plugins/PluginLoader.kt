package com.chiepherd.app.plugins

import com.chiepherd.core.annotations.ChiepherdPlugin
import com.chiepherd.core.controllers.ApplicationController
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile
import java.io.File
import java.io.FileFilter
import java.io.IOException

class PluginLoader(val path : URI) {
    val pluginList = PluginList.instance

    private class JarFilter : FileFilter {
        override fun accept(file: File): Boolean {
            return file.isFile && file.name.toLowerCase().endsWith(".jar")
        }
    }

    fun load() : PluginList {
        val files = File(path).listFiles(JarFilter())

        files.forEach {
            val jarUrl = URL("jar:${it.toURI().toURL()}!/")
            val classLoader = URLClassLoader(arrayOf(jarUrl))
            loadClassNamesAnnotated(it.toString(), classLoader)
        }

        return pluginList
    }

    private fun loadClassNamesAnnotated(path : String, classLoader : ClassLoader) {
        loadClassNames(path).forEach {
            val klass = classLoader.loadClass(it)

            if(klass.isAnnotationPresent(ChiepherdPlugin::class.java)
                    && klass.superclass == ApplicationController::class.java) {
                val annotation = klass.getDeclaredAnnotation(ChiepherdPlugin::class.java)
                pluginList.add(PluginList.Plugin(classLoader, annotation.fxml, annotation.name, annotation.internal, klass.name))
            }
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

        jarFile.close()

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
