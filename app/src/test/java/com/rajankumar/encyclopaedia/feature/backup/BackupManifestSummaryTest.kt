package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupManifestSummaryTest {
  @Test fun separatesNotebookRecords() {
    val manifest = BackupManifest(createdAt=1, knowledgeNodeCount=2, lessonCount=3, questionCount=4, questionTopicCount=5, attemptCount=6, plannerTaskCount=7, notebookPageCount=1, notebookLayerCount=2, notebookStrokeCount=8)
    val summary = manifest.summary()
    assertEquals(38, summary.records)
    assertEquals(27, summary.studyRecords)
    assertEquals(11, summary.notebookRecords)
  }
}
