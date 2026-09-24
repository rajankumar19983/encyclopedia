package com.rajankumar.encyclopaedia.feature.importer

fun OcrDocumentImport.allNotices(): List<String> = buildList {
  addAll(extraction.notices().map { it.message })
  addAll(review.session.notices().map { it.message })
}
