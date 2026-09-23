package com.rajankumar.encyclopaedia.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeStatModelsTest {
  @Test fun buildsFourDashboardStats() {
    val stats = buildHomeStatModels(3, 20, 8, 6, 10)
    assertEquals(listOf("Topics", "Questions", "Practice", "Accuracy"), stats.map { it.title })
    assertEquals("50% coverage", stats[1].detail.substringAfter("• "))
    assertEquals("75%", stats[3].value)
  }
}
