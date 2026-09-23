package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRecordDescriptionTest {
  private fun manifest(q: Int, strokes: Int) = BackupManifest(createdAt=1, knowledgeNodeCount=0, lessonCount=0, questionCount=q, questionTopicCount=0, attemptCount=0, plannerTaskCount=0, notebookStrokeCount=strokes)
  @Test fun emptyBackupIsClear() = assertEquals("Empty backup", manifest(0,0).recordDescription())
  @Test fun populatedBackupSeparatesContent() = assertTrue(manifest(3,4).recordDescription().contains("3 study records • 4 notebook records"))
}
