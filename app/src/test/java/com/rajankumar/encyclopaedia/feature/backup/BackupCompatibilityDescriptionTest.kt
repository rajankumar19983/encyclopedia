package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.*
import org.junit.Test

class BackupCompatibilityDescriptionTest {
  @Test fun everyCompatibilityHasReadableDescription() {
    BackupCompatibility.entries.forEach {
      assertTrue(it.description().isNotBlank())
      assertTrue(it.description().endsWith("."))
    }
  }
}
