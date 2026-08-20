package io.zyxn.lsp.capabilities

import io.zyxn.lsp.CallHierarchyOptions
import io.zyxn.lsp.CallHierarchyRegistrationOptions
import io.zyxn.lsp.CodeActionOptions
import io.zyxn.lsp.CodeLensOptions
import io.zyxn.lsp.CompletionOptions
import io.zyxn.lsp.DeclarationOptions
import io.zyxn.lsp.DeclarationRegistrationOptions
import io.zyxn.lsp.DefinitionOptions
import io.zyxn.lsp.DiagnosticOptions
import io.zyxn.lsp.DiagnosticRegistrationOptions
import io.zyxn.lsp.DocumentColorOptions
import io.zyxn.lsp.DocumentColorRegistrationOptions
import io.zyxn.lsp.DocumentFormattingOptions
import io.zyxn.lsp.DocumentHighlightOptions
import io.zyxn.lsp.DocumentLinkOptions
import io.zyxn.lsp.DocumentOnTypeFormattingOptions
import io.zyxn.lsp.DocumentRangeFormattingOptions
import io.zyxn.lsp.DocumentSymbolOptions
import io.zyxn.lsp.ExecuteCommandOptions
import io.zyxn.lsp.FoldingRangeOptions
import io.zyxn.lsp.FoldingRangeRegistrationOptions
import io.zyxn.lsp.HoverOptions
import io.zyxn.lsp.ImplementationOptions
import io.zyxn.lsp.ImplementationRegistrationOptions
import io.zyxn.lsp.InlayHintOptions
import io.zyxn.lsp.InlayHintRegistrationOptions
import io.zyxn.lsp.InlineCompletionOptions
import io.zyxn.lsp.InlineValueOptions
import io.zyxn.lsp.InlineValueRegistrationOptions
import io.zyxn.lsp.LinkedEditingRangeOptions
import io.zyxn.lsp.LinkedEditingRangeRegistrationOptions
import io.zyxn.lsp.MonikerOptions
import io.zyxn.lsp.MonikerRegistrationOptions
import io.zyxn.lsp.NotebookDocumentSyncOptions
import io.zyxn.lsp.NotebookDocumentSyncRegistrationOptions
import io.zyxn.lsp.PositionEncodingKind
import io.zyxn.lsp.ReferenceOptions
import io.zyxn.lsp.RenameOptions
import io.zyxn.lsp.SelectionRangeOptions
import io.zyxn.lsp.SelectionRangeRegistrationOptions
import io.zyxn.lsp.SemanticTokensOptions
import io.zyxn.lsp.SemanticTokensRegistrationOptions
import io.zyxn.lsp.SignatureHelpOptions
import io.zyxn.lsp.TextDocumentSyncKind
import io.zyxn.lsp.TextDocumentSyncOptions
import io.zyxn.lsp.TypeDefinitionOptions
import io.zyxn.lsp.TypeDefinitionRegistrationOptions
import io.zyxn.lsp.TypeHierarchyOptions
import io.zyxn.lsp.TypeHierarchyRegistrationOptions
import io.zyxn.lsp.WorkspaceSymbolOptions
import io.zyxn.lsp.types.LSPAny
import io.zyxn.lsp.types.OneOf
import io.zyxn.lsp.types.OneOfThree
import kotlinx.serialization.Serializable

/**
 * [LSP Specification](https://microsoft.github.io/language-server-protocol/specifications/lsp/3.18/specification/#serverCapabilities)
 */
