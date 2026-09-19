package com.rajankumar.encyclopaedia.feature.planner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerTaskDeleteTest {
  @Test
  fun deleteMessageIncludesTrimmedTaskTitle() {
    assertEquals(
      "Delete ‘Revise networks’? This cannot be undone.",
      plannerDeleteMessage("  Revise networks  ")
    )
  }
}
