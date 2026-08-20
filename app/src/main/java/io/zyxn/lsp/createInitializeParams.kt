package io.zyxn.lsp

import android.os.Process
import io.zyxn.BuildConfig
import io.zyxn.lsp.capabilities.ClientCapabilities
import io.zyxn.lsp.capabilities.CodeActionCapabilities
import io.zyxn.lsp.capabilities.CodeActionKindCapabilities
import io.zyxn.lsp.capabilities.CodeActionLiteralSupportCapabilities
import io.zyxn.lsp.capabilities.CompletionCapabilities
import io.zyxn.lsp.capabilities.CompletionItemCapabilities
import io.zyxn.lsp.capabilities.DefinitionCapabilities
import io.zyxn.lsp.capabilities.DiagnosticClientCapabilities
import io.zyxn.lsp.capabilities.DiagnosticWorkspaceClientCapabilities
import io.zyxn.lsp.capabilities.DidChangeConfigurationCapabilities
import io.zyxn.lsp.capabilities.DidChangeWatchedFilesCapabilities
import io.zyxn.lsp.capabilities.DocumentSymbolCapabilities
import io.zyxn.lsp.capabilities.ExecuteCommandCapabilities
import io.zyxn.lsp.capabilities.FormattingCapabilities
import io.zyxn.lsp.capabilities.HoverCapabilities
import io.zyxn.lsp.capabilities.InlayHintClientCapabilities
import io.zyxn.lsp.capabilities.MessageActionItemCapabilities
import io.zyxn.lsp.capabilities.OnTypeFormattingCapabilities
import io.zyxn.lsp.capabilities.ParameterInformationCapabilities
import io.zyxn.lsp.capabilities.PublishDiagnosticsCapabilities
import io.zyxn.lsp.capabilities.RangeFormattingCapabilities
import io.zyxn.lsp.capabilities.RenameCapabilities
import io.zyxn.lsp.capabilities.ShowDocumentCapabilities
import io.zyxn.lsp.capabilities.ShowMessageRequestCapabilities
import io.zyxn.lsp.capabilities.SignatureHelpCapabilities
import io.zyxn.lsp.capabilities.SignatureInformationCapabilities
import io.zyxn.lsp.capabilities.SymbolKindCapabilities
import io.zyxn.lsp.capabilities.SynchronizationCapabilities
import io.zyxn.lsp.capabilities.TextDocumentClientCapabilities
import io.zyxn.lsp.capabilities.WindowClientCapabilities
import io.zyxn.lsp.capabilities.WorkspaceClientCapabilities
import io.zyxn.lsp.capabilities.WorkspaceEditCapabilities
import io.zyxn.lsp.capabilities.WorkspaceSymbolCapabilities
import io.zyxn.lsp.types.LSPAny
import java.io.File

internal fun createInitializeParams(
    project: File?,
    initializationOptions: LSPAny? = null
): InitializeParams {
    return InitializeParams(
        processId = Process.myPid(),
        clientInfo = ClientInfo("Zyxn", BuildConfig.VERSION_NAME),
        capabilities = ClientCapabilities(
            textDocument = TextDocumentClientCapabilities(
                synchronization = SynchronizationCapabilities(
                    dynamicRegistration = true,
                    willSave = true,
                    willSaveWaitUntil = true,
                    didSave = true
                ),
                codeAction = CodeActionCapabilities(
                    dynamicRegistration = true,
                    isPreferredSupport = true,
                    codeActionLiteralSupport = CodeActionLiteralSupportCapabilities(
                        codeActionKind = CodeActionKindCapabilities(
                            listOf(
                                CodeActionKind.QuickFix,
                                CodeActionKind.Refactor,
                                CodeActionKind.RefactorInline,
                                CodeActionKind.RefactorExtract,
                                CodeActionKind.RefactorRewrite,
                                CodeActionKind.Source,
                                CodeActionKind.SourceOrganizeImports,
                                CodeActionKind.SourceFixAll
                            )
                        )
                    )
                ),
                completion = CompletionCapabilities(
                    dynamicRegistration = true,
                    completionItem = CompletionItemCapabilities(
                        snippetSupport = true,
                        commitCharactersSupport = true,
                        documentationFormat = listOf(MarkupKind.Markdown, MarkupKind.PlainText),
                        deprecatedSupport = true,
                        preselectSupport = true
                    )
                ),
                hover = HoverCapabilities(
                    dynamicRegistration = true,
                    contentFormat = listOf(MarkupKind.Markdown, MarkupKind.PlainText)
                ),
                signatureHelp = SignatureHelpCapabilities(
                    dynamicRegistration = true,
                    signatureInformation = SignatureInformationCapabilities(
                        documentationFormat = listOf(MarkupKind.Markdown, MarkupKind.PlainText),
                        parameterInformation = ParameterInformationCapabilities(labelOffsetSupport = true)
                    ),
                    contextSupport = true
                ),
                definition = DefinitionCapabilities(dynamicRegistration = true),
                documentSymbol = DocumentSymbolCapabilities(
                    dynamicRegistration = true,
                    symbolKind = SymbolKindCapabilities(valueSet = SymbolKind.entries)
                ),
                formatting = FormattingCapabilities(dynamicRegistration = true),
                rangeFormatting = RangeFormattingCapabilities(dynamicRegistration = true),
                onTypeFormatting = OnTypeFormattingCapabilities(dynamicRegistration = true),
                rename = RenameCapabilities(
                    dynamicRegistration = true,
                    prepareSupport = true
                ),
                publishDiagnostics = PublishDiagnosticsCapabilities(
                    relatedInformation = true,
                    versionSupport = true
                ),
                inlayHint = InlayHintClientCapabilities(dynamicRegistration = true),
                diagnostic = DiagnosticClientCapabilities(
                    relatedDocumentSupport = true,
                    relatedInformation = true
                )
            ),
            workspace = WorkspaceClientCapabilities(
                applyEdit = true,
                workspaceEdit = WorkspaceEditCapabilities(
                    documentChanges = true,
                    resourceOperations = listOf(
                        ResourceOperationKind.Create,
                        ResourceOperationKind.Rename,
                        ResourceOperationKind.Delete
                    ),
                    failureHandling = FailureHandlingKind.TextOnlyTransactional
                ),
                didChangeConfiguration = DidChangeConfigurationCapabilities(dynamicRegistration = true),
                didChangeWatchedFiles = DidChangeWatchedFilesCapabilities(dynamicRegistration = true),
                symbol = WorkspaceSymbolCapabilities(
                    dynamicRegistration = true,
                    symbolKind = SymbolKindCapabilities(valueSet = SymbolKind.entries)
                ),
                executeCommand = ExecuteCommandCapabilities(dynamicRegistration = true),
                diagnostics = DiagnosticWorkspaceClientCapabilities(refreshSupport = true)
            ),
            window = WindowClientCapabilities(
                showMessage = ShowMessageRequestCapabilities(
                    messageActionItem = MessageActionItemCapabilities(additionalPropertiesSupport = true)
                ),
                showDocument = ShowDocumentCapabilities(support = true),
                workDoneProgress = true
            )
        ),
        initializationOptions = initializationOptions
    ).apply {
        if (project != null) {
            @Suppress("DEPRECATION")
            rootUri = project.toLspUri()
            workspaceFolders = listOf(
                WorkspaceFolder(
                    uri = project.toLspUri(),
                    name = project.name
                )
            )
        }
    }
}
