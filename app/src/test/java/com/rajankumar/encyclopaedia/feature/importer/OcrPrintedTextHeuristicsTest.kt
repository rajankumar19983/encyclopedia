package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrPrintedTextHeuristicsTest {
  @Test fun keepsNormalPrintedEnglishLine() = assertTrue(assessOcrLine("Which protocol uses port 53?").usable)
  @Test fun rejectsDevanagariLine() = assertFalse(assessOcrLine("उत्तर DNS").usable)
  @Test fun rejectsObviousSymbolNoise() = assertFalse(assessOcrLine("@@@ ### /// ???").usable)
}
