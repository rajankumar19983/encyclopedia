package com.rajankumar.encyclopaedia.feature.performance

enum class CoverageBand(val label: String) { STARTING("Starting"), PARTIAL("Partial"), BROAD("Broad"), COMPLETE("Complete") }

fun coverageBand(coverage: Int): CoverageBand = when (coverage.coerceIn(0, 100)) {
  in 0..24 -> CoverageBand.STARTING
  in 25..59 -> CoverageBand.PARTIAL
  in 60..99 -> CoverageBand.BROAD
  else -> CoverageBand.COMPLETE
}
