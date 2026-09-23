package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class EntityIdIntegrityTest {
  @Test fun acceptsUniqueIds() = assertTrue(hasUniqueNonBlankIds(listOf("a", "b")))
  @Test fun rejectsBlankIds() = assertFalse(hasUniqueNonBlankIds(listOf("a", "")))
  @Test fun rejectsDuplicateIds() = assertFalse(hasUniqueNonBlankIds(listOf("a", "a")))
}