@Serializable
data class ServerCapabilities(
    /**
     * The position encoding the server picked from the encodings offered
     * by the client via the client capability `general.positionEncodings`.
     *
     * If the client didn't provide any position encodings the only valid
     * value that a server can return is [PositionEncodingKind.UTF16].
     *
     * If omitted it defaults to [PositionEncodingKind.UTF16].
     *
     * @since 3.17.0
     */
    val positionEncoding: PositionEncodingKind?,

    /**
     * Defines how text documents are synced. Is either a detailed structure
     * defining each notification or for backwards compatibility the
     * TextDocumentSyncKind number. If omitted it defaults to
     * [TextDocumentSyncKind.None].
     */
    val textDocumentSync: OneOf<TextDocumentSyncOptions, TextDocumentSyncKind>?,

    /**
     * Defines how notebook documents are synced.
     *
     * @since 3.17.0
     */
    val notebookDocumentSync: OneOf<NotebookDocumentSyncOptions, NotebookDocumentSyncRegistrationOptions>?,

    /**
     * The server provides completion support.
     */
    val completionProvider: CompletionOptions?,

    /**
     * The server provides hover support.
     */
    val hoverProvider: OneOf<Boolean, HoverOptions>?,

    /**
     * The server provides signature help support.
     */
    val signatureHelpProvider: SignatureHelpOptions?,

    /**
     * The server provides go to declaration support.
     *
     * @since 3.14.0
     */
    val declarationProvider: OneOfThree<Boolean, DeclarationOptions, DeclarationRegistrationOptions>?,

    /**
     * The server provides goto definition support.
     */
    val definitionProvider: OneOf<Boolean, DefinitionOptions>?,

    /**
     * The server provides goto type definition support.
     *
     * @since 3.6.0
     */
    val typeDefinitionProvider: OneOfThree<Boolean, TypeDefinitionOptions, TypeDefinitionRegistrationOptions>?,

    /**
     * The server provides goto implementation support.
     *
     * @since 3.6.0
     */
    val implementationProvider: OneOfThree<Boolean, ImplementationOptions, ImplementationRegistrationOptions>?,

    /**
     * The server provides find references support.
     */
    val referencesProvider: OneOf<Boolean, ReferenceOptions>?,

    /**
     * The server provides document highlight support.
     */
    val documentHighlightProvider: OneOf<Boolean, DocumentHighlightOptions>?,

    /**
     * The server provides document symbol support.
     */
    val documentSymbolProvider: OneOf<Boolean, DocumentSymbolOptions>?,

    /**
     * The server provides code actions. The `CodeActionOptions` return type is
     * only valid if the client signals code action literal support via the
     * property `textDocument.codeAction.codeActionLiteralSupport`.
     */
    val codeActionProvider: OneOf<Boolean, CodeActionOptions>?,

    /**
     * The server provides code lens.
     */
    val codeLensProvider: CodeLensOptions?,

    /**
     * The server provides document link support.
     */
    val documentLinkProvider: DocumentLinkOptions?,

    /**
     * The server provides color provider support.
     *
     * @since 3.6.0
     */
    val colorProvider: OneOfThree<Boolean, DocumentColorOptions, DocumentColorRegistrationOptions>?,

    /**
     * The server provides document formatting.
     */
    val documentFormattingProvider: OneOf<Boolean, DocumentFormattingOptions>?,

    /**
     * The server provides document range formatting.
     */
    val documentRangeFormattingProvider: OneOf<Boolean, DocumentRangeFormattingOptions>?,

    /**
     * The server provides document formatting on typing.
     */
    val documentOnTypeFormattingProvider: DocumentOnTypeFormattingOptions?,

    /**
     * The server provides rename support. RenameOptions may only be
     * specified if the client states that it supports
     * `prepareSupport` in its initial `initialize` request.
     */
    val renameProvider: OneOf<Boolean, RenameOptions>?,

    /**
     * The server provides folding provider support.
     *
     * @since 3.10.0
     */
    val foldingRangeProvider: OneOfThree<Boolean, FoldingRangeOptions, FoldingRangeRegistrationOptions>?,

    /**
     * The server provides execute command support.
     */
    val executeCommandProvider: ExecuteCommandOptions?,

    /**
     * The server provides selection range support.
     *
     * @since 3.15.0
     */
    val selectionRangeProvider: OneOfThree<Boolean, SelectionRangeOptions, SelectionRangeRegistrationOptions>?,

    /**
     * The server provides linked editing range support.
     *
     * @since 3.16.0
     */
    val linkedEditingRangeProvider: OneOfThree<Boolean, LinkedEditingRangeOptions, LinkedEditingRangeRegistrationOptions>?,

    /**
     * The server provides call hierarchy support.
     *
     * @since 3.16.0
     */
    val callHierarchyProvider: OneOfThree<Boolean, CallHierarchyOptions, CallHierarchyRegistrationOptions>?,

    /**
     * The server provides semantic tokens support.
     *
     * @since 3.16.0
     */
    val semanticTokensProvider: OneOf<SemanticTokensOptions, SemanticTokensRegistrationOptions>?,

    /**
     * Whether server provides moniker support.
     *
     * @since 3.16.0
     */
    val monikerProvider: OneOfThree<Boolean, MonikerOptions, MonikerRegistrationOptions>?,

    /**
     * The server provides type hierarchy support.
     *
     * @since 3.17.0
     */
    val typeHierarchyProvider: OneOfThree<Boolean, TypeHierarchyOptions, TypeHierarchyRegistrationOptions>?,

    /**
     * The server provides inline values.
     *
     * @since 3.17.0
     */
    val inlineValueProvider: OneOfThree<Boolean, InlineValueOptions, InlineValueRegistrationOptions>?,

    /**
     * The server provides inlay hints.
     *
     * @since 3.17.0
     */
    val inlayHintProvider: OneOfThree<Boolean, InlayHintOptions, InlayHintRegistrationOptions>?,

    /**
     * The server has support for pull model diagnostics.
     *
     * @since 3.17.0
     */
    val diagnosticProvider: OneOf<DiagnosticOptions, DiagnosticRegistrationOptions>?,

    /**
     * The server provides workspace symbol support.
     */
    val workspaceSymbolProvider: OneOf<Boolean, WorkspaceSymbolOptions>?,

    /**
     * The server provides inline completions.
     *
     * @since 3.18.0
     */
    val inlineCompletionProvider: OneOf<Boolean, InlineCompletionOptions>?,

    /**
     * Text document specific server capabilities.
     *
     * @since 3.18.0
     */
    val textDocument: TextDocumentServerCapabilities?,

    /**
     * Workspace specific server capabilities
     */
    val workspace: WorkspaceServerCapabilities?,

    /**
     * Experimental server capabilities.
     */
    val experimental: LSPAny?
)
