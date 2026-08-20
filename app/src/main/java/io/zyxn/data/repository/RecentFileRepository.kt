package io.zyxn.data.repository

import android.net.Uri
import io.zyxn.data.database.RecentFileDao
import io.zyxn.data.database.RecentFileEntity
import io.zyxn.api.data.file.KxFile
import org.koin.core.annotation.Single

@Single
class RecentFileRepository(
    private val dao: RecentFileDao
) {
    fun observeRecentFiles() = dao.observeRecentFiles()

    suspend fun getRecentFiles() = dao.getRecentFiles()

    suspend fun addRecentFile(file: KxFile, projectUri: Uri? = null) {
        dao.insert(
            RecentFileEntity(
                uri = file.uri.toString(),
                name = file.name,
                projectUri = projectUri?.toString(),
                lastOpened = System.currentTimeMillis()
            )
        )
    }

    suspend fun clearAll() = dao.clear()

    suspend fun removeFile(file: KxFile) = dao.deleteByUri(file.uri.toString())
    suspend fun removeByUri(uri: Uri) = dao.deleteByUri(uri.toString())
}
