package com.rajankumar.encyclopaedia.feature.importer

fun OcrAnswerConflict.warning(): String? = if (hasConflict) {
  "Conflicting printed answer markers were detected (${answers.distinct().joinToString()}). Verify the correct option manually."
} else {
  null
}
