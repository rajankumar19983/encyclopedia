package com.rajankumar.encyclopaedia.feature.home

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeDateLabelTest {
  @Test fun formatsReadableEnglishDate() = assertEquals("Thursday, September 24, 2026", homeDateLabel(LocalDate.of(2026, 9, 24)))
}
