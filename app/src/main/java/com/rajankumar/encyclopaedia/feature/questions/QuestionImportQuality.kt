package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun QuestionEntity.safeForAutomaticPractice(): Boolean =
  source.equals("USER", true) || assessQuality().usable
