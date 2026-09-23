package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssueDetail

enum class BackupRestoreIssueGroup {
  BACKUP_STRUCTURE,
  CONTENT,
  RELATIONSHIPS
}

data class BackupRestoreIssueGroupSummary(
  val group: BackupRestoreIssueGroup,
  val issues: List<IntegrityIssueDetail>
) {
  val issueCount: Int get() = issues.size
}

fun BackupInspection.restoreIssueGroups(): List<BackupRestoreIssueGroupSummary> =
  integrity?.details.orEmpty()
    .groupBy { detail ->
      when (detail.issue) {
        IntegrityIssue.MANIFEST,
        IntegrityIssue.IDS -> BackupRestoreIssueGroup.BACKUP_STRUCTURE

        IntegrityIssue.FIELDS,
        IntegrityIssue.KNOWLEDGE_HIERARCHY -> BackupRestoreIssueGroup.CONTENT

        IntegrityIssue.QUESTION_TOPICS,
        IntegrityIssue.STUDY_RELATIONSHIPS,
        IntegrityIssue.NOTEBOOK_RELATIONSHIPS -> BackupRestoreIssueGroup.RELATIONSHIPS
      }
    }
    .map { (group, issues) -> BackupRestoreIssueGroupSummary(group, issues) }
    .sortedBy { it.group.ordinal }
