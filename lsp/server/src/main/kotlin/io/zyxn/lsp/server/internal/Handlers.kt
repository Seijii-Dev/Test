package io.zyxn.lsp.server.internal

import io.zyxn.lsp.NotificationMessage
import io.zyxn.lsp.RequestId
import io.zyxn.lsp.RequestMessage
import io.zyxn.lsp.ResponseMessage

internal typealias ResponseHandler = suspend (ResponseMessage) -> Unit
internal typealias RequestHandler = suspend (RequestMessage) -> ResponseMessage
internal typealias NotificationHandler = suspend (RequestId?, NotificationMessage) -> Unit
