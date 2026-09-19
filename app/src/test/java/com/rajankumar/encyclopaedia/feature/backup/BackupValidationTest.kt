package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity
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

  private fun validSnapshot(): BackupSnapshot {
    val node = KnowledgeNodeEntity(id = "node", name = "Operating Systems", type = "topic")
    val lesson = LessonEntity(id = "lesson", knowledgeNodeId = node.id, title = "Processes")
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
      selectedAnswer = "Program in execution",
      isCorrect = true
    )
    val plannerTask = PlannerTaskEntity(id = "planner", title = "Revise OS", scheduledDate = "2026-09-19")
    val manifest = BackupManifest(
      createdAt = 1L,
      knowledgeNodeCount = 1,
      lessonCount = 1,
      questionCount = 1,
      questionTopicCount = 1,
      attemptCount = 1,
      plannerTaskCount = 1
    )
    return BackupSnapshot(
      manifest = manifest,
      knowledgeNodes = listOf(node),
      lessons = listOf(lesson),
      questions = listOf(question),
      questionTopics = listOf(topic),
      attempts = listOf(attempt),
      plannerTasks = listOf(plannerTask)
    )
  }
}
