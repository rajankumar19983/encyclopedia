package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeRevisionCountTest {
  @Test fun labelsRevisionQueue() { assertEquals("Revision queue clear", homeRevisionCountLabel(0)); assertEquals("1 question to revise", homeRevisionCountLabel(1)); assertEquals("5 questions to revise", homeRevisionCountLabel(5)) }
}
