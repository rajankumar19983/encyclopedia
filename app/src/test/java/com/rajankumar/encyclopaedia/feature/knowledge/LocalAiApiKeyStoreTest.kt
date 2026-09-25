package com.rajankumar.encyclopaedia.feature.knowledge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class LocalAiApiKeyStoreTest {
  private lateinit var store: LocalAiApiKeyStore

  @Before
  fun setUp() {
    store = LocalAiApiKeyStore(RuntimeEnvironment.getApplication())
    store.clearApiKey()
  }

  @Test
  fun savesTrimmedKey() {
    store.saveApiKey("  sk-test-key  ")

    assertEquals("sk-test-key", store.getApiKey())
    assertTrue(store.hasApiKey())
  }

  @Test
  fun clearsKey() {
    store.saveApiKey("sk-test-key")
    store.clearApiKey()

    assertNull(store.getApiKey())
    assertFalse(store.hasApiKey())
  }

  @Test
  fun rejectsBlankKey() {
    assertThrows(IllegalArgumentException::class.java) {
      store.saveApiKey("   ")
    }
  }
}
