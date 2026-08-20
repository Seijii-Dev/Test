package io.zyxn.ui.provider

import androidx.compose.runtime.staticCompositionLocalOf
import io.zyxn.core.event.EventBus

val LocalEventBus = staticCompositionLocalOf<EventBus> {
    error("No LocalEventBus provided")
}
