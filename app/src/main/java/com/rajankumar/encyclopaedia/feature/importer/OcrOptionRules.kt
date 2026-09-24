package com.rajankumar.encyclopaedia.feature.importer

enum class OcrOptionCountQuality { TOO_FEW, NORMAL, UNUSUALLY_MANY }

data class OcrOptionCountPolicy(
  val supported: Boolean,
  val warning: String? = null,
)

data class OcrDuplicateOptions(val groups: List<List<Int>>) {
  val hasDuplicates: Boolean get() = groups.isNotEmpty()
}

fun classifyOcrOptionCount(count: Int): OcrOptionCountQuality = when {
  count < 2 -> OcrOptionCountQuality.TOO_FEW
  count <= 12 -> OcrOptionCountQuality.NORMAL
  else -> OcrOptionCountQuality.UNUSUALLY_MANY
}

fun ParsedQuestionDraft.optionCountPolicy(): OcrOptionCountPolicy = when (options.size) {
  in 2..6 -> OcrOptionCountPolicy(true)
  0, 1 -> OcrOptionCountPolicy(false, "At least two options are required.")
  else -> OcrOptionCountPolicy(false, "More than six options were detected; review OCR structure before saving.")
}

fun detectDuplicateOcrOptions(options: List<String>): OcrDuplicateOptions {
  val indexed = options.mapIndexed { index, option ->
    option.trim().lowercase().replace(Regex("\\s+"), " ") to index
  }
  val groups = indexed.groupBy({ it.first }, { it.second })
    .filter { it.key.isNotBlank() && it.value.size > 1 }
    .values
    .toList()
  return OcrDuplicateOptions(groups)
}

fun OcrDuplicateOptions.warning(): String? {
  if (!hasDuplicates) return null
  val labels = groups.joinToString("; ") { group -> group.joinToString(", ") { ocrOptionLabel(it) } }
  return "Duplicate option text detected at: $labels. Verify OCR before approval."
}

fun ocrOptionLabel(index: Int): String {
  require(index >= 0)
  var value = index
  val result = StringBuilder()
  do {
    result.append(('A'.code + value % 26).toChar())
    value = value / 26 - 1
  } while (value >= 0)
  return result.reverse().toString()
}
