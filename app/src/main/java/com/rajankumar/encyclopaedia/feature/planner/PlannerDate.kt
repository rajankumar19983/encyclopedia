package com.rajankumar.encyclopaedia.feature.planner

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val storageFormatter = DateTimeFormatter.ISO_LOCAL_DATE
private val displayFormatter = DateTimeFormatter.ofPattern("EEE, d MMM")

fun plannerDate(date: LocalDate = LocalDate.now()): String = date.format(storageFormatter)

fun plannerDisplayDate(value: String): String = runCatching {
  LocalDate.parse(value, storageFormatter).format(displayFormatter)
}.getOrDefault(value)

fun nextPlannerDate(value: String): String = LocalDate.parse(value, storageFormatter)
  .plusDays(1)
  .format(storageFormatter)
