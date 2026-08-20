package io.zyxn.plugin

import io.zyxn.api.InternalZyxnApi
import io.zyxn.api.data.file.KxFile
import io.zyxn.api.data.file.providerKey
import io.zyxn.api.lsp.LanguageServerProvider
import io.zyxn.api.lsp.LanguageServerRegistration
import io.zyxn.api.lsp.LanguageServerRegistry
import io.zyxn.api.plugin.ZyxnPlugin
import org.koin.core.annotation.Single
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Single
class LanguageServerRegistryImpl : LanguageServerRegistry {

    private data class RegistrationInfo(
        val id: String,
        val pattern: String,
        val provider: LanguageServerProvider,
        val plugin: ZyxnPlugin?
    )

    private val registrations = ConcurrentHashMap<String, RegistrationInfo>()
    private val nextId = AtomicInteger(0)

    context(plugin: ZyxnPlugin)
    override fun register(pattern: String, provider: LanguageServerProvider): LanguageServerRegistration {
        return doRegister(pattern, provider, plugin)
    }

    @InternalZyxnApi
    override fun registerInternal(pattern: String, provider: LanguageServerProvider): LanguageServerRegistration {
        return doRegister(pattern, provider, null)
    }

    private fun doRegister(
        pattern: String,
        provider: LanguageServerProvider,
        plugin: ZyxnPlugin?
    ): LanguageServerRegistration {
        val id = nextId.getAndIncrement().toString()
        val info = RegistrationInfo(id, pattern, provider, plugin)
        registrations[id] = info
        return object : LanguageServerRegistration {
            override fun unregister() {
                this@LanguageServerRegistryImpl.unregister(id)
            }
        }
    }

    override fun unregister(id: String) {
        registrations.remove(id)
    }

    override fun getProviders(file: KxFile): List<LanguageServerRegistry.RegisteredProvider> {
        val key = file.providerKey
        return registrations.values
            .filter { it.pattern == key }
            .sortedBy { it.id.toInt() }
            .map { LanguageServerRegistry.RegisteredProvider(it.id, it.provider) }
    }
}
