package io.zyxn.api.event

import io.zyxn.api.InternalZyxnApi
import io.zyxn.api.event.editor.FileOpenedEvent
import io.zyxn.api.plugin.PluginContext
import io.zyxn.core.Global
import io.zyxn.core.event.EventBus

/**
 * A [Global] wrapper that exposes the application's [EventBus] to plugins.
 *
 * Registered by the host during startup; plugins reach the bus through [PluginContext.eventBus].
 */
@InternalZyxnApi
class EventBusHolder(val bus: EventBus) : Global

/**
 * The application-wide [EventBus].
 *
 * Plugins can publish their own events and subscribe to built-in ones (such as
 * [FileOpenedEvent]) through this bus.
 *
 * ### Example
 * ```kotlin
 * suspend fun watchOpens() {
 *     currentPluginContext().eventBus.subscribe<FileOpenedEvent> { event ->
 *         println("Opened ${event.fileName}")
 *     }
 * }
 * ```
 */
@OptIn(InternalZyxnApi::class)
val PluginContext.eventBus: EventBus
    get() = app.global<EventBusHolder>().bus
