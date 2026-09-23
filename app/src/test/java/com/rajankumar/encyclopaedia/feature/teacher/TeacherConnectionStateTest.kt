package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.Assert.assertTrue
import org.junit.Test

class TeacherConnectionStateTest {
  @Test fun offlineMessageProtectsLocalFirstExpectation() {
    assertTrue(TeacherConnectionState.OFFLINE.userMessage().contains("ordinary app features still work"))
  }

  @Test fun authFailureSuggestsRotation() {
    assertTrue(TeacherConnectionState.AUTH_FAILED.userMessage().contains("rotate"))
  }
}
