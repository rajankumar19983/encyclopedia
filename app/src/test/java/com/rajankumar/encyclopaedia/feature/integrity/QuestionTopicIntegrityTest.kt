package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.QuestionTopicEntity
import com.rajankumar.encyclopaedia.feature.backup.*
import org.junit.Assert.*
import org.junit.Test

class QuestionTopicIntegrityTest {
  private fun snapshot(topics: List<QuestionTopicEntity>) = BackupSnapshot(BackupManifest(1L, 0, 0, 0, topics.size, 0, 0), emptyList(), emptyList(), emptyList(), topics, emptyList(), emptyList())
  @Test fun acceptsUniquePairs() = assertTrue(snapshot(listOf(QuestionTopicEntity("q", "n"))).hasUniqueQuestionTopics())
  @Test fun rejectsDuplicatePairs() = assertFalse(snapshot(listOf(QuestionTopicEntity("q", "n"), QuestionTopicEntity("q", "n"))).hasUniqueQuestionTopics())
  @Test fun rejectsBlankPair() = assertFalse(snapshot(listOf(QuestionTopicEntity("", "n"))).hasUniqueQuestionTopics())
}
