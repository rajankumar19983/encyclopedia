package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BackupRestoreWarningAcknowledgementTextTest {
  @Test fun textOnlyAppearsWhenAcknowledgementIsPending() {
    assertEquals("Review every warning before enabling restore.", BackupRestoreReviewSessionPresentation("", "", emptyList(), "", false, true).warningAcknowledgementText())
    assertNull(BackupRestoreReviewSessionPresentation("", "", emptyList(), "", true, false).warningAcknowledgementText())
  }
}
