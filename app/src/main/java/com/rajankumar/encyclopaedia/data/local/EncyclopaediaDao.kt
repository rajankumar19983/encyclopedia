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

  @Query("SELECT * FROM questions")
  suspend fun getAllQuestionsOnce(): List<QuestionEntity>

  @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT :limit")
  suspend fun getRandomQuestions(limit: Int): List<QuestionEntity>

  @Query("SELECT * FROM questions WHERE id IN (:ids)")
  suspend fun getQuestionsByIds(ids: List<String>): List<QuestionEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertQuestion(question: QuestionEntity)

  @Query("DELETE FROM questions WHERE id = :id")
  suspend fun deleteQuestion(id: String)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertQuestionTopic(link: QuestionTopicEntity)

  @Query("DELETE FROM question_topics WHERE questionId = :questionId")
  suspend fun clearQuestionTopics(questionId: String)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAttempt(attempt: QuestionAttemptEntity)

  @Query("SELECT * FROM question_attempts WHERE questionId = :questionId ORDER BY attemptedAt DESC")
  fun observeAttemptsForQuestion(questionId: String): Flow<List<QuestionAttemptEntity>>

  @Query("SELECT COUNT(*) FROM question_attempts")
  fun observeAttemptCount(): Flow<Int>

  @Query("SELECT COUNT(*) FROM question_attempts WHERE isCorrect = 1")
  fun observeCorrectAttemptCount(): Flow<Int>

  @Query("SELECT * FROM question_attempts ORDER BY attemptedAt DESC")
  fun observeAllAttempts(): Flow<List<QuestionAttemptEntity>>

  @Query("SELECT * FROM question_attempts ORDER BY attemptedAt DESC")
  suspend fun getAllAttemptsOnce(): List<QuestionAttemptEntity>

  @Query("SELECT * FROM question_attempts WHERE sessionId = :sessionId ORDER BY attemptedAt")
  suspend fun getSessionAttempts(sessionId: String): List<QuestionAttemptEntity>

  @Query("SELECT COUNT(DISTINCT questionId) FROM question_attempts")
  fun observePractisedQuestionCount(): Flow<Int>

  @Query("SELECT COUNT(*) FROM question_attempts WHERE attemptedAt >= :since")
  fun observeAttemptsSince(since: Long): Flow<Int>

  @Query("SELECT COUNT(*) FROM question_attempts WHERE attemptedAt >= :since AND isCorrect = 1")
  fun observeCorrectAttemptsSince(since: Long): Flow<Int>

  @Query("SELECT AVG(timeTakenMs) FROM question_attempts")
  fun observeAverageTimeMs(): Flow<Double?>

  @Query("SELECT * FROM questions WHERE id IN (SELECT DISTINCT questionId FROM question_attempts WHERE isCorrect = 0) ORDER BY RANDOM() LIMIT :limit")
  suspend fun getPreviouslyIncorrectQuestions(limit: Int): List<QuestionEntity>

  @Query("SELECT * FROM questions WHERE id NOT IN (SELECT DISTINCT questionId FROM question_attempts) ORDER BY RANDOM() LIMIT :limit")
  suspend fun getUnattemptedQuestions(limit: Int): List<QuestionEntity>

  @Query("SELECT COUNT(*) FROM question_attempts WHERE questionId = :questionId")
  suspend fun getAttemptCountForQuestion(questionId: String): Int

  @Query("SELECT COUNT(*) FROM question_attempts WHERE questionId = :questionId AND isCorrect = 1")
  suspend fun getCorrectCountForQuestion(questionId: String): Int

  @Query("SELECT * FROM planner_tasks WHERE scheduledDate = :date ORDER BY isCompleted, createdAt")
  fun observePlannerTasks(date: String): Flow<List<PlannerTaskEntity>>

  @Query("SELECT * FROM planner_tasks ORDER BY scheduledDate DESC, createdAt")
  fun observeAllPlannerTasks(): Flow<List<PlannerTaskEntity>>

  @Query("SELECT * FROM planner_tasks WHERE scheduledDate < :date AND isCompleted = 0 ORDER BY scheduledDate, createdAt")
  suspend fun getIncompletePlannerTasksBefore(date: String): List<PlannerTaskEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsertPlannerTask(task: PlannerTaskEntity)

  @Query("UPDATE planner_tasks SET title = :title, updatedAt = :now WHERE id = :id")
  suspend fun updatePlannerTaskTitle(id: String, title: String, now: Long = System.currentTimeMillis())

  @Query("UPDATE planner_tasks SET isCompleted = :completed, completedAt = :completedAt, updatedAt = :now WHERE id = :id")
  suspend fun setPlannerTaskCompleted(id: String, completed: Boolean, completedAt: Long?, now: Long = System.currentTimeMillis())

  @Query("DELETE FROM planner_tasks WHERE id = :id")
  suspend fun deletePlannerTask(id: String)

  @Transaction
  suspend fun saveQuestion(question: QuestionEntity, topicId: String?) {
    upsertQuestion(question)
    clearQuestionTopics(question.id)
    if (topicId != null) upsertQuestionTopic(QuestionTopicEntity(question.id, topicId))
  }

  @Transaction
  suspend fun saveImportedQuestionIfUnique(question: QuestionEntity, topicId: String?): Boolean {
    val incomingQuestion = importFingerprint(question.questionText)
    val incomingOptions = question.options.lines().map(::importFingerprint).filter { it.isNotBlank() }
    val duplicate = getAllQuestionsOnce().any { existing ->
      importFingerprint(existing.questionText) == incomingQuestion &&
        existing.options.lines().map(::importFingerprint).filter { it.isNotBlank() } == incomingOptions
    }
    if (duplicate) return false
    saveQuestion(question, topicId)
    return true
  }
}

private fun importFingerprint(value: String): String = value
  .lowercase()
  .replace(Regex("[^\\p{L}\\p{N}]+"), "")
