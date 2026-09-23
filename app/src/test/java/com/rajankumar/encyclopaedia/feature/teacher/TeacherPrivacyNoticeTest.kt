package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.Assert.assertTrue
import org.junit.Test

class TeacherPrivacyNoticeTest {
  @Test fun contextualNoticeExplainsDataSentAndImageExclusion() {
    val notice = teacherPrivacyNotice(TeacherContext("question", primaryContent = "Q"))
    assertTrue(notice.contains("structured study context"))
    assertTrue(notice.contains("Source images are not attached"))
  }
}
