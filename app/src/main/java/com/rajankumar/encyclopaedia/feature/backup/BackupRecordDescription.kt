package com.rajankumar.encyclopaedia.feature.backup

fun BackupManifest.recordDescription(): String {
  val summary = summary()
  if (summary.records == 0) return "Empty backup"
  return "${summary.studyRecords} study records • ${summary.notebookRecords} notebook records"
}
