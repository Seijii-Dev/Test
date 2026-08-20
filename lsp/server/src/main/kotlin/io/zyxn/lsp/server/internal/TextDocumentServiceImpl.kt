@file:Suppress("DEPRECATION")

package io.zyxn.lsp.server.internal

import io.zyxn.lsp.CallHierarchyIncomingCall
import io.zyxn.lsp.CallHierarchyIncomingCallsParams
import io.zyxn.lsp.CallHierarchyItem
import io.zyxn.lsp.CallHierarchyOutgoingCall
import io.zyxn.lsp.CallHierarchyOutgoingCallsParams
import io.zyxn.lsp.CallHierarchyPrepareParams
import io.zyxn.lsp.CodeAction
import io.zyxn.lsp.CodeActionParams
import io.zyxn.lsp.CodeLens
import io.zyxn.lsp.CodeLensParams
import io.zyxn.lsp.ColorInformation
import io.zyxn.lsp.ColorPresentation
import io.zyxn.lsp.ColorPresentationParams
import io.zyxn.lsp.Command
import io.zyxn.lsp.CompletionItem
import io.zyxn.lsp.CompletionList
import io.zyxn.lsp.CompletionParams
import io.zyxn.lsp.DeclarationParams
import io.zyxn.lsp.DefinitionParams
import io.zyxn.lsp.DidChangeTextDocumentParams
import io.zyxn.lsp.DidCloseTextDocumentParams
import io.zyxn.lsp.DidOpenTextDocumentParams
import io.zyxn.lsp.DidSaveTextDocumentParams
import io.zyxn.lsp.DocumentColorParams
import io.zyxn.lsp.DocumentDiagnosticParams
import io.zyxn.lsp.DocumentDiagnosticReport
import io.zyxn.lsp.DocumentFormattingParams
import io.zyxn.lsp.DocumentHighlight
import io.zyxn.lsp.DocumentHighlightParams
import io.zyxn.lsp.DocumentLink
import io.zyxn.lsp.DocumentLinkParams
import io.zyxn.lsp.DocumentOnTypeFormattingParams
import io.zyxn.lsp.DocumentRangeFormattingParams
import io.zyxn.lsp.DocumentRangesFormattingParams
import io.zyxn.lsp.DocumentSymbol
import io.zyxn.lsp.DocumentSymbolParams
import io.zyxn.lsp.FoldingRange
import io.zyxn.lsp.FoldingRangeParams
import io.zyxn.lsp.Hover
import io.zyxn.lsp.HoverParams
import io.zyxn.lsp.ImplementationParams
import io.zyxn.lsp.InlayHint
import io.zyxn.lsp.InlayHintParams
import io.zyxn.lsp.InlineCompletionItem
import io.zyxn.lsp.InlineCompletionList
import io.zyxn.lsp.InlineCompletionParams
import io.zyxn.lsp.InlineValue
import io.zyxn.lsp.InlineValueParams
import io.zyxn.lsp.LinkedEditingRangeParams
import io.zyxn.lsp.LinkedEditingRanges
import io.zyxn.lsp.Location
import io.zyxn.lsp.LocationLink
import io.zyxn.lsp.Moniker
import io.zyxn.lsp.MonikerParams
import io.zyxn.lsp.PrepareRenameDefaultBehavior
import io.zyxn.lsp.PrepareRenameParams
import io.zyxn.lsp.PrepareRenameResult
import io.zyxn.lsp.Range
import io.zyxn.lsp.ReferenceParams
import io.zyxn.lsp.RenameParams
import io.zyxn.lsp.SelectionRange
import io.zyxn.lsp.SelectionRangeParams
import io.zyxn.lsp.SemanticTokens
import io.zyxn.lsp.SemanticTokensDelta
import io.zyxn.lsp.SemanticTokensDeltaParams
import io.zyxn.lsp.SemanticTokensParams
import io.zyxn.lsp.SemanticTokensRangeParams
import io.zyxn.lsp.SignatureHelp
import io.zyxn.lsp.SignatureHelpParams
import io.zyxn.lsp.SymbolInformation
import io.zyxn.lsp.TextEdit
import io.zyxn.lsp.TypeDefinitionParams
import io.zyxn.lsp.TypeHierarchyItem
import io.zyxn.lsp.TypeHierarchyPrepareParams
import io.zyxn.lsp.TypeHierarchySupertypesParams
import io.zyxn.lsp.WillSaveTextDocumentParams
import io.zyxn.lsp.WorkspaceEdit
import io.zyxn.lsp.server.TextDocumentService
import io.zyxn.lsp.types.OneOf
import io.zyxn.lsp.types.OneOfThree
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

