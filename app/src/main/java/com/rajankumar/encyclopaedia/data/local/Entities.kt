package com.rajankumar.encyclopaedia.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "knowledge_nodes", indices = [Index("parentId"), Index("name")])
data class KnowledgeNodeEntity(
  @PrimaryKey val id: String,
  val parentId: String?,
  val name: String,
  val description: String? = null,
  val sortOrder: Int = 0,
  val isArchived: Boolean = false,
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
  tableName = "lessons",
  foreignKeys = [ForeignKey(entity = KnowledgeNodeEntity::class, parentColumns = ["id"], childColumns = ["knowledgeNodeId"], onDelete = ForeignKey.CASCADE)],
  indices = [Index("knowledgeNodeId")]
)
data class LessonEntity(
  @PrimaryKey val id: String,
  val knowledgeNodeId: String,
  val title: String,
  val content: String,
  val sortOrder: Int = 0,
  val isArchived: Boolean = false,
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "questions", indices = [Index("source"), Index("difficulty"), Index("createdAt")])
data class QuestionEntity(
  @PrimaryKey val id: String,
  val questionText: String,
  val options: String,
  val correctAnswer: String,
  val explanation: String? = null,
  val source: String = "USER",
  val difficulty: String = "MEDIUM",
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
  tableName = "question_topics",
  primaryKeys = ["questionId", "knowledgeNodeId"],
  foreignKeys = [
    ForeignKey(entity = QuestionEntity::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = KnowledgeNodeEntity::class, parentColumns = ["id"], childColumns = ["knowledgeNodeId"], onDelete = ForeignKey.CASCADE)
  ],
  indices = [Index("questionId"), Index("knowledgeNodeId")]
)
data class QuestionTopicEntity(
  val questionId: String,
  val knowledgeNodeId: String
)
