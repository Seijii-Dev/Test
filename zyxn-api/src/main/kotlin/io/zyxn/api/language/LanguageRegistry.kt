package io.zyxn.api.language

import io.zyxn.api.plugin.ZyxnPlugin
import io.zyxn.api.plugin.PluginService

interface LanguageRegistry : PluginService {

    context(plugin: ZyxnPlugin)
    fun register(
        descriptor: LanguageDescriptor,
        grammarProvider: LanguageGrammarProvider,
        queries: QueryProvider,
        theme: LanguageThemeProvider? = null,
    ): LanguageRegistration

    fun unregister(id: String)

    fun getDescriptor(name: String): LanguageDescriptor?

    fun getExtensions(): Map<String, String>

    fun getFileNames(): Map<String, String>
}
