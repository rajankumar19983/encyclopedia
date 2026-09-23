package com.rajankumar.encyclopaedia.feature.backup

data class BackupManifestSummary(val records: Int, val studyRecords: Int, val notebookRecords: Int)

fun BackupManifest.summary(): BackupManifestSummary {
  val notebook = notebookPageCount + notebookLayerCount + notebookStrokeCount
  return BackupManifestSummary(totalRecords, totalRecords - notebook, notebook)
}
