package com.rajankumar.encyclopaedia.feature.knowledge

import com.rajankumar.encyclopaedia.R
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.xmlpull.v1.XmlPullParser

@RunWith(RobolectricTestRunner::class)
class AiCredentialBackupRulesTest {
  private val credentialPreferenceFile = "${LocalAiApiKeyStore.PREFERENCES_NAME}.xml"

  @Test
  fun `modern Android backup rules exclude AI credentials`() {
    assertTrue(credentialPreferenceFile in excludedSharedPreferences(R.xml.backup_rules))
  }

  @Test
  fun `legacy Android backup rules exclude AI credentials`() {
    assertTrue(credentialPreferenceFile in excludedSharedPreferences(R.xml.backup_rules_legacy))
  }

  private fun excludedSharedPreferences(resourceId: Int): Set<String> {
    val parser = RuntimeEnvironment.getApplication().resources.getXml(resourceId)
    val paths = mutableSetOf<String>()
    var event = parser.eventType
    while (event != XmlPullParser.END_DOCUMENT) {
      if (
        event == XmlPullParser.START_TAG &&
        parser.name == "exclude" &&
        parser.getAttributeValue(null, "domain") == "sharedpref"
      ) {
        parser.getAttributeValue(null, "path")?.let(paths::add)
      }
      event = parser.next()
    }
    parser.close()
    return paths
  }
}
