package com.rajankumar.encyclopaedia.feature.performance

data class WeakAreaSummary(val weakQuestions: Int, val repeatedMistakes: Int)

fun List<WeakQuestion>.weakAreaSummary() = WeakAreaSummary(size, count { it.mistakes >= 2 })
