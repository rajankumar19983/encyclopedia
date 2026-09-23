package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class RelationshipIntegrityTest {
  @Test fun acceptsKnownReferences() = assertTrue(allReferencesExist(listOf("a"), setOf("a", "b")))
  @Test fun rejectsUnknownReferences() = assertFalse(allReferencesExist(listOf("c"), setOf("a", "b")))
  @Test fun acceptsNullOptionalReference() = assertTrue(allOptionalReferencesExist(listOf(null, "a"), setOf("a")))
}
