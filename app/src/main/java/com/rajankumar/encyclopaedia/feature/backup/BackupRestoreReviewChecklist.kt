package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReviewChecklistItem(
  val group: BackupRestoreIssueGroup,
  val message: String,
  val blocking: Boolean
)

fun BackupInspection.restoreReviewChecklist(): List<BackupRestoreReviewChecklistItem> =
  restoreReviewSections().orderedForRestoreReview().flatMap { section ->
    section.messages.map { message ->
      BackupRestoreReviewChecklistItem(section.group, message, section.hasBlockingIssues)
    }
  }
