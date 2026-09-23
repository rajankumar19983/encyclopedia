package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.*
import com.rajankumar.encyclopaedia.feature.backup.*
import org.junit.Assert.*
import org.junit.Test

class BackupRelationshipIntegrityTest {
  private fun manifest(
    nodes: Int = 0,
    lessons: Int = 0,
    questions: Int = 0,
    topics: Int = 0,
    attempts: Int = 0
  ) = BackupManifest(
    createdAt = 1L,
    knowledgeNodeCount = nodes,
    lessonCount = lessons,
    questionCount = questions,
    questionTopicCount = topics,
    attemptCount = attempts,
    plannerTaskCount = 0
  )

  private fun snapshot(
    nodes: List<KnowledgeNodeEntity> = emptyList(),
    lessons: List<LessonEntity> = emptyList(),
    questions: List<QuestionEntity> = emptyList(),
    topics: List<QuestionTopicEntity> = emptyList(),
    attempts: List<QuestionAttemptEntity> = emptyList()
  ) = BackupSnapshot(
    manifest = manifest(nodes.size, lessons.size, questions.size, topics.size, attempts.size),
    knowledgeNodes = nodes,
    lessons = lessons,
    questions = questions,
    questionTopics = topics,
    attempts = attempts,
    plannerTasks = emptyList()
  )

  @Test
  fun acceptsConnectedStudyData() {
    val node = KnowledgeNodeEntity("n", null, "Topic")
    val question = QuestionEntity("q", "Q?", "A\nB", "A")
    val lesson = LessonEntity("l", "n", "Lesson", "Body")
    val topic = QuestionTopicEntity("q", "n")
    val attempt = QuestionAttemptEntity("a", "q", "s", "A", true, 1L, 1L)

    assertTrue(
      snapshot(
        nodes = listOf(node),
        lessons = listOf(lesson),
        questions = listOf(question),
        topics = listOf(topic),
        attempts = listOf(attempt)
      ).hasValidStudyRelationships()
    )
  }

  @Test
  fun rejectsOrphanLesson() = assertFalse(
    snapshot(
      lessons = listOf(LessonEntity("l", "missing", "Lesson", "Body"))
    ).hasValidStudyRelationships()
  )
}
