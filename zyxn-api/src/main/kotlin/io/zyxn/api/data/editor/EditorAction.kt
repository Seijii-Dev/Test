package io.zyxn.api.data.editor

import io.zyxn.api.data.file.KxFile

sealed interface EditorAction

data class Save(val file: KxFile) : EditorAction
data class SaveAs(val oldTabId: String, val newFile: KxFile) : EditorAction
