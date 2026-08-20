package io.zyxn.lsp.server.internal

import io.zyxn.lsp.DidChangeNotebookDocumentParams
import io.zyxn.lsp.DidCloseNotebookDocumentParams
import io.zyxn.lsp.DidOpenNotebookDocumentParams
import io.zyxn.lsp.DidSaveNotebookDocumentParams
import io.zyxn.lsp.server.NotebookDocumentService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

internal class NotebookDocumentServiceImpl(
    val connection: JsonRpcConnection,
    val json: Json
) : NotebookDocumentService {

    private suspend inline fun <reified Params> sendNotification(method: String, params: Params? = null) {
        connection.sendNotification("notebookDocument/$method", json.encodeToJsonElement(params))
    }

    override suspend fun didOpen(params: DidOpenNotebookDocumentParams) {
        sendNotification("didOpen", params)
    }

    override suspend fun didChange(params: DidChangeNotebookDocumentParams) {
        sendNotification("didChange", params)
    }

    override suspend fun didSave(params: DidSaveNotebookDocumentParams) {
        sendNotification("didSave", params)
    }

    override suspend fun didClose(params: DidCloseNotebookDocumentParams) {
        sendNotification("didClose", params)
    }
}
