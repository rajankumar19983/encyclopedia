package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRetentionTest {
  private fun point(
    index: Int,
    valid: Boolean = true
  ) = BackupRestorePoint(
    name = "backup-$index",
    createdAt = index.toLong(),
    type = BackupType.AUTOMATIC,
    destination = BackupDestination.DEVICE,
    valid = valid
  )

  @Test
  fun keepLatestReturnsFiveNewestValidRestorePoints() {
    val points = (1..7).map(::point) + point(8, valid = false)

    val kept = BackupRetention.keepLatest(points)

    assertEquals(listOf("backup-7", "backup-6", "backup-5", "backup-4", "backup-3"), kept.map { it.name })
    assertFalse(kept.any { it.name == "backup-8" })
  }

  @Test
  fun removableOnlyContainsValidPointsOutsideRetentionWindow() {
    val points = (1..7).map(::point) + point(8, valid = false)

    val removable = BackupRetention.removableAfterSuccessfulBackup(points)

    assertEquals(setOf("backup-1", "backup-2"), removable.map { it.name }.toSet())
    assertTrue(removable.all { it.valid })
  }

  @Test
  fun retentionCanUseSmallerExplicitLimit() {
    val kept = BackupRetention.keepLatest((1..4).map(::point), limit = 2)

    assertEquals(listOf("backup-4", "backup-3"), kept.map { it.name })
  }
}
