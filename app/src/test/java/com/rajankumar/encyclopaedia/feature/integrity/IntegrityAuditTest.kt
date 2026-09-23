package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.*
import org.junit.Assert.*
import org.junit.Test

class IntegrityAuditTest {
  private fun snapshot(manifest: BackupManifest) = BackupSnapshot(manifest, emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
  @Test fun cleanEmptySnapshotHasNoIssues() = assertTrue(snapshot(BackupManifest(1L, 0, 0, 0, 0, 0, 0)).integrityIssues().isEmpty())
  @Test fun countMismatchReportsManifestIssue() = assertTrue(IntegrityIssue.MANIFEST in snapshot(BackupManifest(1L, 0, 0, 1, 0, 0, 0)).integrityIssues())
}
