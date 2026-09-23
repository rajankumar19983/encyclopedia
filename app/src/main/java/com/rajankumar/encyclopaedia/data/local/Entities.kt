package com.rajankumar.encyclopaedia.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "knowledge_nodes", indices = [Index("parentId"), Index("name")])
data class KnowledgeNodeEntity(@PrimaryKey val id: String, val parentId: String?, val name: String, val description: String? = null, val sortOrder: Int = 0, val isArchived: Boolean = false, val createdAt: Long = System.currentTimeMillis(), val updatedAt: Long = System.currentTimeMillis())

@Entity(tableName = "lessons", foreignKeys = [ForeignKey(entity = KnowledgeNodeEntity::class, parentColumns = ["id"], childColumns = ["knowledgeNodeId"], onDelete = ForeignKey.CASCADE)], indices = [Index("knowledgeNodeId")])
data class LessonEntity(@PrimaryKey val id: String, val knowledgeNodeId: String, val title: String, val content: String, val sortOrder: Int = 0, val isArchived: Boolean = false, val createdAt: Long = System.currentTimeMillis(), val updatedAt: Long = System.currentTimeMillis())

@Entity(tableName = "questions", indices = [Index("source"), Index("difficulty"), Index("createdAt")])
data class QuestionEntity(@PrimaryKey val id: String, val questionText: String, val options: String, val correctAnswer: String, val explanation: String? = null, val source: String = "USER", val difficulty: String = "MEDIUM", val createdAt: Long = System.currentTimeMillis(), val updatedAt: Long = System.currentTimeMillis())

@Entity(tableName = "question_topics", primaryKeys = ["questionId", "knowledgeNodeId"], foreignKeys = [ForeignKey(entity = QuestionEntity::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE), ForeignKey(entity = KnowledgeNodeEntity::class, parentColumns = ["id"], childColumns = ["knowledgeNodeId"], onDelete = ForeignKey.CASCADE)], indices = [Index("questionId"), Index("knowledgeNodeId")])
data class QuestionTopicEntity(val questionId: String, val knowledgeNodeId: String)

@Entity(tableName = "question_attempts", foreignKeys = [ForeignKey(entity = QuestionEntity::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE)], indices = [Index("questionId"), Index("attemptedAt"), Index("sessionId")])
data class QuestionAttemptEntity(@PrimaryKey val id: String, val questionId: String, val sessionId: String, val selectedAnswer: String, val isCorrect: Boolean, val timeTakenMs: Long, val attemptedAt: Long = System.currentTimeMillis())

@Entity(tableName = "planner_tasks", indices = [Index("scheduledDate"), Index("isCompleted"), Index("createdAt")])
data class PlannerTaskEntity(@PrimaryKey val id: String, val title: String, val scheduledDate: String, val isCompleted: Boolean = false, val completedAt: Long? = null, val carriedFromDate: String? = null, val createdAt: Long = System.currentTimeMillis(), val updatedAt: Long = System.currentTimeMillis())

@Entity(tableName = "notebook_pages", indices = [Index("knowledgeNodeId"), Index("updatedAt")])
data class NotebookPageEntity(
  @PrimaryKey val id: String,
  val title: String,
  val knowledgeNodeId: String? = null,
  val pageWidth: Float = 1600f,
  val pageHeight: Float = 2200f,
  val background: String = "PLAIN",
  val sortOrder: Int = 0,
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notebook_layers", foreignKeys = [ForeignKey(entity = NotebookPageEntity::class, parentColumns = ["id"], childColumns = ["pageId"], onDelete = ForeignKey.CASCADE)], indices = [Index("pageId")])
data class NotebookLayerEntity(
  @PrimaryKey val id: String,
  val pageId: String,
  val name: String,
  val sortOrder: Int = 0,
  val isVisible: Boolean = true,
  val isLocked: Boolean = false,
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notebook_strokes", foreignKeys = [ForeignKey(entity = NotebookLayerEntity::class, parentColumns = ["id"], childColumns = ["layerId"], onDelete = ForeignKey.CASCADE)], indices = [Index("layerId"), Index("createdAt")])
data class NotebookStrokeEntity(
  @PrimaryKey val id: String,
  val layerId: String,
  val pointsJson: String,
  val tool: String = "PEN",
  val colorArgb: Long = 0xFF111111,
  val width: Float = 4f,
  val createdAt: Long = System.currentTimeMillis()
)
