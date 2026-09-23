package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionSessionTitleTest {
  @Test fun pluralisesRevisionCount() {
    assertEquals("Revision queue clear", revisionSessionTitle(0))
    assertEquals("1 question to revise", revisionSessionTitle(1))
    assertEquals("3 questions to revise", revisionSessionTitle(3))
  }
}
