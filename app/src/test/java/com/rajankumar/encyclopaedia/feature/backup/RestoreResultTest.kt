package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class RestoreResultTest {
  @Test fun labelsRestoreResults() { assertEquals("Restore complete", RestoreResult.SUCCESS.label()); assertEquals("Restore failed", RestoreResult.FAILED.label()); assertEquals("Restore cancelled", RestoreResult.CANCELLED.label()) }
}
