package com.rajankumar.encyclopaedia.feature.home

data class HomeCalendarDay(val day: Int, val selected: Boolean = false, val hasPlan: Boolean = false) {
  val valid: Boolean get() = day in 1..31
}
