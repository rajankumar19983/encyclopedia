package com.rajankumar.encyclopaedia.feature.teacher

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Stores the user's OpenAI API key encrypted with a non-exportable Android
 * Keystore key. The encrypted payload is kept in no-backup app storage so the
 * credential is deliberately excluded from Android and Encyclopaedia backups.
 */
class OpenAiApiKeyStore(context: Context) {
  private val preferences = context.applicationContext
    .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

  fun save(apiKey: String) {
    val normalized = apiKey.trim()
    require(normalized.isNotEmpty()) { "API key cannot be blank" }

    val cipher = Cipher.getInstance(TRANSFORMATION)
    cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
    val encrypted = cipher.doFinal(normalized.toByteArray(Charsets.UTF_8))

    preferences.edit()
      .putString(ENCRYPTED_KEY, Base64.encodeToString(encrypted, Base64.NO_WRAP))
      .putString(IV_KEY, Base64.encodeToString(cipher.iv, Base64.NO_WRAP))
      .apply()
  }

  fun read(): String? {
    val encrypted = preferences.getString(ENCRYPTED_KEY, null) ?: return null
    val iv = preferences.getString(IV_KEY, null) ?: return null

    return runCatching {
      val cipher = Cipher.getInstance(TRANSFORMATION)
      cipher.init(
        Cipher.DECRYPT_MODE,
        getExistingKey(),
        GCMParameterSpec(128, Base64.decode(iv, Base64.NO_WRAP))
      )
      String(
        cipher.doFinal(Base64.decode(encrypted, Base64.NO_WRAP)),
        Charsets.UTF_8
      )
    }.getOrNull()
  }

  fun hasKey(): Boolean = read() != null

  fun remove() {
    preferences.edit().clear().apply()
    val keyStore = keyStore()
    if (keyStore.containsAlias(KEY_ALIAS)) {
      keyStore.deleteEntry(KEY_ALIAS)
    }
  }

  private fun getOrCreateKey(): SecretKey {
    getExistingKeyOrNull()?.let { return it }

    val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
    generator.init(
      KeyGenParameterSpec.Builder(
        KEY_ALIAS,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
      )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .setRandomizedEncryptionRequired(true)
        .build()
    )
    return generator.generateKey()
  }

  private fun getExistingKey(): SecretKey = getExistingKeyOrNull()
    ?: error("OpenAI credential encryption key is unavailable")

  private fun getExistingKeyOrNull(): SecretKey? =
    keyStore().getKey(KEY_ALIAS, null) as? SecretKey

  private fun keyStore(): KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

  companion object {
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "encyclopaedia_openai_api_key"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val PREFERENCES_NAME = "openai_secure_credentials"
    private const val ENCRYPTED_KEY = "encrypted_api_key"
    private const val IV_KEY = "api_key_iv"
  }
}
