package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot

fun BackupSnapshot.hasValidDomainFields(): Boolean =
  knowledgeNodes.all { it.hasValidKnowledgeFields() } &&
    lessons.all { it.hasValidLessonFields() } &&
    questions.all { it.hasValidQuestionFields() } &&
    attempts.all { it.hasValidAttemptFields() } &&
    plannerTasks.all { it.hasValidPlannerFields() }

fun BackupSnapshot.hasValidDomainIds(): Boolean = listOf(
  knowledgeNodes.map { it.id }, lessons.map { it.id }, questions.map { it.id }, attempts.map { it.id },
  plannerTasks.map { it.id }, notebookPages.map { it.id }, notebookLayers.map { it.id }, notebookStrokes.map { it.id }
).all(::hasUniqueNonBlankIds)
