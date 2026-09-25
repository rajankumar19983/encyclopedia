package com.rajankumar.encyclopaedia.feature.knowledge

import com.rajankumar.encyclopaedia.data.local.KnowledgeContentSource

data class KnowledgeSourceSummary(
  val total: Int,
  val aiGenerated: Int,
  val manual: Int,
  val other: Int,
) {
  fun countFor(filter: KnowledgeSourceFilter): Int = when (filter) {
    KnowledgeSourceFilter.ALL -> total
    KnowledgeSourceFilter.AI_GENERATED -> aiGenerated
    KnowledgeSourceFilter.MANUAL -> manual
  }
}

fun summarizeKnowledgeSources(sources: Iterable<String>): KnowledgeSourceSummary {
  var total = 0
  var aiGenerated = 0
  var manual = 0
  var other = 0

  sources.forEach { source ->
    total += 1
    when (source) {
      KnowledgeContentSource.AI -> aiGenerated += 1
      KnowledgeContentSource.USER -> manual += 1
      else -> other += 1
    }
  }

  return KnowledgeSourceSummary(
    total = total,
    aiGenerated = aiGenerated,
    manual = manual,
    other = other,
  )
}

fun knowledgeSourceBadge(source: String): String? = when (source) {
  KnowledgeContentSource.AI -> "AI-generated"
  else -> null
}
