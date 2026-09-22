package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity
import com.rajankumar.encyclopaedia.data.local.NotebookStrokeEntity
import org.junit.Assert.assertFalse
import org.junit.Test

class BackupNotebookFieldIntegrityTest {
  private fun snapshot(
    pages: List<NotebookPageEntity> = emptyList(),
    strokes: List<NotebookStrokeEntity> = emptyList()
  ) = BackupSnapshot(
    BackupManifest(createdAt = 1, knowledgeNodeCount = 0, lessonCount = 0, questionCount = 0, questionTopicCount = 0, attemptCount = 0, plannerTaskCount = 0, notebookPageCount = pages.size, notebookStrokeCount = strokes.size),
    emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), pages, emptyList(), strokes
  )

  @Test fun rejectsInvalidNotebookPageGeometry() {
    assertFalse(snapshot(pages = listOf(NotebookPageEntity("p", "Page", pageWidth = 1f))).hasValidNotebookFields())
  }

  @Test fun rejectsUnsupportedStrokeTool() {
    assertFalse(snapshot(strokes = listOf(NotebookStrokeEntity("s", "l", "[]", tool = "SPRAY"))).hasValidNotebookFields())
  }
}
