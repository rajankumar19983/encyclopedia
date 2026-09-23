package com.rajankumar.encyclopaedia.feature.backup

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.IOException

/**
 * Stores portable backup files in a user-selected Storage Access Framework
 * directory. The persisted tree permission lets backups survive app uninstall;
 * the files are not kept in disposable app-private storage.
 */
class DeviceBackupStore(
  private val context: Context
) {
  private val preferences = context.applicationContext.getSharedPreferences(
    PREFERENCES_NAME,
    Context.MODE_PRIVATE
  )

  fun configuredDirectory(): Uri? = preferences.getString(DIRECTORY_KEY, null)?.let(Uri::parse)

  fun setDirectory(uri: Uri) {
    context.contentResolver.takePersistableUriPermission(
      uri,
      android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
        android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    )
    preferences.edit().putString(DIRECTORY_KEY, uri.toString()).apply()
  }

  fun clearDirectory() {
    preferences.edit().remove(DIRECTORY_KEY).apply()
  }

  fun write(name: String, bytes: ByteArray): Uri {
    require(isSupportedBackupFileName(name)) { "Unsupported Encyclopaedia backup filename" }
    val directory = requireDirectory()
    val file = directory.createFile(MIME_TYPE, name)
      ?: throw IOException("Unable to create backup file")
    try {
      context.contentResolver.openOutputStream(file.uri, "w")?.use { output ->
        output.write(bytes)
        output.flush()
      } ?: throw IOException("Unable to open backup output stream")
    } catch (error: Throwable) {
      file.delete()
      throw error
    }
    return file.uri
  }

  fun read(uri: Uri): ByteArray = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
    ?: throw IOException("Unable to read backup")

  fun discover(): List<StoredBackupFile> = requireDirectory().listFiles()
    .filter { file -> file.isFile && isSupportedBackupFileName(file.name.orEmpty()) }
    .mapNotNull { file ->
      val name = file.name ?: return@mapNotNull null
      StoredBackupFile(name, file.uri, file.lastModified(), file.length())
    }
    .sortedByDescending(StoredBackupFile::lastModified)

  fun delete(uri: Uri): Boolean {
    val directory = requireDirectory()
    val file = directory.listFiles().firstOrNull { it.uri == uri } ?: return false
    return file.delete()
  }

  fun deleteByName(name: String): Boolean {
    require(isSupportedBackupFileName(name)) { "Unsupported Encyclopaedia backup filename" }
    val file = requireDirectory().listFiles().firstOrNull { it.name == name } ?: return false
    return file.delete()
  }

  fun deleteAllBackups(): Int {
    val files = requireDirectory().listFiles()
      .filter { it.isFile && isSupportedBackupFileName(it.name.orEmpty()) }
    return files.count { it.delete() }
  }

  private fun requireDirectory(): DocumentFile {
    val uri = configuredDirectory() ?: error("Backup directory has not been selected")
    return DocumentFile.fromTreeUri(context, uri)
      ?.takeIf { it.exists() && it.isDirectory }
      ?: error("Configured backup directory is unavailable")
  }

  companion object {
    private const val PREFERENCES_NAME = "backup_storage"
    private const val DIRECTORY_KEY = "device_backup_directory"
    private const val MIME_TYPE = "application/octet-stream"
  }
}

data class StoredBackupFile(
  val name: String,
  val uri: Uri,
  val lastModified: Long,
  val sizeBytes: Long
)
