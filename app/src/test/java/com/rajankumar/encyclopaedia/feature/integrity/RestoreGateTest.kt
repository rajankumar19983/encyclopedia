package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupManifest
import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot
import org.junit.Assert.*
import org.junit.Test

class RestoreGateTest {
  @Test fun acceptsEmptyConsistentSnapshot() {
    val snapshot = BackupSnapshot(BackupManifest(createdAt = 1L))
    assertTrue(snapshot.passesRestoreIntegrityGate())
  }

  @Test(expected = IllegalArgumentException::class)
  fun requireRejectsInvalidManifest() {
    BackupSnapshot(BackupManifest(createdAt = 1L, questionCount = 1)).requireRestoreIntegrity()
  }
}
