package com.rajankumar.encyclopaedia.feature.questions

private val questionFingerprintNoise = Regex("[^\\p{L}\\p{N}]+")

fun aiQuestionFingerprint(value: String): String =
  value.trim().lowercase().replace(questionFingerprintNoise, "")
