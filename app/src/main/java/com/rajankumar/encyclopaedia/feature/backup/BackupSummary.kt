package com.rajankumar.encyclopaedia.feature.backup

data class BackupSummary(val itemCount: Int, val bytes: Long, val age: BackupAge) {
  fun label(): String = "${backupItemCountLabel(itemCount)} • ${backupSizeLabel(bytes)} • ${age.label()}"
}
