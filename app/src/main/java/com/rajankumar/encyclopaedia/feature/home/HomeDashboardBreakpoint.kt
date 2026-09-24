package com.rajankumar.encyclopaedia.feature.home

enum class HomeDashboardSize { COMPACT, MEDIUM, EXPANDED }

fun homeDashboardSize(widthDp: Int): HomeDashboardSize = when {
  widthDp >= 1100 -> HomeDashboardSize.EXPANDED
  widthDp >= 700 -> HomeDashboardSize.MEDIUM
  else -> HomeDashboardSize.COMPACT
}

fun HomeDashboardSize.statColumns(): Int = when (this) {
  HomeDashboardSize.COMPACT -> 1
  HomeDashboardSize.MEDIUM -> 2
  HomeDashboardSize.EXPANDED -> 4
}