internal class TextDocumentServiceImpl(val connection: JsonRpcConnection, val json: Json) : TextDocumentService {
    private suspend inline fun <reified T, reified Params> sendRequest(method: String, params: Params? = null): T {
        return connection.sendRequest("textDocument/$method", params)
    }

    private suspend inline fun <reified Params> sendNotification(method: String, params: Params? = null) {
        connection.sendNotification("textDocument/$method", json.encodeToJsonElement(params))
    }

    override suspend fun didOpen(params: DidOpenTextDocumentParams) {
        sendNotification("didOpen", params)
    }

    override suspend fun didChange(params: DidChangeTextDocumentParams) {
        sendNotification("didChange", params)
    }

    override suspend fun willSave(params: WillSaveTextDocumentParams) {
        sendNotification("willSave", params)
    }

    override suspend fun willSaveWaitUntil(params: WillSaveTextDocumentParams): List<TextEdit>? {
        return sendRequest("willSaveWaitUntil", params)
    }

    override suspend fun didSave(params: DidSaveTextDocumentParams) {
        sendNotification("didSave", params)
    }

    override suspend fun didClose(params: DidCloseTextDocumentParams) {
        sendNotification("didClose", params)
    }

    override suspend fun declaration(params: DeclarationParams): OneOf<List<Location>, List<LocationLink>>? {
        return sendRequest("declaration", params)
    }

    override suspend fun definition(params: DefinitionParams): OneOf<List<Location>, List<LocationLink>>? {
        return sendRequest("definition", params)
    }

    override suspend fun typeDefinition(params: TypeDefinitionParams): OneOf<List<Location>, List<LocationLink>>? {
        return sendRequest("typeDefinition", params)
    }

    override suspend fun implementation(params: ImplementationParams): OneOf<List<Location>, List<LocationLink>>? {
        return sendRequest("implementation", params)
    }

    override suspend fun references(params: ReferenceParams): List<LocationLink>? {
        return sendRequest("references", params)
    }

    override suspend fun prepareCallHierarchy(params: CallHierarchyPrepareParams): List<CallHierarchyItem>? {
        return sendRequest("prepareCallHierarchy", params)
    }

    override suspend fun callHierarchyIncomingCalls(params: CallHierarchyIncomingCallsParams): List<CallHierarchyIncomingCall>? {
        return connection.sendRequest("callHierarchy/incomingCalls", params)
    }

    override suspend fun callHierarchyOutgoingCalls(params: CallHierarchyOutgoingCallsParams): List<CallHierarchyOutgoingCall>? {
        return connection.sendRequest("callHierarchy/outgoingCalls", params)
    }

    override suspend fun prepareTypeHierarchy(params: TypeHierarchyPrepareParams): List<TypeHierarchyItem>? {
        return sendRequest("prepareTypeHierarchy", params)
    }

    override suspend fun typeHierarchySupertypes(params: TypeHierarchySupertypesParams): List<TypeHierarchyItem>? {
        return connection.sendRequest("typeHierarchy/supertypes", params)
    }

    override suspend fun documentHighlight(params: DocumentHighlightParams): List<DocumentHighlight>? {
        return sendRequest("documentHighlight", params)
    }

    override suspend fun documentLink(params: DocumentLinkParams): List<DocumentLink>? {
        return sendRequest("documentLink", params)
    }

    override suspend fun resolveDocumentLink(unresolved: DocumentLink): DocumentLink {
        return connection.sendRequest("documentLink/resolve", unresolved)
    }

    override suspend fun hover(params: HoverParams): Hover? {
        return sendRequest("hover", params)
    }

    override suspend fun codeLens(params: CodeLensParams): List<CodeLens>? {
        return sendRequest("codeLens", params)
    }

