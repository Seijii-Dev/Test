package io.zyxn.data.repository

import io.zyxn.data.database.PluginTabDao
import io.zyxn.data.database.PluginTabEntity
import org.koin.core.annotation.Single

@Single
class PluginTabRepository(private val dao: PluginTabDao) {

    suspend fun getPluginTabs(): List<PluginTabEntity> = dao.getPluginTabs()

    suspend fun saveTab(tabId: String, pluginId: String, title: String, metadata: String? = null) {
        dao.insert(
            PluginTabEntity(
                tabId = tabId,
                pluginId = pluginId,
                title = title,
                metadata = metadata,
                lastOpened = System.currentTimeMillis()
            )
        )
    }

    suspend fun removeTab(tabId: String) = dao.deleteByTabId(tabId)

    suspend fun removeAll() = dao.clear()
}
