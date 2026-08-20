package io.zyxn.compiler.plugin

import io.zyxn.compiler.plugin.fir.ZyxnFirExtensionRegistrar
import io.zyxn.compiler.plugin.ir.ZyxnIrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

class ZyxnCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override val pluginId = BuildConfig.KOTLIN_PLUGIN_ID
    override val supportsK2 = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val descriptorOutputDir = configuration[ZyxnConfigurationKeys.DESCRIPTOR_OUTPUT_DIR]
        val descriptorIcon = configuration[ZyxnConfigurationKeys.DESCRIPTOR_ICON]
        val messageCollector = configuration[CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY]
            ?: MessageCollector.NONE

        FirExtensionRegistrarAdapter.registerExtension(ZyxnFirExtensionRegistrar(messageCollector))
        IrGenerationExtension.registerExtension(
            ZyxnIrGenerationExtension(
                descriptorOutputDir = descriptorOutputDir,
                descriptorIcon = descriptorIcon,
                messageCollector = messageCollector
            )
        )
    }
}
