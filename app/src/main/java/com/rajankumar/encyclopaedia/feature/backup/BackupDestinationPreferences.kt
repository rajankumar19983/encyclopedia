package com.rajankumar.encyclopaedia.feature.backup

import android.content.Context

/**
 * Persists where the user wants portable backups written. Device storage is
 * always available once a folder is selected; Drive becomes selectable after
 * its account/storage adapter is connected.
 */
class BackupDestinationPreferences(context: Context) {
  private val preferences = context.applicationContext.getSharedPreferences(
    PREFERENCES_NAME,
    Context.MODE_PRIVATE
  )

  fun selected(): BackupDestination = runCatching {
    BackupDestination.valueOf(
      preferences.getString(DESTINATION_KEY, BackupDestination.DEVICE.name)
        ?: BackupDestination.DEVICE.name
    )
  }.getOrDefault(BackupDestination.DEVICE)

  fun select(destination: BackupDestination) {
    preferences.edit().putString(DESTINATION_KEY, destination.name).apply()
  }

  companion object {
    private const val PREFERENCES_NAME = "backup_storage"
    private const val DESTINATION_KEY = "backup_destination"
  }
}
