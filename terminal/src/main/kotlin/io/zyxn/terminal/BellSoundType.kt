package io.zyxn.terminal

import kotlinx.serialization.Serializable

@Serializable
enum class BellSoundType {
    System,
    Gentle,
    VisualOnly
}
