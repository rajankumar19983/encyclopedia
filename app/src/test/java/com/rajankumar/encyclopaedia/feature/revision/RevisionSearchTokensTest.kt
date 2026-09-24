package com.rajankumar.encyclopaedia.feature.revision

import org.junit.Assert.assertEquals
import org.junit.Test

class RevisionSearchTokensTest {
  @Test fun tokensAreNormalizedAndUnique() = assertEquals(listOf("operating", "system"), revisionSearchTokens(" Operating   system operating "))
  @Test fun oneCharacterNoiseIsIgnored() = assertEquals(listOf("dbms"), revisionSearchTokens("a DBMS"))
}
