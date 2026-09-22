package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity
import com.rajankumar.encyclopaedia.data.local.NotebookLayerEntity
import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity
import com.rajankumar.encyclopaedia.data.local.NotebookStrokeEntity
import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity
import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupValidationTest {
  @Test
  fun validSnapshotPassesValidation() {
    assertEquals(BackupValidationResult.Valid, validateBackupSnapshot(validSnapshot()))
  }

  @Test
  fun manifestMismatchIsRejected() {
    val snapshot = validSnapshot().let {
      it.copy(manifest = it.manifest.copy(questionCount = 2))
    }

    assertTrue(validateBackupSnapshot(snapshot) is BackupValidationResult.Invalid)
  }

  @Test
  fun missingKnowledgeParentIsRejected() {
    val snapshot = validSnapshot().let {
      it.copy(
        knowledgeNodes = listOf(it.knowledgeNodes.single().copy(parentId = "missing"))
      )
    }

    assertTrue(validateBackupSnapshot(snapshot) is BackupValidationResult.Invalid)
  }

  @Test
  fun orphanedAttemptIsRejected() {
    val snapshot = validSnapshot().let {
      it.copy(attempts = listOf(it.attempts.single().copy(questionId = "missing")))
    }

    assertTrue(validateBackupSnapshot(snapshot) is BackupValidationResult.Invalid)
  }

  @Test
  fun notebookPageWithMissingKnowledgeNodeIsRejected() {
    val snapshot = validSnapshot().let {
      it.copy(notebookPages = listOf(it.notebookPages.single().copy(knowledgeNodeId = "missing")))
    }

    assertTrue(validateBackupSnapshot(snapshot) is BackupValidationResult.Invalid)
  }

  @Test
  fun orphanedNotebookLayerIsRejected() {
    val snapshot = validSnapshot().let {
      it.copy(notebookLayers = listOf(it.notebookLayers.single().copy(pageId = "missing")))
    }

    assertTrue(validateBackupSnapshot(snapshot) is BackupValidationResult.Invalid)
  }

  @Test
  fun orphanedNotebookStrokeIsRejected() {
    val snapshot = validSnapshot().let {
      it.copy(notebookStrokes = listOf(it.notebookStrokes.single().copy(layerId = "missing")))
    }

    assertTrue(validateBackupSnapshot(snapshot) is BackupValidationResult.Invalid)
  }

  private fun validSnapshot(): BackupSnapshot {
    val node = KnowledgeNodeEntity(
      id = "node",
      parentId = null,
      name = "Operating Systems"
    )
    val lesson = LessonEntity(
      id = "lesson",
      knowledgeNodeId = node.id,
      title = "Processes",
      content = "A process is a program in execution."
    )
    val question = QuestionEntity(
      id = "question",
      questionText = "What is a process?",
      options = "Program\nProgram in execution",
      correctAnswer = "Program in execution"
    )
    val topic = QuestionTopicEntity(questionId = question.id, knowledgeNodeId = node.id)
    val attempt = QuestionAttemptEntity(
      id = "attempt",
      questionId = question.id,
      sessionId = "session",
      selectedAnswer = "Program in execution",
      isCorrect = true,
      timeTakenMs = 1_000L
    )
    val plannerTask = PlannerTaskEntity(id = "planner", title = "Revise OS", scheduledDate = "2026-09-19")
    val page = NotebookPageEntity(id = "page", title = "Process notes", knowledgeNodeId = node.id)
    val layer = NotebookLayerEntity(id = "layer", pageId = page.id, name = "Writing")
    val stroke = NotebookStrokeEntity(
      id = "stroke",
      layerId = layer.id,
      pointsJson = "[[10,20],[30,40]]",
      tool = "PEN",
      colorArgb = 0xFF111111,
      width = 4f
    )
    val manifest = BackupManifest(
      createdAt = 1L,
      knowledgeNodeCount = 1,
      lessonCount = 1,
      questionCount = 1,
      questionTopicCount = 1,
      attemptCount = 1,
      plannerTaskCount = 1,
      notebookPageCount = 1,
      notebookLayerCount = 1,
      notebookStrokeCount = 1
    )
    return BackupSnapshot(
      manifest = manifest,
      knowledgeNodes = listOf(node),
      lessons = listOf(lesson),
      questions = listOf(question),
      questionTopics = listOf(topic),
      attempts = listOf(attempt),
      plannerTasks = listOf(plannerTask),
      notebookPages = listOf(page),
      notebookLayers = listOf(layer),
      notebookStrokes = listOf(stroke)
    )
  }
}
