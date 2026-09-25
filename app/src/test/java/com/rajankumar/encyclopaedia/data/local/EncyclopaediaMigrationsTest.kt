package com.rajankumar.encyclopaedia.data.local

import org.junit.Assert.assertArrayEquals
import org.junit.Test

class EncyclopaediaMigrationsTest {
  @Test
  fun migrationChainIsContiguous() {
    assertArrayEquals(intArrayOf(1, 2, 3, 4), ALL_MIGRATIONS.map { it.startVersion }.toIntArray())
    assertArrayEquals(intArrayOf(2, 3, 4, 5), ALL_MIGRATIONS.map { it.endVersion }.toIntArray())
  }
}
