package com.rajankumar.encyclopaedia.feature.home

enum class HomeDashboardSize { COMPACT, MEDIUM, EXPANDED }

fun homeDashboardSize(widthDp: Int): HomeDashboardSize = when {
  widthDp >= 1100 -> HomeDashboardSize.EXPANDED
  widthDp >= 700 -> HomeDashboardSize.MEDIUM
  else -> HomeDashboardSize.COMPACT
}
