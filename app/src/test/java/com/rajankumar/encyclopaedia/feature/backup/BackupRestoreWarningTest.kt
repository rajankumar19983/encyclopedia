package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreWarningTest {
  private fun manifest(q: Int) = BackupManifest(createdAt=1, knowledgeNodeCount=0, lessonCount=0, questionCount=q, questionTopicCount=0, attemptCount=0, plannerTaskCount=0)
  @Test fun populatedRestoreExplainsReplacement() = assertTrue(restoreReplacementWarning(manifest(4)).contains("replace local study data"))
  @Test fun emptyRestoreWarnsAboutEmptyLibrary() = assertTrue(restoreReplacementWarning(manifest(0)).contains("empty"))
}
