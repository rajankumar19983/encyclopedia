package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.*
import com.rajankumar.encyclopaedia.feature.backup.*
import org.junit.Assert.*
import org.junit.Test

class BackupRelationshipIntegrityTest {
  private fun manifest(nodes: Int = 0, lessons: Int = 0, questions: Int = 0, topics: Int = 0, attempts: Int = 0) = BackupManifest(1L, nodes, lessons, questions, topics, attempts, 0)
  private fun snapshot(nodes: List<KnowledgeNodeEntity> = emptyList(), lessons: List<LessonEntity> = emptyList(), questions: List<QuestionEntity> = emptyList(), topics: List<QuestionTopicEntity> = emptyList(), attempts: List<QuestionAttemptEntity> = emptyList()) = BackupSnapshot(manifest(nodes.size, lessons.size, questions.size, topics.size, attempts.size), nodes, lessons, questions, topics, attempts, emptyList())

  @Test fun acceptsConnectedStudyData() {
    val node = KnowledgeNodeEntity("n", null, "Topic")
    val question = QuestionEntity("q", "Q?", "A\nB", "A")
    assertTrue(snapshot(listOf(node), listOf(LessonEntity("l", "n", "Lesson", "Body")), listOf(question), listOf(QuestionTopicEntity("q", "n")), listOf(QuestionAttemptEntity("a", "q", "s", "A", true, 1L, 1L))).hasValidStudyRelationships())
  }

  @Test fun rejectsOrphanLesson() = assertFalse(snapshot(lessons = listOf(LessonEntity("l", "missing", "Lesson", "Body"))).hasValidStudyRelationships())
}
