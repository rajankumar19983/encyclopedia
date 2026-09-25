package com.rajankumar.encyclopaedia.feature.knowledge

import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Test

class AiCredentialBackupRulesTest {
  private val credentialPreferenceFile = "${LocalAiApiKeyStore.PREFERENCES_NAME}.xml"

  @Test
  fun `modern Android backup rules exclude AI credentials`() {
    val rules = readSourceXml("backup_rules.xml")

    assertTrue(rules.contains("domain=\"sharedpref\" path=\"$credentialPreferenceFile\""))
  }

  @Test
  fun `legacy Android backup rules exclude AI credentials`() {
    val rules = readSourceXml("backup_rules_legacy.xml")

    assertTrue(rules.contains("domain=\"sharedpref\" path=\"$credentialPreferenceFile\""))
  }

  private fun readSourceXml(fileName: String): String {
    val candidates = listOf(
      File("src/main/res/xml/$fileName"),
      File("app/src/main/res/xml/$fileName"),
    )
    val file = candidates.firstOrNull(File::isFile)
      ?: error("Could not locate $fileName from ${File(".").absolutePath}")
    return file.readText()
  }
}
