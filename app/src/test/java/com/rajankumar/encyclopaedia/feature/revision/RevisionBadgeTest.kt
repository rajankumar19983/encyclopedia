package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionBadgeTest {
  @Test fun emptyBadgeIsReadable() = assertEquals("No questions due", RevisionBadge(0, false).label)
  @Test fun urgentBadgeAnnouncesUrgency() = assertEquals("3 due, urgent revision available", RevisionBadge(3, true).label)
}
