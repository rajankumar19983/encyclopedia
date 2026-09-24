package com.rajankumar.encyclopaedia.feature.importer

data class OcrMetadataCandidate(
  val metadata: OcrSourceMetadata,
  val sourcePreview: String
)

fun detectOcrMetadataCandidate(text: String): OcrMetadataCandidate {
  val metadata = OcrSourceMetadataExtractor.extract(text)
  val preview = text.lineSequence()
    .map(String::trim)
    .filter(String::isNotBlank)
    .take(5)
    .joinToString(" ")
    .take(300)
  return OcrMetadataCandidate(metadata, preview)
}
