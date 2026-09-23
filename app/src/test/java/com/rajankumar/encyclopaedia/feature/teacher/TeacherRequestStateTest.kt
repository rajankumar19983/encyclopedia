package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TeacherRequestStateTest {
  @Test fun sendingPreventsDuplicateSubmit() {
    assertFalse(TeacherRequestState.Sending.canSend())
    assertTrue(TeacherRequestState.Idle.canSend())
    assertTrue(TeacherRequestState.Failed(TeacherConnectionState.OFFLINE).canSend())
  }
}
