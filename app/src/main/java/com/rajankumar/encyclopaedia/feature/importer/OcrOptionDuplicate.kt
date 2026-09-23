package com.rajankumar.encyclopaedia.feature.importer

data class OcrDuplicateOptions(val groups: List<List<Int>>) {
  val hasDuplicates: Boolean get() = groups.isNotEmpty()
}

fun detectDuplicateOcrOptions(options: List<String>): OcrDuplicateOptions {
  val indexed = options.mapIndexed { index, option -> option.trim().lowercase().replace(Regex("\\s+"), " ") to index }
  val groups = indexed.groupBy({ it.first }, { it.second }).filter { it.key.isNotBlank() && it.value.size > 1 }.values.toList()
  return OcrDuplicateOptions(groups)
}
