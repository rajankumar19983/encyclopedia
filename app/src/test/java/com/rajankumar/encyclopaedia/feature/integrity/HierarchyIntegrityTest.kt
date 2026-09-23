package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class HierarchyIntegrityTest {
  @Test fun acceptsTree() = assertTrue(hasAcyclicParents(mapOf("a" to null, "b" to "a")))
  @Test fun rejectsSelfCycle() = assertFalse(hasAcyclicParents(mapOf("a" to "a")))
  @Test fun rejectsMultiNodeCycle() = assertFalse(hasAcyclicParents(mapOf("a" to "b", "b" to "a")))
}
