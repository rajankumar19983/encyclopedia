package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrImportMilestoneTest {
  @Test fun allAcceptanceRequirementsCompleteMilestone() = assertTrue(OcrImportMilestone(true, true, true, true, true, true, true, true).complete)
  @Test fun anyMissingRequirementKeepsMilestoneOpen() = assertFalse(OcrImportMilestone(true, true, true, true, false, true, true, true).complete)
}
