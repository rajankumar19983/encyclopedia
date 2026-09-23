package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.*
import com.rajankumar.encyclopaedia.feature.backup.*
import org.junit.Assert.*
import org.junit.Test

class NotebookRelationshipIntegrityTest {
  private fun snapshot(
    pages: List<NotebookPageEntity>,
    layers: List<NotebookLayerEntity>,
    strokes: List<NotebookStrokeEntity>
  ) = BackupSnapshot(
    manifest = BackupManifest(
      createdAt = 1L,
      knowledgeNodeCount = 0,
      lessonCount = 0,
      questionCount = 0,
      questionTopicCount = 0,
      attemptCount = 0,
      plannerTaskCount = 0,
      notebookPageCount = pages.size,
      notebookLayerCount = layers.size,
      notebookStrokeCount = strokes.size
    ),
    knowledgeNodes = emptyList(),
    lessons = emptyList(),
    questions = emptyList(),
    questionTopics = emptyList(),
    attempts = emptyList(),
    plannerTasks = emptyList(),
    notebookPages = pages,
    notebookLayers = layers,
    notebookStrokes = strokes
  )

  @Test
  fun acceptsConnectedNotebook() = assertTrue(
    snapshot(
      listOf(NotebookPageEntity("p", "Notes")),
      listOf(NotebookLayerEntity("l", "p", "Ink")),
      listOf(NotebookStrokeEntity("s", "l", "[]"))
    ).hasValidNotebookRelationships()
  )

  @Test
  fun rejectsOrphanLayer() = assertFalse(
    snapshot(
      emptyList(),
      listOf(NotebookLayerEntity("l", "missing", "Ink")),
      emptyList()
    ).hasValidNotebookRelationships()
  )
}
