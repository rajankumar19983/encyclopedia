package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class ImportFingerprintTest {
  @Test fun formattingDifferencesProduceSameFingerprint() {
    val first = EditableImportDraft("Which protocol is used for WEB?", listOf("HTTP", "FTP"), "A")
    val second = EditableImportDraft(" which  protocol is used for web ", listOf(" http ", "ftp"), "A")
    assertEquals(first.importFingerprint(), second.importFingerprint())
  }

  @Test fun optionContentParticipatesInFingerprint() {
    val first = EditableImportDraft("Choose one", listOf("HTTP", "FTP"), "A")
    val second = EditableImportDraft("Choose one", listOf("HTTP", "SMTP"), "A")
    assert(first.importFingerprint() != second.importFingerprint())
  }
}
