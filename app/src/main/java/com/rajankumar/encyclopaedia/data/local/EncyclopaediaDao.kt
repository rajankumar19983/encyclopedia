package com.rajankumar.encyclopaedia.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot
import kotlinx.coroutines.flow.Flow

@Dao
interface EncyclopaediaDao {
  @Query("SELECT * FROM knowledge_nodes WHERE parentId IS NULL AND isArchived = 0 ORDER BY sortOrder, name") fun observeRootNodes(): Flow<List<KnowledgeNodeEntity>>
  @Query("SELECT * FROM knowledge_nodes WHERE parentId = :parentId AND isArchived = 0 ORDER BY sortOrder, name") fun observeChildren(parentId: String): Flow<List<KnowledgeNodeEntity>>
  @Query("SELECT * FROM knowledge_nodes WHERE isArchived = 0 ORDER BY name") fun observeAllNodes(): Flow<List<KnowledgeNodeEntity>>
  @Query("SELECT * FROM knowledge_nodes ORDER BY createdAt") suspend fun getAllNodesForBackup(): List<KnowledgeNodeEntity>
  @Query("SELECT COUNT(*) FROM knowledge_nodes WHERE isArchived = 0") fun observeTopicCount(): Flow<Int>
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertNode(node: KnowledgeNodeEntity)
  @Update suspend fun updateNode(node: KnowledgeNodeEntity)
  @Query("UPDATE knowledge_nodes SET isArchived = 1, updatedAt = :now WHERE id = :id") suspend fun archiveNode(id: String, now: Long = System.currentTimeMillis())
  @Query("SELECT * FROM lessons WHERE knowledgeNodeId = :nodeId AND isArchived = 0 ORDER BY sortOrder, title") fun observeLessons(nodeId: String): Flow<List<LessonEntity>>
  @Query("SELECT * FROM lessons ORDER BY createdAt") suspend fun getAllLessonsForBackup(): List<LessonEntity>
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertLesson(lesson: LessonEntity)
  @Query("UPDATE lessons SET isArchived = 1, updatedAt = :now WHERE id = :id") suspend fun archiveLesson(id: String, now: Long = System.currentTimeMillis())
  @Query("SELECT * FROM questions ORDER BY createdAt DESC") fun observeQuestions(): Flow<List<QuestionEntity>>
  @Query("SELECT COUNT(*) FROM questions") fun observeQuestionCount(): Flow<Int>
  @Query("SELECT * FROM questions") suspend fun getAllQuestionsOnce(): List<QuestionEntity>
  @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT :limit") suspend fun getRandomQuestions(limit: Int): List<QuestionEntity>
  @Query("SELECT * FROM questions WHERE id IN (:ids)") suspend fun getQuestionsByIds(ids: List<String>): List<QuestionEntity>
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertQuestion(question: QuestionEntity)
  @Query("DELETE FROM questions WHERE id = :id") suspend fun deleteQuestion(id: String)
  @Query("SELECT * FROM question_topics ORDER BY questionId, knowledgeNodeId") suspend fun getAllQuestionTopicsForBackup(): List<QuestionTopicEntity>
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertQuestionTopic(link: QuestionTopicEntity)
  @Query("DELETE FROM question_topics WHERE questionId = :questionId") suspend fun clearQuestionTopics(questionId: String)
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAttempt(attempt: QuestionAttemptEntity)
  @Query("SELECT * FROM question_attempts WHERE questionId = :questionId ORDER BY attemptedAt DESC") fun observeAttemptsForQuestion(questionId: String): Flow<List<QuestionAttemptEntity>>
  @Query("SELECT COUNT(*) FROM question_attempts") fun observeAttemptCount(): Flow<Int>
  @Query("SELECT COUNT(*) FROM question_attempts WHERE isCorrect = 1") fun observeCorrectAttemptCount(): Flow<Int>
  @Query("SELECT * FROM question_attempts ORDER BY attemptedAt DESC") fun observeAllAttempts(): Flow<List<QuestionAttemptEntity>>
  @Query("SELECT * FROM question_attempts ORDER BY attemptedAt DESC") suspend fun getAllAttemptsOnce(): List<QuestionAttemptEntity>
  @Query("SELECT * FROM question_attempts WHERE sessionId = :sessionId ORDER BY attemptedAt") suspend fun getSessionAttempts(sessionId: String): List<QuestionAttemptEntity>
  @Query("SELECT COUNT(DISTINCT questionId) FROM question_attempts") fun observePractisedQuestionCount(): Flow<Int>
  @Query("SELECT COUNT(*) FROM question_attempts WHERE attemptedAt >= :since") fun observeAttemptsSince(since: Long): Flow<Int>
  @Query("SELECT COUNT(*) FROM question_attempts WHERE attemptedAt >= :since AND isCorrect = 1") fun observeCorrectAttemptsSince(since: Long): Flow<Int>
  @Query("SELECT AVG(timeTakenMs) FROM question_attempts") fun observeAverageTimeMs(): Flow<Double?>
  @Query("SELECT * FROM questions WHERE id IN (SELECT DISTINCT questionId FROM question_attempts WHERE isCorrect = 0) ORDER BY RANDOM() LIMIT :limit") suspend fun getPreviouslyIncorrectQuestions(limit: Int): List<QuestionEntity>
  @Query("SELECT * FROM questions WHERE id NOT IN (SELECT DISTINCT questionId FROM question_attempts) ORDER BY RANDOM() LIMIT :limit") suspend fun getUnattemptedQuestions(limit: Int): List<QuestionEntity>
  @Query("SELECT COUNT(*) FROM question_attempts WHERE questionId = :questionId") suspend fun getAttemptCountForQuestion(questionId: String): Int
  @Query("SELECT COUNT(*) FROM question_attempts WHERE questionId = :questionId AND isCorrect = 1") suspend fun getCorrectCountForQuestion(questionId: String): Int
  @Query("SELECT * FROM planner_tasks WHERE scheduledDate = :date ORDER BY isCompleted, createdAt") fun observePlannerTasks(date: String): Flow<List<PlannerTaskEntity>>
  @Query("SELECT * FROM planner_tasks ORDER BY scheduledDate DESC, createdAt") fun observeAllPlannerTasks(): Flow<List<PlannerTaskEntity>>
  @Query("SELECT * FROM planner_tasks ORDER BY scheduledDate, createdAt") suspend fun getAllPlannerTasksForBackup(): List<PlannerTaskEntity>
  @Query("SELECT * FROM planner_tasks WHERE scheduledDate < :date AND isCompleted = 0 ORDER BY scheduledDate, createdAt") suspend fun getIncompletePlannerTasksBefore(date: String): List<PlannerTaskEntity>
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertPlannerTask(task: PlannerTaskEntity)
  @Query("UPDATE planner_tasks SET title = :title, updatedAt = :now WHERE id = :id") suspend fun updatePlannerTaskTitle(id: String, title: String, now: Long = System.currentTimeMillis())
  @Query("UPDATE planner_tasks SET isCompleted = :completed, completedAt = :completedAt, updatedAt = :now WHERE id = :id") suspend fun setPlannerTaskCompleted(id: String, completed: Boolean, completedAt: Long?, now: Long = System.currentTimeMillis())
  @Query("DELETE FROM planner_tasks WHERE id = :id") suspend fun deletePlannerTask(id: String)
  @Query("SELECT * FROM notebook_pages ORDER BY sortOrder, updatedAt DESC") fun observeNotebookPages(): Flow<List<NotebookPageEntity>>
  @Query("SELECT * FROM notebook_pages ORDER BY sortOrder, createdAt") suspend fun getAllNotebookPagesForBackup(): List<NotebookPageEntity>
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertNotebookPage(page: NotebookPageEntity)
  @Query("UPDATE notebook_pages SET background = :background, updatedAt = :now WHERE id = :id") suspend fun setNotebookPageBackground(id: String, background: String, now: Long = System.currentTimeMillis())
  @Query("UPDATE notebook_pages SET title = :title, updatedAt = :now WHERE id = :id") suspend fun renameNotebookPage(id: String, title: String, now: Long = System.currentTimeMillis())
  @Query("DELETE FROM notebook_pages WHERE id = :id") suspend fun deleteNotebookPage(id: String)
  @Query("SELECT * FROM notebook_layers WHERE pageId = :pageId ORDER BY sortOrder, createdAt") fun observeNotebookLayers(pageId: String): Flow<List<NotebookLayerEntity>>
  @Query("SELECT * FROM notebook_layers ORDER BY pageId, sortOrder, createdAt") suspend fun getAllNotebookLayersForBackup(): List<NotebookLayerEntity>
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertNotebookLayer(layer: NotebookLayerEntity)
  @Query("UPDATE notebook_layers SET name = :name, updatedAt = :now WHERE id = :id") suspend fun renameNotebookLayer(id: String, name: String, now: Long = System.currentTimeMillis())
  @Query("UPDATE notebook_layers SET sortOrder = :sortOrder, updatedAt = :now WHERE id = :id") suspend fun setNotebookLayerSortOrder(id: String, sortOrder: Int, now: Long = System.currentTimeMillis())
  @Transaction suspend fun swapNotebookLayerOrder(firstId:String,firstOrder:Int,secondId:String,secondOrder:Int){setNotebookLayerSortOrder(firstId,secondOrder);setNotebookLayerSortOrder(secondId,firstOrder)}
  @Query("UPDATE notebook_layers SET isVisible = :visible, updatedAt = :now WHERE id = :id") suspend fun setNotebookLayerVisible(id: String, visible: Boolean, now: Long = System.currentTimeMillis())
  @Query("UPDATE notebook_layers SET isLocked = :locked, updatedAt = :now WHERE id = :id") suspend fun setNotebookLayerLocked(id: String, locked: Boolean, now: Long = System.currentTimeMillis())
  @Query("DELETE FROM notebook_layers WHERE id = :id") suspend fun deleteNotebookLayer(id: String)
  @Query("SELECT * FROM notebook_strokes WHERE layerId = :layerId ORDER BY createdAt") fun observeNotebookStrokes(layerId: String): Flow<List<NotebookStrokeEntity>>
  @Query("SELECT s.* FROM notebook_strokes s INNER JOIN notebook_layers l ON l.id = s.layerId WHERE l.pageId = :pageId ORDER BY l.sortOrder, l.createdAt, s.createdAt") fun observeNotebookPageStrokes(pageId: String): Flow<List<NotebookStrokeEntity>>
  @Query("SELECT * FROM notebook_strokes WHERE layerId = :layerId ORDER BY createdAt") suspend fun getNotebookStrokesOnce(layerId:String):List<NotebookStrokeEntity>
  @Query("SELECT * FROM notebook_strokes ORDER BY layerId, createdAt") suspend fun getAllNotebookStrokesForBackup(): List<NotebookStrokeEntity>
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertNotebookStroke(stroke: NotebookStrokeEntity)
  @Query("DELETE FROM notebook_strokes WHERE id = :id") suspend fun deleteNotebookStroke(id: String)
  @Query("DELETE FROM notebook_strokes WHERE layerId = :layerId") suspend fun clearNotebookLayer(layerId: String)
  @Query("DELETE FROM question_attempts") suspend fun deleteAllAttemptsForRestore()
  @Query("DELETE FROM question_topics") suspend fun deleteAllQuestionTopicsForRestore()
  @Query("DELETE FROM questions") suspend fun deleteAllQuestionsForRestore()
  @Query("DELETE FROM lessons") suspend fun deleteAllLessonsForRestore()
  @Query("DELETE FROM knowledge_nodes") suspend fun deleteAllNodesForRestore()
  @Query("DELETE FROM planner_tasks") suspend fun deleteAllPlannerTasksForRestore()
  @Query("DELETE FROM notebook_strokes") suspend fun deleteAllNotebookStrokesForRestore()
  @Query("DELETE FROM notebook_layers") suspend fun deleteAllNotebookLayersForRestore()
  @Query("DELETE FROM notebook_pages") suspend fun deleteAllNotebookPagesForRestore()
  @Transaction suspend fun restoreSnapshot(snapshot: BackupSnapshot) { require(snapshot.isInternallyConsistent()); deleteAllNotebookStrokesForRestore(); deleteAllNotebookLayersForRestore(); deleteAllNotebookPagesForRestore(); deleteAllAttemptsForRestore(); deleteAllQuestionTopicsForRestore(); deleteAllQuestionsForRestore(); deleteAllLessonsForRestore(); deleteAllNodesForRestore(); deleteAllPlannerTasksForRestore(); snapshot.knowledgeNodes.forEach { upsertNode(it) }; snapshot.lessons.forEach { upsertLesson(it) }; snapshot.questions.forEach { upsertQuestion(it) }; snapshot.questionTopics.forEach { upsertQuestionTopic(it) }; snapshot.attempts.forEach { insertAttempt(it) }; snapshot.plannerTasks.forEach { upsertPlannerTask(it) }; snapshot.notebookPages.forEach { upsertNotebookPage(it) }; snapshot.notebookLayers.forEach { upsertNotebookLayer(it) }; snapshot.notebookStrokes.forEach { upsertNotebookStroke(it) } }
  @Transaction suspend fun saveQuestion(question: QuestionEntity, topicId: String?) { upsertQuestion(question); clearQuestionTopics(question.id); if (topicId != null) upsertQuestionTopic(QuestionTopicEntity(question.id, topicId)) }
  @Transaction suspend fun saveImportedQuestionIfUnique(question: QuestionEntity, topicId: String?): Boolean { val q=importFingerprint(question.questionText); val opts=question.options.lines().map(::importFingerprint).filter{it.isNotBlank()}; if(getAllQuestionsOnce().any{importFingerprint(it.questionText)==q && it.options.lines().map(::importFingerprint).filter{x->x.isNotBlank()}==opts}) return false; saveQuestion(question,topicId); return true }
}
private fun importFingerprint(value:String)=value.lowercase().replace(Regex("[^\\p{L}\\p{N}]+"),"")
