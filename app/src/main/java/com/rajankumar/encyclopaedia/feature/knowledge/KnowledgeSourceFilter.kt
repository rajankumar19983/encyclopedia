package com.rajankumar.encyclopaedia.feature.knowledge

import com.rajankumar.encyclopaedia.data.local.KnowledgeContentSource
import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity

enum class KnowledgeSourceFilter(val label: String) {
  ALL("All"),
  AI_GENERATED("AI-generated"),
  MANUAL("Manual");

  fun matches(source: String): Boolean = when (this) {
    ALL -> true
    AI_GENERATED -> source == KnowledgeContentSource.AI
    MANUAL -> source == KnowledgeContentSource.USER
  }

  fun emptyMessage(contentLabel: String): String = when (this) {
    ALL -> "No $contentLabel yet."
    AI_GENERATED -> "No AI-generated $contentLabel here."
    MANUAL -> "No manually created $contentLabel here."
  }
}

fun filterKnowledgeNodesBySource(
  nodes: List<KnowledgeNodeEntity>,
  filter: KnowledgeSourceFilter,
): List<KnowledgeNodeEntity> = nodes.filter { filter.matches(it.source) }

fun filterLessonsBySource(
  lessons: List<LessonEntity>,
  filter: KnowledgeSourceFilter,
): List<LessonEntity> = lessons.filter { filter.matches(it.source) }
