package io.zyxn.compiler.plugin.fir

import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class ZyxnFirExtensionRegistrar(private val messageCollector: MessageCollector) : FirExtensionRegistrar() {

    override fun ExtensionRegistrarContext.configurePlugin() {
        +::ZyxnManifestCheckers
        +{ session: FirSession -> ZyxnDescriptorGenerationExtension(session, messageCollector) }
    }
}
