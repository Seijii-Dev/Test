package io.zyxn.i18n.processor

data class Configs(
    val packageName: String,
    val moduleName: String,
    val internalVisibility: Boolean,
    val generateStringsProperty: Boolean
)
