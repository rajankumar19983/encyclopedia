package com.rajankumar.encyclopaedia.feature.importer

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
