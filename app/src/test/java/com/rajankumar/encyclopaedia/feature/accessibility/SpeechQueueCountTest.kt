package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechQueueCountTest {
  @Test fun labelsQueueCounts() { assertEquals("Nothing queued", speechQueueCountLabel(0)); assertEquals("1 section queued", speechQueueCountLabel(1)); assertEquals("3 sections queued", speechQueueCountLabel(3)) }
}
