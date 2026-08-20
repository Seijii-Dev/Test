package io.zyxn.lsp.capabilities

import io.zyxn.lsp.CodeActionTag
import kotlinx.serialization.Serializable

@Serializable
data class CodeActionTagSupportClientCapabilities(
    /**
     * The tags supported by the client.
     */
    val valueSet: List<CodeActionTag>
)
