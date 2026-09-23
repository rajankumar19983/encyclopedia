package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.NotebookLayerEntity
import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity
import com.rajankumar.encyclopaedia.data.local.NotebookStrokeEntity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupNotebookIntegrityTest {
  private fun snapshot(
    pages: List<NotebookPageEntity> = emptyList(),
    layers: List<NotebookLayerEntity> = emptyList(),
    strokes: List<NotebookStrokeEntity> = emptyList()
  ) = BackupSnapshot(
    BackupManifest(createdAt = 1, knowledgeNodeCount = 0, lessonCount = 0, questionCount = 0, questionTopicCount = 0, attemptCount = 0, plannerTaskCount = 0, notebookPageCount = pages.size, notebookLayerCount = layers.size, notebookStrokeCount = strokes.size),
    emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), pages, layers, strokes
  )

  @Test fun acceptsCompleteNotebookHierarchy() {
    val page = NotebookPageEntity("p", "Page")
    val layer = NotebookLayerEntity("l", "p", "Ink")
    val stroke = NotebookStrokeEntity("s", "l", "[]")
    assertTrue(snapshot(listOf(page), listOf(layer), listOf(stroke)).isSafeToRestore())
  }

  @Test fun rejectsOrphanLayerAndStroke() {
    assertFalse(snapshot(layers = listOf(NotebookLayerEntity("l", "missing", "Ink"))).isSafeToRestore())
    assertFalse(snapshot(strokes = listOf(NotebookStrokeEntity("s", "missing", "[]"))).isSafeToRestore())
  }
}
