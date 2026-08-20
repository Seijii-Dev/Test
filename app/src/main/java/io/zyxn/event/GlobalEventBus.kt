package io.zyxn.event

import io.zyxn.core.App
import io.zyxn.core.Global
import io.zyxn.core.event.EventBus
import io.zyxn.core.event.eventBus
import io.zyxn.core.unsafe.GlobalApp
import io.zyxn.core.unsafe.UnsafeGlobalAccess

private class EventBusGlobal(val bus: EventBus) : Global

@OptIn(UnsafeGlobalAccess::class)
val GlobalEventBus by lazy { GlobalApp.eventBus() }

/**
 * Retrieves the global [EventBus] instance from the [App] registry.
 *
 * @return The registered [EventBus].
 */
fun App.eventBus() = global<EventBusGlobal>().bus

/**
 * Initializes and registers the global [EventBus] into the provided [App] instance.
 * The bus's coroutine lifecycle is tied to the [App.backgroundScope].
 *
 * @param app The application instance where the global bus will be registered.
 */
fun initializeGlobalEventBus(app: App) {
    app.setGlobal(EventBusGlobal(eventBus(app.backgroundScope)))
}
