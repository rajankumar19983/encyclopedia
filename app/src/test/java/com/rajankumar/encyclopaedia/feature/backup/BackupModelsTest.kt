package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupModelsTest {
  @Test
  fun totalRecordsIncludesEveryBackedUpTable() {
    val manifest = BackupManifest(
      createdAt = 1L,
      knowledgeNodeCount = 2,
      lessonCount = 3,
      questionCount = 5,
      questionTopicCount = 7,
      attemptCount = 11,
      plannerTaskCount = 13
    )

    assertEquals(41, manifest.totalRecords)
    assertEquals(BACKUP_FORMAT_VERSION, manifest.formatVersion)
  }
}
