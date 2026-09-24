package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertTrue
import org.junit.Test

class HomeFocusMessageTest {
  @Test fun revisionTakesPriority() = assertTrue(homeFocusMessage(3, 4).contains("revision"))
  @Test fun plannerFollowsClearRevisionQueue() = assertTrue(homeFocusMessage(0, 4).contains("planned"))
}
