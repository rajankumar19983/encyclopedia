package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionTopicCountTest {
  @Test fun labelsTopicCounts() { assertEquals("No weak topics", revisionTopicCountLabel(0)); assertEquals("1 topic needs revision", revisionTopicCountLabel(1)); assertEquals("6 topics need revision", revisionTopicCountLabel(6)) }
}
