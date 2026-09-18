package com.rajankumar.encyclopaedia.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EncyclopaediaDao {
  @Query("SELECT * FROM knowledge_nodes WHERE parentId IS NULL AND isArchived = 0 ORDER BY sortOrder, name")
  fun observeRootNodes(): Flow<List<KnowledgeNodeEntity>>

  @Query("SELECT * FROM knowledge_nodes WHERE parentId = :parentId AND isArchived = 0 ORDER BY sortOrder, name")
  fun observeChildren(parentId: String): Flow<List<KnowledgeNodeEntity>>

  @Query("SELECT * FROM knowledge_nodes WHERE isArchived = 0 ORDER BY name")
  fun observeAllNodes(): Flow<List<KnowledgeNodeEntity>>

  @Query("SELECT COUNT(*) FROM knowledge_nodes WHERE isArchived = 0")
  fun observeTopicCount(): Flow<Int>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertNode(node: KnowledgeNodeEntity)

  @Update suspend fun updateNode(node: KnowledgeNodeEntity)

  @Query("UPDATE knowledge_nodes SET isArchived = 1, updatedAt = :now WHERE id = :id")
  suspend fun archiveNode(id: String, now: Long = System.currentTimeMillis())

  @Query("SELECT * FROM lessons WHERE knowledgeNodeId = :nodeId AND isArchived = 0 ORDER BY sortOrder, title")
  fun observeLessons(nodeId: String): Flow<List<LessonEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertLesson(lesson: LessonEntity)

  @Query("UPDATE lessons SET isArchived = 1, updatedAt = :now WHERE id = :id")
  suspend fun archiveLesson(id: String, now: Long = System.currentTimeMillis())

  @Query("SELECT * FROM questions ORDER BY createdAt DESC")
  fun observeQuestions(): Flow<List<QuestionEntity>>

  @Query("SELECT COUNT(*) FROM questions")
  fun observeQuestionCount(): Flow<Int>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertQuestion(question: QuestionEntity)

  @Query("DELETE FROM questions WHERE id = :id")
  suspend fun deleteQuestion(id: String)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertQuestionTopic(link: QuestionTopicEntity)

  @Query("DELETE FROM question_topics WHERE questionId = :questionId")
  suspend fun clearQuestionTopics(questionId: String)

  @Transaction
  suspend fun saveQuestion(question: QuestionEntity, topicId: String?) {
    upsertQuestion(question)
    clearQuestionTopics(question.id)
    if (topicId != null) upsertQuestionTopic(QuestionTopicEntity(question.id, topicId))
  }
}
