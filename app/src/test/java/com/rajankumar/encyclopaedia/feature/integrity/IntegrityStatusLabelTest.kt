package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertTrue
import org.junit.Test

class IntegrityStatusLabelTest {
  @Test
  fun everyStatusHasReadableLabel() {
    IntegrityStatus.entries.forEach { status ->
      assertTrue(status.label().isNotBlank())
    }
  }
}
