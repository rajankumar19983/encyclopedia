package com.rajankumar.encyclopaedia.feature.accessibility

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechChunkCountTest {
  @Test fun emptyTextHasNoSections() = assertEquals("No speech sections", speechChunkCountLabel(""))
  @Test fun shortTextHasOneSection() = assertEquals("1 speech section", speechChunkCountLabel("Short text"))
}
