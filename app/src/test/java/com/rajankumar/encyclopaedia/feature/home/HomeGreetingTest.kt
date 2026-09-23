package com.rajankumar.encyclopaedia.feature.home

import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeGreetingTest {
  @Test fun morningGreeting() = assertEquals("Good morning", homeGreeting(LocalTime.of(8, 0)))
  @Test fun afternoonGreeting() = assertEquals("Good afternoon", homeGreeting(LocalTime.of(14, 0)))
  @Test fun eveningGreeting() = assertEquals("Good evening", homeGreeting(LocalTime.of(19, 0)))
  @Test fun lateNightGreeting() = assertEquals("Welcome back", homeGreeting(LocalTime.of(1, 0)))
}
