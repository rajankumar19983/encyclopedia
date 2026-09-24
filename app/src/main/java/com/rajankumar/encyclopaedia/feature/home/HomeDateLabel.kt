package com.rajankumar.encyclopaedia.feature.home

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val dashboardDateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH)

fun homeDateLabel(date: LocalDate = LocalDate.now()): String = date.format(dashboardDateFormatter)
