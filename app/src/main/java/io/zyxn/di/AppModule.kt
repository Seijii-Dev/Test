package io.zyxn.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import io.zyxn.BuildConfig
import io.zyxn.data.database.ZyxnDatabase
import io.zyxn.data.database.MIGRATION_1_2
import io.zyxn.data.database.MIGRATION_2_3
import io.zyxn.data.preferences.SettingsDataStore
import io.zyxn.data.preferences.dataStore
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@ComponentScan("io.zyxn")
object AppModule

@Singleton
fun provideContentResolver(context: Context): ContentResolver = context.contentResolver

@Singleton
fun provideAppDatabase(context: Context) = Room
    .databaseBuilder(
        context = context.applicationContext,
        klass = ZyxnDatabase::class.java,
        name = "zyxn_database"
    )
    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
    .apply {
        if (BuildConfig.DEBUG) {
            fallbackToDestructiveMigration(dropAllTables = true)
        }
    }
    .build()

@Singleton
fun provideRecentFileDao(db: ZyxnDatabase) = db.recentFileDao()

@Singleton
fun provideRecentProjectDao(db: ZyxnDatabase) = db.recentProjectDao()

@Singleton
fun providePluginTabDao(db: ZyxnDatabase) = db.pluginTabDao()

@Singleton
fun provideAppPreferences(context: Context): SettingsDataStore = context.dataStore
