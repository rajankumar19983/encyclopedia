package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.data.local.PlannerTaskEntity
import org.junit.Assert.*
import org.junit.Test

class PlannerTaskIntegrityTest {
  @Test fun acceptsOpenTask() = assertTrue(PlannerTaskEntity("1", "Study", "2026-09-23").hasValidPlannerFields())
  @Test fun rejectsCompletedTaskWithoutTimestamp() = assertFalse(PlannerTaskEntity("1", "Study", "2026-09-23", isCompleted = true).hasValidPlannerFields())
  @Test fun rejectsMalformedDate() = assertFalse(PlannerTaskEntity("1", "Study", "23-09-2026").hasValidPlannerFields())
}