    override suspend fun resolveCodeLens(unresolved: CodeLens): CodeLens {
        return connection.sendRequest("codeLens/resolve", unresolved)
    }

    override suspend fun foldingRange(params: FoldingRangeParams): List<FoldingRange>? {
        return sendRequest("foldingRange", params)
    }

    override suspend fun selectionRange(params: SelectionRangeParams): List<SelectionRange>? {
        return sendRequest("selectionRange", params)
    }

    override suspend fun documentSymbol(params: DocumentSymbolParams): OneOf<List<DocumentSymbol>, List<SymbolInformation>>? {
        return sendRequest("documentSymbol", params)
    }

    override suspend fun semanticTokensFull(params: SemanticTokensParams): SemanticTokens? {
        return sendRequest("semanticTokens/full", params)
    }

    override suspend fun semanticTokensFullDelta(params: SemanticTokensDeltaParams): OneOf<SemanticTokens, SemanticTokensDelta>? {
        return sendRequest("semanticTokens/full/delta", params)
    }

    override suspend fun semanticTokensRange(params: SemanticTokensRangeParams): SemanticTokens? {
        return sendRequest("semanticTokens/range", params)
    }

    override suspend fun inlayHint(params: InlayHintParams): List<InlayHint>? {
        return sendRequest("inlayHint", params)
    }

    override suspend fun resolveInlayHint(unresolved: InlayHint): InlayHint {
        return connection.sendRequest("inlayHint/resolve", unresolved)
    }

    override suspend fun inlineValue(params: InlineValueParams): List<InlineValue>? {
        return sendRequest("inlineValue", params)
    }

    override suspend fun moniker(params: MonikerParams): List<Moniker>? {
        return sendRequest("moniker", params)
    }

    override suspend fun completion(params: CompletionParams): OneOf<List<CompletionItem>, CompletionList>? {
        return sendRequest("completion", params)
    }

    override suspend fun resolveCompletionItem(unresolved: CompletionItem): CompletionItem {
        return connection.sendRequest("completionItem/resolve", unresolved)
    }

    override suspend fun diagnostic(params: DocumentDiagnosticParams): DocumentDiagnosticReport {
        return sendRequest("diagnostic", params)
    }

    override suspend fun signatureHelp(params: SignatureHelpParams): SignatureHelp? {
        return sendRequest("signatureHelp", params)
    }

    override suspend fun codeAction(params: CodeActionParams): List<OneOf<Command, CodeAction>>? {
        return sendRequest("codeAction", params)
    }

    override suspend fun resolveCodeAction(unresolved: CodeAction): CodeAction {
        return connection.sendRequest("codeAction/resolve", unresolved)
    }

    override suspend fun documentColor(params: DocumentColorParams): List<ColorInformation> {
        return sendRequest("documentColor", params)
    }

    override suspend fun colorPresentation(params: ColorPresentationParams): List<ColorPresentation> {
        return sendRequest("colorPresentation", params)
    }

    override suspend fun formatting(params: DocumentFormattingParams): List<TextEdit>? {
        return sendRequest("formatting", params)
    }

    override suspend fun rangeFormatting(params: DocumentRangeFormattingParams): List<TextEdit>? {
        return sendRequest("rangeFormatting", params)
    }

    override suspend fun rangesFormatting(params: DocumentRangesFormattingParams): List<TextEdit>? {
        return sendRequest("rangesFormatting", params)
    }

    override suspend fun onTypeFormatting(params: DocumentOnTypeFormattingParams): List<TextEdit>? {
        return sendRequest("onTypeFormatting", params)
    }

    override suspend fun rename(params: RenameParams): WorkspaceEdit? {
        return sendRequest("rename", params)
    }

    override suspend fun prepareRename(params: PrepareRenameParams): OneOfThree<Range, PrepareRenameResult, PrepareRenameDefaultBehavior>? {
        return sendRequest("prepareRename", params)
    }

    override suspend fun linkedEditingRange(params: LinkedEditingRangeParams): LinkedEditingRanges? {
        return sendRequest("linkedEditingRange", params)
    }

    override suspend fun inlineCompletion(params: InlineCompletionParams): OneOf<List<InlineCompletionItem>, InlineCompletionList>? {
        return sendRequest("inlineCompletion", params)
    }
}
