package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import com.rajankumar.encyclopaedia.feature.backup.*
import org.junit.Assert.*
import org.junit.Test

class QuestionTopicIntegrityTest {
  private fun snapshot(topics: List<QuestionTopicEntity>) = BackupSnapshot(
    manifest = BackupManifest(
      createdAt = 1L,
      knowledgeNodeCount = 0,
      lessonCount = 0,
      questionCount = 0,
      questionTopicCount = topics.size,
      attemptCount = 0,
      plannerTaskCount = 0
    ),
    knowledgeNodes = emptyList(),
    lessons = emptyList(),
    questions = emptyList(),
    questionTopics = topics,
    attempts = emptyList(),
    plannerTasks = emptyList()
  )

  @Test
  fun acceptsUniquePairs() = assertTrue(
    snapshot(listOf(QuestionTopicEntity("q", "n"))).hasUniqueQuestionTopics()
  )

  @Test
  fun rejectsDuplicatePairs() = assertFalse(
    snapshot(
      listOf(
        QuestionTopicEntity("q", "n"),
        QuestionTopicEntity("q", "n")
      )
    ).hasUniqueQuestionTopics()
  )

  @Test
  fun rejectsBlankPair() = assertFalse(
    snapshot(listOf(QuestionTopicEntity("", "n"))).hasUniqueQuestionTopics()
  )
}
