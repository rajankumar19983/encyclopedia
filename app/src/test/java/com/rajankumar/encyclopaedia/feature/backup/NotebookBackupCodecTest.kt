package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.NotebookLayerEntity
import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity
import com.rajankumar.encyclopaedia.data.local.NotebookStrokeEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookBackupCodecTest {
  @Test
  fun notebookDataSurvivesBackupRoundTrip() {
    val page = NotebookPageEntity(
      id = "page-1",
      title = "CPU scheduling",
      knowledgeNodeId = null,
      pageWidth = 1600f,
      pageHeight = 2400f,
      background = "GRID",
      sortOrder = 3,
      createdAt = 100L,
      updatedAt = 200L
    )
    val layer = NotebookLayerEntity(
      id = "layer-1",
      pageId = page.id,
      name = "Highlights",
      sortOrder = 2,
      isVisible = false,
      isLocked = true,
      createdAt = 300L,
      updatedAt = 400L
    )
    val stroke = NotebookStrokeEntity(
      id = "stroke-1",
      layerId = layer.id,
      pointsJson = "[{\"x\":10.5,\"y\":20.25,\"pressure\":0.7},{\"x\":30.75,\"y\":40.5,\"pressure\":1.0}]",
      tool = "HIGHLIGHTER",
      colorArgb = 0x80FFCC00,
      width = 18.5f,
      createdAt = 500L
    )
    val snapshot = BackupSnapshot(
      manifest = BackupManifest(
        createdAt = 600L,
        knowledgeNodeCount = 0,
        lessonCount = 0,
        questionCount = 0,
        questionTopicCount = 0,
        attemptCount = 0,
        plannerTaskCount = 0,
        notebookPageCount = 1,
        notebookLayerCount = 1,
        notebookStrokeCount = 1
      ),
      knowledgeNodes = emptyList(),
      lessons = emptyList(),
      questions = emptyList(),
      questionTopics = emptyList(),
      attempts = emptyList(),
      plannerTasks = emptyList(),
      notebookPages = listOf(page),
      notebookLayers = listOf(layer),
      notebookStrokes = listOf(stroke)
    )

    val restored = BackupCodec.decode(BackupCodec.encode(snapshot))

    assertEquals(snapshot.manifest, restored.manifest)
    assertEquals(listOf(page), restored.notebookPages)
    assertEquals(listOf(layer), restored.notebookLayers)
    assertEquals(listOf(stroke), restored.notebookStrokes)
    assertEquals(BackupValidationResult.Valid, validateBackupSnapshot(restored))
  }

  @Test
  fun notebookStrokeGeometryIsNotNormalizedDuringBackup() {
    val points = "[{\"x\":-12.125,\"y\":4096.75,\"pressure\":0.12345}]"
    val stroke = NotebookStrokeEntity(
      id = "stroke-geometry",
      layerId = "layer-geometry",
      pointsJson = points,
      tool = "PEN",
      colorArgb = 0xFF123456,
      width = 2.75f,
      createdAt = 1L
    )
    val page = NotebookPageEntity(id = "page-geometry", title = "Geometry")
    val layer = NotebookLayerEntity(id = "layer-geometry", pageId = page.id, name = "Ink")
    val snapshot = BackupSnapshot(
      manifest = BackupManifest(
        createdAt = 1L,
        knowledgeNodeCount = 0,
        lessonCount = 0,
        questionCount = 0,
        questionTopicCount = 0,
        attemptCount = 0,
        plannerTaskCount = 0,
        notebookPageCount = 1,
        notebookLayerCount = 1,
        notebookStrokeCount = 1
      ),
      knowledgeNodes = emptyList(),
      lessons = emptyList(),
      questions = emptyList(),
      questionTopics = emptyList(),
      attempts = emptyList(),
      plannerTasks = emptyList(),
      notebookPages = listOf(page),
      notebookLayers = listOf(layer),
      notebookStrokes = listOf(stroke)
    )

    val restoredStroke = BackupCodec.decode(BackupCodec.encode(snapshot)).notebookStrokes.single()

    assertEquals(points, restoredStroke.pointsJson)
    assertEquals(stroke.width, restoredStroke.width)
    assertEquals(stroke.colorArgb, restoredStroke.colorArgb)
    assertTrue(restoredStroke.pointsJson.contains("4096.75"))
  }
}
