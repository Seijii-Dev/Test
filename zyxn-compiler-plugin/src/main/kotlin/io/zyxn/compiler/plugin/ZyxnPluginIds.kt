package io.zyxn.compiler.plugin

import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

internal object ZyxnPluginIds {
    const val OPTION_DESCRIPTOR_OUTPUT_DIR = "descriptorOutputDir"
    const val OPTION_DESCRIPTOR_ICON = "descriptorIcon"

    val PLUGIN_MANIFEST_FQN = FqName("io.zyxn.api.plugin.PluginManifest")
    val PLUGIN_MANIFEST_CLASS_ID = ClassId.topLevel(PLUGIN_MANIFEST_FQN)

    val PLUGIN_AUTHOR_CLASS_ID = ClassId.topLevel(FqName("io.zyxn.api.plugin.PluginAuthor"))
    val PLUGIN_LINKS_CLASS_ID = ClassId.topLevel(FqName("io.zyxn.api.plugin.PluginLinks"))

    val ZYXN_PLUGIN_FQN = FqName("io.zyxn.api.plugin.ZyxnPlugin")
    val ZYXN_PLUGIN_CLASS_ID = ClassId.topLevel(ZYXN_PLUGIN_FQN)

    val PLUGIN_DESCRIPTOR_FQN = FqName("io.zyxn.api.plugin.PluginDescriptor")
    val PLUGIN_DESCRIPTOR_CLASS_ID = ClassId.topLevel(PLUGIN_DESCRIPTOR_FQN)

    val DESCRIPTOR_PROPERTY_NAME = Name.identifier("descriptor")

    private val SEMVER_REGEX = Regex("""^\d+\.\d+\.\d+(-[0-9A-Za-z.-]+)?(\+[0-9A-Za-z.-]+)?$""")
    private val REVERSE_DNS_REGEX = Regex("""^[a-z0-9]+(\.[a-z0-9-]+)+$""")

    fun isValidSemver(value: String): Boolean = SEMVER_REGEX.matches(value)
    fun isValidPluginId(value: String): Boolean = REVERSE_DNS_REGEX.matches(value)
}
