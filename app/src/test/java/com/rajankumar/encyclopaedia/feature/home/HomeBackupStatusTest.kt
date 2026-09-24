package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeBackupStatusTest {
  @Test fun labelsBackupStatus() { assertEquals("No backup yet", homeBackupStatusLabel(null)); assertEquals("Backup current", homeBackupStatusLabel(0)); assertEquals("Backup 3d ago", homeBackupStatusLabel(3)); assertEquals("Backup recommended", homeBackupStatusLabel(10)) }
}
