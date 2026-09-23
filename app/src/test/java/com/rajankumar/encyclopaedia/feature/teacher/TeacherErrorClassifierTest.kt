package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.Assert.assertEquals
import org.junit.Test

class TeacherErrorClassifierTest {
  @Test fun classifiesCommonFailures() {
    assertEquals(TeacherConnectionState.AUTH_FAILED, classifyTeacherFailure("HTTP 401 Unauthorized"))
    assertEquals(TeacherConnectionState.RATE_LIMITED, classifyTeacherFailure("HTTP 429 rate limit"))
    assertEquals(TeacherConnectionState.OFFLINE, classifyTeacherFailure("Unable to resolve host"))
    assertEquals(TeacherConnectionState.UNAVAILABLE, classifyTeacherFailure("Unexpected response"))
  }
}
