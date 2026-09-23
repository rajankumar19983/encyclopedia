package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity

fun KnowledgeNodeEntity.hasValidKnowledgeFields(): Boolean = id.isNotBlank() && name.isNotBlank() && sortOrder >= 0
fun LessonEntity.hasValidLessonFields(): Boolean = id.isNotBlank() && knowledgeNodeId.isNotBlank() && title.isNotBlank() && content.isNotBlank() && sortOrder >= 0
