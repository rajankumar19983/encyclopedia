package com.rajankumar.encyclopaedia.feature.importer

fun ImportReviewQueue.canPersistWithoutDuplicates(existing: List<ExistingQuestionFingerprint> = emptyList()): Boolean =
  persistencePlan(existing).ready

fun ImportReviewQueue.persistableUniqueDrafts(existing: List<ExistingQuestionFingerprint> = emptyList()): List<EditableImportDraft> =
  persistencePlan(existing).drafts
