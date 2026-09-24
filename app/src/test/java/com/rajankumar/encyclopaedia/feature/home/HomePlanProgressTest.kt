package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.*
import org.junit.Test

class HomePlanProgressTest {
  @Test fun calculatesProgress() = assertEquals(0.5f, HomePlanProgress(2, 4).fraction, 0.001f)
  @Test fun boundsCompletedCount() = assertEquals("4 of 4 completed", HomePlanProgress(9, 4).label)
}
