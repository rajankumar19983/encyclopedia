package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class QuestionBankStats(
  val total: Int,
  val scanned: Int,
  val pdf: Int,
  val userCreated: Int
)

fun List<QuestionEntity>.questionBankStats(): QuestionBankStats = QuestionBankStats(
  total = size,
  scanned = count { it.source.equals("SCAN", true) },
  pdf = count { it.source.equals("PDF", true) },
  userCreated = count { it.source.equals("USER", true) }
)
