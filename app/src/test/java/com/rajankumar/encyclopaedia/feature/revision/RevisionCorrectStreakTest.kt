package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionCorrectStreakTest {
  @Test fun labelsCorrectStreaks() { assertEquals("No correct streak", revisionCorrectStreakLabel(0)); assertEquals("1 correct in a row", revisionCorrectStreakLabel(1)); assertEquals("5 correct in a row", revisionCorrectStreakLabel(5)) }
}
